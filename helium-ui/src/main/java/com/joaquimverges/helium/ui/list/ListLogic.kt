package com.joaquimverges.helium.ui.list

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.repository.ListRepository
import com.joaquimverges.helium.ui.util.RefreshPolicy
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * A Typical List logic implementation:
 * - loads data from a [ListRepository] asynchronously when the activity/fragment is resumed
 * - publishes loading states (loading, empty, error, success) to the attached UiBlock
 * - handles optional pagination
 *
 * Optional: pass a RefreshPolicy to control how often the data should get reloaded.
 * default is to refresh on every resume. Consider passing your own refresh policy to meet your use case.
 */
open class ListLogic<T, E : BlockEvent>(
    private val repository: ListRepository<List<T>>,
    private val refreshPolicy: RefreshPolicy = RefreshPolicy(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LogicBlock<DataLoadState<List<T>>, ListBlockEvent<E>>() {

    sealed class PaginationEvent<T> {
        data class FirstPageLoaded<T>(val data: List<T>) : PaginationEvent<T>()
        data class AdditionalPageLoaded<T>(val data: List<T>) : PaginationEvent<T>()
    }

    private val paginationEvents = PublishSubject.create<PaginationEvent<T>>()

    init {
        paginationEvents.scan<DataLoadState<List<T>>>(
            DataLoadState.Init(),
            { prevState, paginationEvent ->
                when (paginationEvent) {
                    is PaginationEvent.FirstPageLoaded -> {
                        DataLoadState.Ready(paginationEvent.data)
                    }
                    is PaginationEvent.AdditionalPageLoaded -> {
                        when (prevState) {
                            is DataLoadState.Ready -> {
                                DataLoadState.Ready(prevState.data + paginationEvent.data)
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
        flow { emit(repository.getFirstPage()) }
            .flowOn(dispatcher)
            .onStart { pushState(DataLoadState.Loading()) }
            .onCompletion { cause ->
                if (cause == null) {
                    refreshPolicy.updateLastRefreshedTime()
                }
            }
            .catch { error -> pushState(DataLoadState.Error(error)) }
            .onEach { data ->
                if (data.isNotEmpty()) {
                    paginationEvents.onNext(PaginationEvent.FirstPageLoaded(data))
                } else {
                    pushState(DataLoadState.Empty())
                }
            }.launchInBlock()
    }

    fun paginate() {
        flow { emit(repository.paginate()) }
            .filterNotNull()
            .flowOn(dispatcher)
            .catch { error -> pushState(DataLoadState.Error(error)) }
            .onEach { paginatedData ->
                if (paginatedData.isNotEmpty()) {
                    paginationEvents.onNext(PaginationEvent.AdditionalPageLoaded(paginatedData))
                }
            }.launchInBlock()
    }

    override fun onUiEvent(event: ListBlockEvent<E>) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}