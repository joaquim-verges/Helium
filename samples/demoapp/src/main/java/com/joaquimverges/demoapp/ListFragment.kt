package com.joaquimverges.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joaquimverges.demoapp.logic.MyListLogic
import com.joaquimverges.demoapp.ui.MyListItem
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.ui.list.ListUi

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val logic = getRetainedLogicBlock<MyListLogic>()
        val listUi = ListUi(inflater, { layoutInflater, parentContainer ->
            MyListItem(layoutInflater, parentContainer)
        })
        assemble(logic + listUi)
        return listUi.view
    }
}