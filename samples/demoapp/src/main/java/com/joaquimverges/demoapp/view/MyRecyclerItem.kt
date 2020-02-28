package com.joaquimverges.demoapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.joaquimverges.demoapp.R
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.ui.list.adapter.ListItem

/**
 * @author joaquim
 */
class MyRecyclerItem(inflater: LayoutInflater, container: ViewGroup) :
    ListItem<MyItem, ClickEvent<MyItem>>(R.layout.list_item_layout, inflater, container) {

    override fun bind(data: MyItem) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, data.color))
        view.setOnClickListener { pushEvent(ClickEvent(view, data)) }
    }
}