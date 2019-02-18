package com.joaquimverges.demoapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.demoapp.presenter.MyListPresenter
import com.joaquimverges.demoapp.view.MyRecyclerItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.retained.getRetainedPresenter
import com.joaquimverges.helium.ui.viewdelegate.ListViewDelegate

class ListFragment : Fragment() {

    private lateinit var presenter: MyListPresenter
    private lateinit var viewDelegate: ListViewDelegate<MyItem, ClickEvent<MyItem>, MyRecyclerItem>

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = getRetainedPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDelegate = ListViewDelegate(inflater, { layoutInflater, parentContainer ->
            MyRecyclerItem(layoutInflater, parentContainer)
        })
        return viewDelegate.view
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(viewDelegate)
    }
}