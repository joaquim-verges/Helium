package com.joaquimverges.helium.navigation.bottomnav

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.R

/**
 * UiBlock around a BottomNavigationView
 */
class BottomNavUi(
    view: View,
    @MenuRes menuResId: Int? = null,
    bottmoBarCustomization: ((BottomNavigationView) -> Unit)? = null
) : UiBlock<BlockState, BottomNavEvent>(view) {

    constructor(
        inflater: LayoutInflater,
        @LayoutRes layoutResId: Int = R.layout.bottom_nav_layout,
        parentContainer: ViewGroup? = null,
        addToContainer: Boolean = false,
        @MenuRes menuResId: Int? = null,
        bottmoBarCustomization: ((BottomNavigationView) -> Unit)? = null
    ) : this(inflater.inflate(layoutResId, parentContainer, addToContainer), menuResId, bottmoBarCustomization)

    private val bottomNav = (view as? BottomNavigationView) ?: findView(R.id.bottom_nav)

    init {
        bottmoBarCustomization?.invoke(bottomNav)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            pushEvent(BottomNavEvent.NavItemSelected(item))
            true
        }
        bottomNav.setOnNavigationItemReselectedListener { item ->
            pushEvent(BottomNavEvent.NavItemReSelected(item))
        }
        menuResId?.let { bottomNav.inflateMenu(it) }
    }

    override fun render(viewState: BlockState) {
        // no-op
    }
}