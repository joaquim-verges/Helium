package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.logic.MyListLogic
import com.joaquimverges.demoapp.ui.MyListItem
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.ui.list.ListUi

class SimpleListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listUi = ListUi(layoutInflater, { inflater, container ->
            MyListItem(inflater, container)
        })
        (MyListLogic() + listUi).assemble()
        setContentView(listUi.view)
    }
}

