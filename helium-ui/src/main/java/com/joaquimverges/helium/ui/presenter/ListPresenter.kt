package com.joaquimverges.helium.ui.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.util.async
import com.joaquimverges.helium.ui.repository.BaseRepository
import com.joaquimverges.helium.ui.state.ListViewState
import com.joaquimverges.helium.ui.util.RefreshPolicy

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
) : BasePresenter<ListViewState<List<T>>, E>() {

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
                { sources ->
                    if (sources.isNotEmpty()) {
                        pushState(ListViewState.DataReady(sources))
                    } else {
                        pushState(ListViewState.Empty())
                    }
                },
                { error -> pushState(ListViewState.Error(error)) }
            ).autoDispose()
    }

    override fun onViewEvent(event: E) {
        // Subclasses can implement this if needed
    }
}