package com.joaquimverges.helium.navigation.bottomnav

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.R

/**
 * UiBlock around a BottomNavigationView
 */
class BottomNavUi(
    view: View,
    @MenuRes menuResId: Int,
    @NavigationRes navGraph: Int,
    bottomBarCustomization: ((BottomNavigationView) -> Unit)? = null
) : UiBlock<BlockState, BottomNavEvent>(view) {

    constructor(
        inflater: LayoutInflater,
        @MenuRes menuResId: Int,
        @NavigationRes navGraph: Int,
        @LayoutRes layoutResId: Int = R.layout.bottom_nav_screen_layout,
        parentContainer: ViewGroup? = null,
        addToContainer: Boolean = false,
        bottomBarCustomization: ((BottomNavigationView) -> Unit)? = null
    ) : this(inflater.inflate(layoutResId, parentContainer, addToContainer), menuResId, navGraph, bottomBarCustomization)

    private val navContainerView = findView<FragmentContainerView>(R.id.nav_host_fragment)
    private val bottomNav = (view as? BottomNavigationView) ?: findView(R.id.bottom_nav)

    init {
        bottomBarCustomization?.invoke(bottomNav)
        menuResId.let { bottomNav.inflateMenu(it) }
        val navHostFragment = NavHostFragment.create(navGraph)
        (context as FragmentActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, navHostFragment, "nav")
            .setPrimaryNavigationFragment(navHostFragment)
            .runOnCommit {
                val navController = Navigation.findNavController(navContainerView)
                navController.addOnDestinationChangedListener { controller, destination, arguments ->
                    pushEvent(BottomNavEvent.NavigationChanged(context, controller, destination, arguments))
                }
                bottomNav.setupWithNavController(navController)
            }.commit()
        bottomNav.setOnNavigationItemReselectedListener { item ->
            pushEvent(BottomNavEvent.NavItemReselected(context, item.itemId))
        }
    }

    override fun render(state: BlockState) {
        // no-op
    }
}
