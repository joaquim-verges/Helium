package com.joaquimverges.helium.ui.list

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.repository.ListRepository
import com.joaquimverges.helium.ui.util.RefreshPolicy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.withContext

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

    private val paginationEvents = BroadcastChannel<PaginationEvent<T>>(Channel.BUFFERED)

    init {
        paginationEvents.asFlow().scan<PaginationEvent<T>, DataLoadState<List<T>>>(
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
            .onEach { state ->
                pushState(state)
            }.launchInBlock()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun refreshIfNeeded() {
        if (refreshPolicy.shouldRefresh()) {
            loadFirstPage()
        }
    }

    fun loadFirstPage() {
        launchInBlock {
            try {
                pushState(DataLoadState.Loading())
                val data = withContext(dispatcher) {
                    repository.getFirstPage()
                }
                if (data.isNotEmpty()) {
                    paginationEvents.offer(PaginationEvent.FirstPageLoaded(data))
                } else {
                    pushState(DataLoadState.Empty())
                }
                refreshPolicy.updateLastRefreshedTime()
            } catch (error: Exception) {
                pushState(DataLoadState.Error(error))
            }
        }
    }

    fun paginate() {
        launchInBlock {
            try {
                val data = withContext(dispatcher) {
                    repository.paginate()
                }
                if (data?.isNotEmpty() == true) {
                    paginationEvents.offer(PaginationEvent.AdditionalPageLoaded(data))
                }
            } catch (error: Exception) {
                pushState(DataLoadState.Error(error))
            }
        }
    }

    override fun onUiEvent(event: ListBlockEvent<E>) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}