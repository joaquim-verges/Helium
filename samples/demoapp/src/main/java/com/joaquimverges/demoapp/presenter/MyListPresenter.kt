package com.joaquimverges.demoapp.presenter

import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.joaquimverges.demoapp.data.Colors
import com.joaquimverges.demoapp.data.MyListRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.event.ClickEvent
import com.joaquimverges.helium.presenter.DataListPresenter
import com.joaquimverges.helium.util.RefreshPolicy
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class MyListPresenter : DataListPresenter<MyItem, ClickEvent<MyItem>>(MyListRepository(), RefreshPolicy(5, TimeUnit.MINUTES)) {

    override fun onViewEvent(event: ClickEvent<MyItem>) {
        val context = event.view.context
        val clickedColor = ContextCompat.getColor(context, event.data.color)
        Toast.makeText(context, "Clicked color ${Colors.toHexString(clickedColor)}", Toast.LENGTH_SHORT).show()
    }
}