package com.joaquimverges.demoapp.view

import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.ui.viewdelegate.BaseRecyclerViewItem

/**
 * @author joaquim
 */
class MyRecyclerItem(@LayoutRes layoutResId: Int, inflater: LayoutInflater, container: ViewGroup)
    : BaseRecyclerViewItem<MyItem, ClickEvent<MyItem>>(layoutResId, inflater, container) {

    override fun bind(data: MyItem) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, data.color))
        view.setOnClickListener { pushEvent(ClickEvent(view, data)) }
    }
}