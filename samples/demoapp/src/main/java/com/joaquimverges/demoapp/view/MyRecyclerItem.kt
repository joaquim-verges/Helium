package com.joaquimverges.demoapp.view

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.demoapp.R
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.event.ClickEvent
import com.joaquimverges.helium.viewdelegate.BaseRecyclerViewItem

/**
 * @author joaquim
 */
class MyRecyclerItem(@LayoutRes layoutResId: Int, inflater: LayoutInflater, container: ViewGroup)
    : BaseRecyclerViewItem<MyItem, ClickEvent<MyItem>>(layoutResId, inflater, container) {

    override fun bind(data: MyItem) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, data.color))
        view.setOnClickListener { onViewEvent(ClickEvent(view, data)) }
    }
}