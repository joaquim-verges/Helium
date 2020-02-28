package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.logic.MyListLogic
import com.joaquimverges.demoapp.ui.GridSpacingDecorator
import com.joaquimverges.demoapp.ui.MyContentCardListItem
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.toolbar.CollapsingToolbarScreenUi
import com.joaquimverges.helium.ui.list.ListUi

class CardListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listUi = ListUi(
            layoutInflater,
            recyclerItemFactory = { inflater, container ->
                MyContentCardListItem(inflater, container)
            },
            recyclerViewConfig = {
                val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing_double)
                it.addItemDecoration(GridSpacingDecorator(padding))
            })

        getRetainedLogicBlock<MyListLogic>().attach(listUi)
        setContentView(
            CollapsingToolbarScreenUi<BlockState, BlockEvent>(
                layoutInflater,
                listUi
            ).view
        )
    }
}
