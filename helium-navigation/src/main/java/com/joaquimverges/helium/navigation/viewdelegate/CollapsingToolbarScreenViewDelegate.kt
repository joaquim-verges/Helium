package com.joaquimverges.helium.navigation.viewdelegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.R

/**
 * A view delegate that renders a screen with a collapsing toolbar that scrolls away with the content
 * @param inflater
 * @param contentViewDelegate: The content of the screen (should contain a recyclerView or NestedScrollView)
 * @param backdropViewDelegate: Optional header to display behind the toolbar (usually an image or solid color)
 * @param scrollConfiguration: Optional configuration for the scroll behaviors
 * @param menuResId: Optional xml menu file to populate the toolbar
 * @param collapsingLayoutCustomization: Extra CollapsingToolbarLayout customization (likely title and styles)
 * @param actionBarCustomization: Extra customization for the ActionBar (likely up navigation controls)
 * @param toolbarCustomization: Extra customization for the Toolbar (any other styling)
 */
class CollapsingToolbarScreenViewDelegate(
    inflater: LayoutInflater,
    contentViewDelegate: BaseViewDelegate<*, *>,
    backdropViewDelegate: BaseViewDelegate<*, *>? = null,
    scrollConfiguration: ScrollConfiguration = backdropViewDelegate?.let {
        ScrollConfiguration.defaultWithBackdrop()
    } ?: ScrollConfiguration.default(),
    // Toolbar customization
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
        collapsingToolbarLayout.isTitleEnabled = false // collapsing toolbar title off by default
        listContainer.addView(contentViewDelegate.view)
        backdropViewDelegate?.let {
            backdropContainer.addView(it.view)
            // enable collapsing toolbar title if backdrop view is provided
            collapsingToolbarLayout.isTitleEnabled = true
        }
        collapsingLayoutCustomization?.invoke(collapsingToolbarLayout)
        (collapsingToolbarLayout.layoutParams as AppBarLayout.LayoutParams).scrollFlags = scrollConfiguration.scrollMode.scrollFlags
        (toolbarContainer.layoutParams as CollapsingToolbarLayout.LayoutParams).collapseMode = scrollConfiguration.toolbarCollapseMode.mode
        (backdropContainer.layoutParams as CollapsingToolbarLayout.LayoutParams).collapseMode = scrollConfiguration.backdropCollapseMode.mode
    }

    override fun render(viewState: ViewState) {

    }
}