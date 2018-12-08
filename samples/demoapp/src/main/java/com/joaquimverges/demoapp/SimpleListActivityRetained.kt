package com.joaquimverges.demoapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.joaquimverges.demoapp.presenter.MyListPresenter
import com.joaquimverges.demoapp.view.MyRecyclerItem
import com.joaquimverges.helium.core.retained.getRetainedPresenter
import com.joaquimverges.helium.ui.viewdelegate.ListViewDelegate

class SimpleListActivityRetained : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDelegate = ListViewDelegate(layoutInflater, { inflater, container ->
            MyRecyclerItem(R.layout.list_item_layout, inflater, container)
        })

        getRetainedPresenter<MyListPresenter>().attach(viewDelegate)
        setContentView(viewDelegate.view)
    }
}

