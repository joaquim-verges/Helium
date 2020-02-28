package com.joaquimverges.helium.navigation.toolbar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.navigation.R

/**
 * A UiBlock that renders a screen with a collapsing toolbar that scrolls away with the content
 * @param inflater
 * @param contentUi: The content of the screen (should contain a recyclerView or NestedScrollView)
 * @param backdropUi: Optional header to display behind the toolbar (usually an image or solid color)
 * @param scrollConfiguration: Optional configuration for the scroll behaviors
 * @param menuResId: Optional xml menu file to populate the toolbar
 * @param collapsingLayoutCustomization: Extra CollapsingToolbarLayout customization (likely title and styles)
 * @param actionBarCustomization: Extra customization for the ActionBar (likely up navigation controls)
 * @param toolbarCustomization: Extra customization for the Toolbar (any other styling)
 */
open class CollapsingToolbarScreenUi<S: BlockState, E: BlockEvent>(
    inflater: LayoutInflater,
    contentUi: UiBlock<*, *>,
    backdropUi: UiBlock<*, *>? = null,
    scrollConfiguration: ScrollConfiguration = backdropUi?.let {
        ScrollConfiguration.defaultWithBackdrop()
    } ?: ScrollConfiguration.default(),
    // Toolbar customization
    @MenuRes menuResId: Int? = null,
    appBarCustomization: ((AppBarLayout) -> Unit)? = null,
    collapsingLayoutCustomization: ((CollapsingToolbarLayout) -> Unit)? = null,
    actionBarCustomization: ((ActionBar) -> Unit)? = null,
    toolbarCustomization: ((Toolbar) -> Unit)? = null
) : UiBlock<S, E>(
    R.layout.collapsing_toolbar_screen_layout,
    inflater
) {

    private val appBarLayout = findView<AppBarLayout>(R.id.app_bar_layout)
    private val collapsingToolbarLayout = findView<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
    private val toolbarContainer = findView<ViewGroup>(R.id.collapsing_toolbar)
    private val listContainer = findView<ViewGroup>(R.id.collapsing_list_container)
    private val backdropContainer = findView<ViewGroup>(R.id.collapsing_backdrop_container)

    val toolbarUi = ToolbarUi(toolbarContainer, menuResId, actionBarCustomization, toolbarCustomization)

    init {
        collapsingToolbarLayout.isTitleEnabled = false // collapsing toolbar title off by default
        listContainer.addView(contentUi.view)
        backdropUi?.let {
            backdropContainer.addView(it.view)
            // enable collapsing toolbar title if backdrop view is provided
            collapsingToolbarLayout.isTitleEnabled = true
        }
        appBarCustomization?.invoke(appBarLayout)
        collapsingLayoutCustomization?.invoke(collapsingToolbarLayout)
        (collapsingToolbarLayout.layoutParams as AppBarLayout.LayoutParams).scrollFlags = scrollConfiguration.scrollMode.scrollFlags
        (toolbarContainer.layoutParams as CollapsingToolbarLayout.LayoutParams).collapseMode = scrollConfiguration.toolbarCollapseMode.mode
        (backdropContainer.layoutParams as CollapsingToolbarLayout.LayoutParams).collapseMode = scrollConfiguration.backdropCollapseMode.mode
    }

    override fun render(viewState: S) {

    }
}