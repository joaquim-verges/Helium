package com.joaquimverges.helium.navigation.viewdelegate

import androidx.annotation.MenuRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.R
import com.joaquimverges.helium.navigation.event.ToolbarEvent

/**
 * A view delegate that renders a toolbar
 */
class ToolbarViewDelegate(
    inflater: LayoutInflater,
    @LayoutRes layoutResId : Int = R.layout.toolbar_layout,
    parentContainer: ViewGroup? = null,
    addToContainer: Boolean = false,
    @MenuRes menuResId: Int? = null,
    actionBarCustomization: ((ActionBar) -> Unit)? = null,
    toolbarCustomization: ((Toolbar) -> Unit)? = null
) : BaseViewDelegate<ViewState, ToolbarEvent>(
    layoutResId,
    inflater,
    parentContainer,
    addToContainer
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
        toolbarCustomization?.invoke(toolbar)
        toolbar.setNavigationOnClickListener { pushEvent(ToolbarEvent.HomeClicked) }
        menuResId?.let { toolbar.inflateMenu(it) }
    }

    override fun render(viewState: ViewState) {
        // no-op
    }
}