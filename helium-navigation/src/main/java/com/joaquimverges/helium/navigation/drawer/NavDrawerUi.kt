package com.joaquimverges.helium.navigation.drawer

import androidx.drawerlayout.widget.DrawerLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.navigation.R

/**
 * A UiBlock that presents 2 UiBlocks in the form of a navigation drawer and main content
 */
class NavDrawerUi(
    mainContentUi: UiBlock<*, *>,
    drawerUi: UiBlock<*, *>,
    gravity: Int = Gravity.START,
    drawerCustomisation: ((DrawerLayout) -> Unit)? = null
) : UiBlock<NavDrawerState, NavDrawerEvent>(
    R.layout.drawer_layout,
    LayoutInflater.from(mainContentUi.view.context)
) {

    private val drawerLayout = findView<DrawerLayout>(R.id.drawer_layout)
    private val mainContainer = findView<ViewGroup>(R.id.main_container)
    private val drawerContainer = findView<ViewGroup>(R.id.drawer_container)

    init {
        drawerCustomisation?.invoke(drawerLayout)
        (drawerContainer.layoutParams as DrawerLayout.LayoutParams).gravity = gravity
        mainContainer.addView(mainContentUi.view)
        drawerContainer.addView(drawerUi.view)
    }

    override fun render(state: NavDrawerState) {
        when (state) {
            NavDrawerState.Opened -> drawerLayout.openDrawer(drawerContainer)
            NavDrawerState.Closed -> drawerLayout.closeDrawer(drawerContainer)
        }
    }
}