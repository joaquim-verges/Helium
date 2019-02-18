package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.presenter.MyListPresenter
import com.joaquimverges.demoapp.view.MyRecyclerItem
import com.joaquimverges.helium.ui.viewdelegate.ListViewDelegate

class SimpleListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDelegate = ListViewDelegate(layoutInflater, { inflater, container ->
            MyRecyclerItem(R.layout.list_item_layout, inflater, container)
        })
        MyListPresenter().attach(viewDelegate)
        setContentView(viewDelegate.view)
    }
}

