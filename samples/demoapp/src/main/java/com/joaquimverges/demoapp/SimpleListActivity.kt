package com.joaquimverges.demoapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.joaquimverges.demoapp.presenter.MyListPresenter
import com.joaquimverges.demoapp.view.MyRecyclerItem
import com.joaquimverges.helium.viewdelegate.DataListViewDelegate

class SimpleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDelegate = DataListViewDelegate(layoutInflater, { inflater, container ->
            MyRecyclerItem(R.layout.list_item_layout, inflater, container)
        })
        setContentView(viewDelegate.view)
        MyListPresenter().attach(viewDelegate)
    }
}

