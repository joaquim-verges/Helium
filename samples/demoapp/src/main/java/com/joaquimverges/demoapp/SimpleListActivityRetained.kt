package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.logic.MyListLogic
import com.joaquimverges.demoapp.ui.MyListItem
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.ui.list.ListUi

class SimpleListActivityRetained : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listUi = ListUi(layoutInflater, { inflater, container ->
            MyListItem(inflater, container)
        })
        getRetainedLogicBlock<MyListLogic>().attach(listUi)
        setContentView(listUi.view)
    }
}

