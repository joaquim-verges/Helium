package com.joaquimverges.demoapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.ui.GridSpacingDecorator
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.ui.list.ListLogic
import com.joaquimverges.helium.ui.list.ListUi
import com.joaquimverges.helium.ui.list.card.CardListItem
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.repository.ListRepository

class MainActivity : AppCompatActivity() {

    // DATA

    enum class MenuItem(val title: String) {
        LIST_NOT_RETAINED("List (not retained)"),
        LIST_RETAINED("List (retained)"),
        LIST_RETAINED_FRAGMENT("List Fragment (retained)"),
        ADVANCED_LIST("Advanced List"),
        CARD_LIST("Card List"),
        VIEW_PAGER("ViewPager"),
        BOTTOM_NAV("Bottom Navigation")
    }

    class MenuRepository : ListRepository<List<MenuItem>> {
        private fun getMenuItems() = MenuItem.values().toList()
        override suspend fun getFirstPage(): List<MenuItem> = getMenuItems()
    }

    // Logic

    class MenuLogicBlock : ListLogic<MenuItem, ClickEvent<MenuItem>>(MenuRepository()) {

        override fun onUiEvent(event: ListBlockEvent<ClickEvent<MenuItem>>) {
            when (event) {
                is ListBlockEvent.ListItemEvent -> {
                    val itemEvent = event.itemEvent
                    val context = itemEvent.view.context
                    val activityClass = when (itemEvent.data) {
                        MenuItem.LIST_NOT_RETAINED -> SimpleListActivity::class.java
                        MenuItem.LIST_RETAINED -> SimpleListActivityRetained::class.java
                        MenuItem.LIST_RETAINED_FRAGMENT -> SimpleListFragmentActivityRetained::class.java
                        MenuItem.ADVANCED_LIST -> AdvancedListActivity::class.java
                        MenuItem.CARD_LIST -> CardListActivity::class.java
                        MenuItem.VIEW_PAGER -> ViewPagerActivity::class.java
                        MenuItem.BOTTOM_NAV -> BottomNavActivity::class.java
                    }
                    context.startActivity(Intent(context, activityClass))
                }
            }
        }
    }

    // VIEW

    class MenuListItem(
        inflater: LayoutInflater,
        parent: ViewGroup,
        root: View = inflater.inflate(R.layout.menu_item_layout, parent, false)
    ) :
        CardListItem<MenuItem, ClickEvent<MenuItem>>(root, inflater, parent) {

        private val title = root.findViewById<TextView>(R.id.menu_title)

        override fun bind(data: MenuItem) {
            title.text = data.title
            view.setOnClickListener { pushEvent(ClickEvent(view, data)) }
        }
    }

    // ACTIVITY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listUi = ListUi(
            layoutInflater,
            { inflater, container ->
                MenuListItem(inflater, container)
            },
            recyclerViewConfig = {
                it.addItemDecoration(GridSpacingDecorator(resources.getDimensionPixelSize(R.dimen.menu_padding)))
            }
        )
        assemble(MenuLogicBlock() + listUi)
        setContentView(listUi.view)
    }
}
