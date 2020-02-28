package com.joaquimverges.helium.ui.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.BasePresenter
import com.joaquimverges.helium.core.util.async
import com.joaquimverges.helium.ui.event.ListViewEvent
import com.joaquimverges.helium.ui.repository.BaseRepository
import com.joaquimverges.helium.ui.state.ListViewState
import com.joaquimverges.helium.ui.util.RefreshPolicy
import io.reactivex.subjects.PublishSubject

/**
 * A Typical Presenter base implementation:
 * - loads data from a Repository asynchronously when the activity/fragment is resumed
 * - publishes network states (loading, empty, error, success) to the attached ViewDelegate
 *
 * Optional: pass a RefreshPolicy to control how often the data should get reloaded.
 * default is to refresh on every resume. Consider passing your own refresh policy to meet your use case.
 */
open class ListPresenter<T, E : ViewEvent>(
    private val repository: BaseRepository<List<T>>,
    private val refreshPolicy: RefreshPolicy = RefreshPolicy()
) : BasePresenter<ListViewState<List<T>>, ListViewEvent<E>>() {

    sealed class PaginationEvent<T> {
        data class FirstPageLoaded<T>(val data: List<T>) : PaginationEvent<T>()
        data class AdditionalPageLoaded<T>(val data: List<T>) : PaginationEvent<T>()
    }

    private val paginationEvents = PublishSubject.create<PaginationEvent<T>>()

    init {
        paginationEvents.scan<ListViewState<List<T>>>(
            ListViewState.Init(),
            { prevState, paginationEvent ->
                when (paginationEvent) {
                    is PaginationEvent.FirstPageLoaded -> {
                        ListViewState.DataReady(paginationEvent.data)
                    }
                    is PaginationEvent.AdditionalPageLoaded -> {
                        when (prevState) {
                            is ListViewState.DataReady -> {
                                ListViewState.DataReady(prevState.data + paginationEvent.data)
                            }
                            else -> prevState
                        }
                    }
                }
            })
            .subscribe { state ->
                pushState(state)
            }.autoDispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun refreshIfNeeded() {
        if (refreshPolicy.shouldRefresh()) {
            loadData()
        }
    }

    fun loadData() {
        repository.getData()
            .async()
            .doOnSubscribe { pushState(ListViewState.Loading()) }
            .doOnSuccess { refreshPolicy.updateLastRefreshedTime() }
            .subscribe(
                { data ->
                    if (data.isNotEmpty()) {
                        paginationEvents.onNext(PaginationEvent.FirstPageLoaded(data))
                    } else {
                        pushState(ListViewState.Empty())
                    }
                },
                { error -> pushState(ListViewState.Error(error)) }
            ).autoDispose()
    }

    fun paginate() {
        repository.paginate()
            .async()
            .subscribe(
                { paginatedData ->
                    if (paginatedData.isNotEmpty()) {
                        paginationEvents.onNext(PaginationEvent.AdditionalPageLoaded(paginatedData))
                    }
                },
                { error -> pushState(ListViewState.Error(error)) }
            ).autoDispose()
    }

    override fun onViewEvent(event: ListViewEvent<E>) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}