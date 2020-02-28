package com.joaquimverges.demoapp.ui

import androidx.core.content.ContextCompat
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.joaquimverges.demoapp.R
import com.joaquimverges.demoapp.data.Colors
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.ui.list.state.DataLoadState
import com.joaquimverges.helium.core.UiBlock

/**
 * @author joaquim
 */
class MyDetailUi(inflater: LayoutInflater, container: ViewGroup?)
    : UiBlock<DataLoadState<MyItem>, ClickEvent<MyItem>>(
        R.layout.detail_layout,
        inflater,
        container) {

    private val detailColorView: View = view.findViewById(R.id.detail_color_view)
    private val colorNameView: TextView = view.findViewById(R.id.color_name)
    private val loader: ProgressBar = view.findViewById(R.id.loader)

    override fun render(viewState: DataLoadState<MyItem>) {
        TransitionManager.beginDelayedTransition(detailColorView as ViewGroup)
        when (viewState) {
            is DataLoadState.Loading -> {
                loader.visibility = View.VISIBLE
                detailColorView.visibility = View.GONE
            }
            is DataLoadState.Ready -> {
                val item = viewState.data
                val color = ContextCompat.getColor(context, item.color)
                loader.visibility = View.GONE
                colorNameView.text = Colors.toHexString(color)
                detailColorView.visibility = View.VISIBLE
                detailColorView.setBackgroundColor(color)
                detailColorView.setOnClickListener { v -> pushEvent(ClickEvent(v, item)) }
            }
        }
    }
}