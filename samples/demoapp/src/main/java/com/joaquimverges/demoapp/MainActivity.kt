package com.joaquimverges.demoapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.joaquimverges.helium.event.ClickEvent
import com.joaquimverges.helium.presenter.DataListPresenter
import com.joaquimverges.helium.repository.BaseRepository
import com.joaquimverges.helium.viewdelegate.BaseRecyclerViewItem
import com.joaquimverges.helium.viewdelegate.DataListViewDelegate
import io.reactivex.Observable
import io.reactivex.Single

class MainActivity : AppCompatActivity() {

    // DATA

    enum class MenuItem(val title: String) {
        LIST_NOT_RETAINED("List (not retained)"),
        LIST_RETAINED("List (retained)"),
        ADVANCED_LIST("Advanced List"),
        VIEW_PAGER("ViewPager")
    }

    class MenuRepository : BaseRepository<List<MenuItem>> {
        private fun getMenuItems() = MenuItem.values().toList()
        override fun getData(): Single<List<MenuItem>> = Observable.fromIterable(getMenuItems()).toList()
    }

    // PRESENTER

    class MenuPresenter : DataListPresenter<MenuItem, ClickEvent<MenuItem>>(MenuRepository()) {

        override fun onViewEvent(event: ClickEvent<MenuItem>) {
            val context = event.view.context
            val activityClass = when (event.data) {
                MainActivity.MenuItem.LIST_NOT_RETAINED -> SimpleListActivity::class.java
                MainActivity.MenuItem.LIST_RETAINED -> SimpleListActivityRetained::class.java
                MainActivity.MenuItem.ADVANCED_LIST -> AdvancedListActivity::class.java
                MainActivity.MenuItem.VIEW_PAGER -> ViewPagerActivity::class.java
            }
            context.startActivity(Intent(context, activityClass))
        }
    }

    // VIEW

    class MenuRecyclerItem(inflater: LayoutInflater,
                           parent: ViewGroup,
                           root: View = inflater.inflate(R.layout.menu_item_layout, parent, false))
        : BaseRecyclerViewItem<MenuItem, ClickEvent<MenuItem>>(root) {

        private val title = root.findViewById<TextView>(R.id.menu_title)

        override fun bind(data: MenuItem) {
            title.text = data.title
            view.setOnClickListener { pushEvent(ClickEvent(view, data)) }
        }
    }

    // ACTIVITY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewDelegate = DataListViewDelegate(layoutInflater, { inflater, container ->
            MenuRecyclerItem(inflater, container)
        })
        MenuPresenter().attach(viewDelegate)
        setContentView(viewDelegate.view)
    }
}
