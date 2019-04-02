package com.joaquimverges.helium.navigation.viewdelegate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.R

/**
 * A view delegate that renders a screen with a collapsing toolbar that scrolls away with the content
 */
class CollapsingToolbarScreenViewDelegate(
    inflater: LayoutInflater,
    private val toolbarViewDelegate: ToolbarViewDelegate,
    private val contentViewDelegate: BaseViewDelegate<*, *>,
    private val headerViewDelegate: BaseViewDelegate<*, *>? = null // TODO
) : BaseViewDelegate<ViewState, ViewEvent>(
    R.layout.collapsing_toolbar_screen_layout,
    inflater
) {
    private val toolbarContainer = findView<ViewGroup>(R.id.collapsing_toolbar_container)
    private val listContainer = findView<ViewGroup>(R.id.collapsing_list_container)

    init {
        toolbarContainer.addView(toolbarViewDelegate.view)
        listContainer.addView(contentViewDelegate.view)
        // TODO configuration
    }

    override fun render(viewState: ViewState) {

    }
}