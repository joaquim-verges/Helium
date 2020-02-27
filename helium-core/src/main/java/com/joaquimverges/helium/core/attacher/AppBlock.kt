package com.joaquimverges.helium.core.attacher

import androidx.lifecycle.Lifecycle
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.util.autoDispose
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate

/**
 * Class responsible for connecting Presenters and ViewDelegates together.
 */
class AppBlock<S : ViewState, E : ViewEvent>(
    private val presenter: BasePresenter<S, E>,
    private val viewDelegate: BaseViewDelegate<S, E>,
    private val childBlocks: List<AppBlock<*, *>> = emptyList()
) {

    fun assemble() {
        val lifecycle: Lifecycle = viewDelegate.lifecycle
        presenter.observeViewState().autoDispose(lifecycle).subscribe { viewDelegate.render(it) }
        viewDelegate.observer().autoDispose(lifecycle).subscribe { presenter.processViewEvent(it) }
        lifecycle.addObserver(presenter)

        childBlocks.forEach {
            it.assemble()
        }
    }
}

operator fun <S : ViewState, E : ViewEvent> BasePresenter<S, E>.plus(vd: BaseViewDelegate<S,E>) = AppBlock(this, vd)
