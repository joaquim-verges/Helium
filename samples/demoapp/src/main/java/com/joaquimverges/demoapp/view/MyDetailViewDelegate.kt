package com.joaquimverges.demoapp.view

import android.support.v4.content.ContextCompat
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.view.isVisible
import com.joaquimverges.demoapp.R
import com.joaquimverges.demoapp.data.Colors
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.event.ClickEvent
import com.joaquimverges.helium.state.NetworkViewState
import com.joaquimverges.helium.viewdelegate.BaseViewDelegate

/**
 * @author joaquim
 */
class MyDetailViewDelegate(inflater: LayoutInflater, container: ViewGroup?)
    : BaseViewDelegate<NetworkViewState<MyItem>, ClickEvent<MyItem>>(
        R.layout.detail_layout,
        inflater,
        container) {

    private val detailColorView: View = view.findViewById(R.id.detail_color_view)
    private val colorNameView: TextView = view.findViewById(R.id.color_name)
    private val loader: ProgressBar = view.findViewById(R.id.loader)

    override fun render(viewState: NetworkViewState<MyItem>) {
        TransitionManager.beginDelayedTransition(detailColorView as ViewGroup)
        when (viewState) {
            is NetworkViewState.Loading -> {
                loader.isVisible = true
                detailColorView.isVisible = false
            }
            is NetworkViewState.DataReady -> {
                val item = viewState.data
                val color = ContextCompat.getColor(context, item.color)
                loader.isVisible = false
                colorNameView.text = Colors.toHexString(color)
                detailColorView.isVisible = true
                detailColorView.setBackgroundColor(color)
                detailColorView.setOnClickListener { v -> pushEvent(ClickEvent(v, item)) }
            }
        }
    }
}