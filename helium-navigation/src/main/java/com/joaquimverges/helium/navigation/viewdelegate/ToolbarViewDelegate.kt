package com.joaquimverges.helium.navigation.viewdelegate

import android.support.annotation.MenuRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.R
import com.joaquimverges.helium.navigation.event.ToolbarEvent

/**
 * A view delegate that renders a toolbar
 */
class ToolbarViewDelegate(
    inflater: LayoutInflater,
    @MenuRes menuResId: Int,
    actionBarCustomization: ((ActionBar) -> Unit)? = null
) : BaseViewDelegate<ViewState, ToolbarEvent>(
    R.layout.toolbar_layout,
    inflater
) {
    private val toolbar = findView<Toolbar>(R.id.toolbar)
    private val actionBar: ActionBar?

    init {
        (context as? AppCompatActivity)?.setSupportActionBar(toolbar)
        actionBar = (context as? AppCompatActivity)?.supportActionBar?.apply {
            actionBarCustomization?.invoke(this)
        }
        toolbar.setOnMenuItemClickListener {
            pushEvent(ToolbarEvent.MenuItemClicked(it.itemId))
            true
        }
        toolbar.setNavigationOnClickListener { pushEvent(ToolbarEvent.HomeClicked) }
        toolbar.inflateMenu(menuResId)
    }

    override fun render(viewState: ViewState) {
        // no-op
    }
}