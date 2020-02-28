package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.presenter.MyListLogic
import com.joaquimverges.demoapp.view.MyRecyclerItem
import com.joaquimverges.helium.ui.list.ListUi

class SimpleListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDelegate = ListUi(layoutInflater, { inflater, container ->
            MyRecyclerItem(inflater, container)
        })
        MyListLogic().attach(viewDelegate)
        setContentView(viewDelegate.view)
    }
}

