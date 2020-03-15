package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.logic.MyListLogic
import com.joaquimverges.demoapp.ui.MyListItem
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.ui.list.ListUi

class SimpleListActivityRetained : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logic = getRetainedLogicBlock<MyListLogic>()
        val ui = ListUi(layoutInflater, { inflater, container ->
            MyListItem(inflater, container)
        })
        assemble(logic + ui)
        setContentView(ui.view)
    }
}

