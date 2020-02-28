package com.joaquimverges.helium.ui.list

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.util.async
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.repository.ListRepository
import com.joaquimverges.helium.ui.list.state.ListBlockState
import com.joaquimverges.helium.ui.util.RefreshPolicy
import io.reactivex.subjects.PublishSubject

/**
 * A Typical List logic implementation:
 * - loads data from a Repository asynchronously when the activity/fragment is resumed
 * - publishes loading states (loading, empty, error, success) to the attached UiBlock
 *
 * Optional: pass a RefreshPolicy to control how often the data should get reloaded.
 * default is to refresh on every resume. Consider passing your own refresh policy to meet your use case.
 */
open class ListLogic<T, E : BlockEvent>(
    private val repository: ListRepository<List<T>>,
    private val refreshPolicy: RefreshPolicy = RefreshPolicy()
) : LogicBlock<ListBlockState<List<T>>, ListBlockEvent<E>>() {

    sealed class PaginationEvent<T> {
        data class FirstPageLoaded<T>(val data: List<T>) : PaginationEvent<T>()
        data class AdditionalPageLoaded<T>(val data: List<T>) : PaginationEvent<T>()
    }

    private val paginationEvents = PublishSubject.create<PaginationEvent<T>>()

    init {
        paginationEvents.scan<ListBlockState<List<T>>>(
            ListBlockState.Init(),
            { prevState, paginationEvent ->
                when (paginationEvent) {
                    is PaginationEvent.FirstPageLoaded -> {
                        ListBlockState.DataReady(paginationEvent.data)
                    }
                    is PaginationEvent.AdditionalPageLoaded -> {
                        when (prevState) {
                            is ListBlockState.DataReady -> {
                                ListBlockState.DataReady(prevState.data + paginationEvent.data)
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
            loadFirstPage()
        }
    }

    fun loadFirstPage() {
        repository.getData()
            .async()
            .doOnSubscribe { pushState(ListBlockState.Loading()) }
            .doOnSuccess { refreshPolicy.updateLastRefreshedTime() }
            .subscribe(
                { data ->
                    if (data.isNotEmpty()) {
                        paginationEvents.onNext(PaginationEvent.FirstPageLoaded(data))
                    } else {
                        pushState(ListBlockState.Empty())
                    }
                },
                { error -> pushState(ListBlockState.Error(error)) }
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
                { error -> pushState(ListBlockState.Error(error)) }
            ).autoDispose()
    }

    override fun onUiEvent(event: ListBlockEvent<E>) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}