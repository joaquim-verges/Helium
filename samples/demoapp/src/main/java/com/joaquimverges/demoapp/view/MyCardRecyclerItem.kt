package com.joaquimverges.demoapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.joaquimverges.demoapp.R
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.ui.viewdelegate.CardRecyclerItem

/**
 * @author joaquim
 */
class MyCardRecyclerItem(inflater: LayoutInflater, container: ViewGroup)
    : CardRecyclerItem<MyItem, ClickEvent<MyItem>>(R.layout.grid_item_layout, inflater, container) {

    override fun bind(data: MyItem) {
        cardLayout.setCardBackgroundColor(ContextCompat.getColor(view.context, data.color))
        cardLayout.setOnClickListener { pushEvent(ClickEvent(view, data)) }
    }
}