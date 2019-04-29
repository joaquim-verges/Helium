package com.joaquimverges.demoapp.view

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.joaquimverges.demoapp.data.Colors
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.ui.viewdelegate.ContentCardRecyclerItem

class MyContentCardRecyclerItem(inflater: LayoutInflater, container: ViewGroup)
    : ContentCardRecyclerItem<MyItem, ClickEvent<MyItem>>(inflater, container) {

    override fun bindTitle(data: MyItem, view: TextView) {
        view.text = data.name
    }

    override fun bindSubtitle(data: MyItem, view: TextView) {
        view.text = Colors.toHexString(ContextCompat.getColor(context, data.color))
    }

    override fun bindImage(data: MyItem, view: ImageView) {
        view.setImageDrawable(ColorDrawable(ContextCompat.getColor(context, data.color)))
        view.setOnClickListener { pushEvent(ClickEvent(view, data)) }
    }
}
