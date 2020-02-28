package com.joaquimverges.demoapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.demoapp.presenter.MyListLogic
import com.joaquimverges.demoapp.view.MyRecyclerItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.ui.list.ListUi

class ListFragment : Fragment() {

    private lateinit var presenter: MyListLogic
    private lateinit var listUi: ListUi<MyItem, ClickEvent<MyItem>, MyRecyclerItem>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = getRetainedLogicBlock()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        listUi = ListUi(inflater, { layoutInflater, parentContainer ->
            MyRecyclerItem(layoutInflater, parentContainer)
        })
        return listUi.view
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(listUi)
    }
}