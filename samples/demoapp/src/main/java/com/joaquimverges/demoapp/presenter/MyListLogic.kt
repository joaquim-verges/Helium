package com.joaquimverges.demoapp.presenter

import androidx.core.content.ContextCompat
import android.widget.Toast
import com.joaquimverges.demoapp.data.Colors
import com.joaquimverges.demoapp.data.MyListRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.ListLogic
import com.joaquimverges.helium.ui.util.RefreshPolicy
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class MyListLogic : ListLogic<MyItem, ClickEvent<MyItem>>(MyListRepository(), RefreshPolicy(5, TimeUnit.MINUTES)) {

    override fun onUiEvent(event: ListBlockEvent<ClickEvent<MyItem>>) {
        when(event) {
           is ListBlockEvent.ListItemEvent -> {
               val context = event.itemEvent.view.context
               val clickedColor = ContextCompat.getColor(context, event.itemEvent.data.color)
               Toast.makeText(context, "Clicked color ${Colors.toHexString(clickedColor)}", Toast.LENGTH_SHORT).show()
           }
        }

    }
}