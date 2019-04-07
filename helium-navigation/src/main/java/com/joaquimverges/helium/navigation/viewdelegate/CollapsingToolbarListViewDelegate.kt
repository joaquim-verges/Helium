package com.joaquimverges.helium.navigation.viewdelegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.R

/**
 * A view delegate that renders a screen with a collapsing toolbar that scrolls away with the content
 */
class CollapsingToolbarScreenViewDelegate(
    inflater: LayoutInflater,
    contentViewDelegate: BaseViewDelegate<*, *>,
    backdropViewDelegate: BaseViewDelegate<*, *>? = null,
    @MenuRes menuResId: Int? = null,
    collapsingLayoutCustomization: ((CollapsingToolbarLayout) -> Unit)? = null,
    actionBarCustomization: ((ActionBar) -> Unit)? = null,
    toolbarCustomization: ((Toolbar) -> Unit)? = null
) : BaseViewDelegate<ViewState, ViewEvent>(
    R.layout.collapsing_toolbar_screen_layout,
    inflater
) {
    private val collapsingToolbarLayout = findView<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
    private val toolbarContainer = findView<ViewGroup>(R.id.collapsing_toolbar)
    private val listContainer = findView<ViewGroup>(R.id.collapsing_list_container)
    private val backdropContainer = findView<ViewGroup>(R.id.collapsing_backdrop_container)

    val toolbarViewDelegate = ToolbarViewDelegate(toolbarContainer, menuResId, actionBarCustomization, toolbarCustomization)

    init {
        listContainer.addView(contentViewDelegate.view)
        backdropViewDelegate?.let { backdropContainer.addView(it.view) }
        collapsingLayoutCustomization?.invoke(collapsingToolbarLayout)
    }

    override fun render(viewState: ViewState) {

    }
}