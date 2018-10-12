package com.joaquimverges.demoapp.presenter

import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.joaquimverges.demoapp.data.Colors
import com.joaquimverges.demoapp.data.MyListRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.ui.presenter.ListPresenter
import com.joaquimverges.helium.ui.util.RefreshPolicy
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class MyListPresenter : ListPresenter<MyItem, ClickEvent<MyItem>>(MyListRepository(), RefreshPolicy(5, TimeUnit.MINUTES)) {

    override fun onViewEvent(event: ClickEvent<MyItem>) {
        val context = event.view.context
        val clickedColor = ContextCompat.getColor(context, event.data.color)
        Toast.makeText(context, "Clicked color ${Colors.toHexString(clickedColor)}", Toast.LENGTH_SHORT).show()
    }
}