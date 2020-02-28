package com.joaquimverges.helium.navigation.viewdelegate

import androidx.drawerlayout.widget.DrawerLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.navigation.R
import com.joaquimverges.helium.navigation.event.NavDrawerEvent
import com.joaquimverges.helium.navigation.state.NavDrawerState

/**
 * A view delegate that presents 2 view delegates in the form of a navigation drawer and main content
 */
class NavDrawerViewDelegate(
    mainContentViewDelegate: UiBlock<*, *>,
    drawerViewDelegate: UiBlock<*, *>,
    gravity: Int = Gravity.START,
    drawerCustomisation: ((DrawerLayout) -> Unit)? = null
) : UiBlock<NavDrawerState, NavDrawerEvent>(
    R.layout.drawer_layout,
    LayoutInflater.from(mainContentViewDelegate.view.context)
) {

    private val drawerLayout = findView<DrawerLayout>(R.id.drawer_layout)
    private val mainContainer = findView<ViewGroup>(R.id.main_container)
    private val drawerContainer = findView<ViewGroup>(R.id.drawer_container)

    init {
        drawerCustomisation?.invoke(drawerLayout)
        (drawerContainer.layoutParams as DrawerLayout.LayoutParams).gravity = gravity
        mainContainer.addView(mainContentViewDelegate.view)
        drawerContainer.addView(drawerViewDelegate.view)
    }

    override fun render(viewState: NavDrawerState) {
        when (viewState) {
            NavDrawerState.Opened -> drawerLayout.openDrawer(drawerContainer)
            NavDrawerState.Closed -> drawerLayout.closeDrawer(drawerContainer)
        }
    }
}