package com.joaquimverges.demoapp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.demoapp.presenter.MyListPresenter
import com.joaquimverges.demoapp.view.MyRecyclerItem
import com.joaquimverges.helium.retained.RetainedPresenters
import com.joaquimverges.helium.viewdelegate.DataListViewDelegate

class ListFragment : Fragment() {

    private lateinit var presenter: MyListPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = RetainedPresenters.get(this, MyListPresenter::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewDelegate = DataListViewDelegate(inflater, { layoutInflater, parentContainer ->
            MyRecyclerItem(R.layout.list_item_layout, inflater, parentContainer)
        })
        presenter.attach(viewDelegate)
        return viewDelegate.view
    }
}