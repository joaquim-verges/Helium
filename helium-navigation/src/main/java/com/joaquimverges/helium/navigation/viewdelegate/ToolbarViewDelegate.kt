package com.joaquimverges.helium.navigation.viewdelegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.navigation.R
import com.joaquimverges.helium.navigation.event.ToolbarEvent

/**
 * A view delegate that renders a toolbar
 */
class ToolbarViewDelegate(
    view: View,
    @MenuRes menuResId: Int? = null,
    actionBarCustomization: ((ActionBar) -> Unit)? = null,
    toolbarCustomization: ((Toolbar) -> Unit)? = null
) : UiBlock<BlockState, ToolbarEvent>(view) {

    constructor(
        inflater: LayoutInflater,
        @LayoutRes layoutResId: Int = R.layout.toolbar_layout,
        parentContainer: ViewGroup? = null,
        addToContainer: Boolean = false,
        @MenuRes menuResId: Int? = null,
        actionBarCustomization: ((ActionBar) -> Unit)? = null,
        toolbarCustomization: ((Toolbar) -> Unit)? = null,
        view: View = inflater.inflate(layoutResId, parentContainer, addToContainer)
    ) : this(view, menuResId, actionBarCustomization, toolbarCustomization)

    private val toolbar = (view as? Toolbar) ?: findView(R.id.toolbar)

    init {
        (context as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.apply {
                actionBarCustomization?.invoke(this)
            }
        }
        toolbar.setOnMenuItemClickListener {
            pushEvent(ToolbarEvent.MenuItemClicked(it.itemId))
            true
        }
        toolbarCustomization?.invoke(toolbar)
        toolbar.setNavigationOnClickListener { pushEvent(ToolbarEvent.HomeClicked) }
        menuResId?.let { toolbar.inflateMenu(it) }
    }

    override fun render(blockState: BlockState) {
        // no-op
    }
}