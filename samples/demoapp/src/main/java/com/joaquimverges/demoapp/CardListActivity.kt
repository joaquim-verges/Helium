package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.logic.MyListLogic
import com.joaquimverges.demoapp.ui.GridSpacingDecorator
import com.joaquimverges.demoapp.ui.MyContentCardListItem
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.toolbar.CollapsingToolbarUi
import com.joaquimverges.helium.ui.list.ListUi

class CardListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val logic = getRetainedLogicBlock<MyListLogic>()
        val listUi = ListUi(
            layoutInflater,
            recyclerItemFactory = { inflater, container ->
                MyContentCardListItem(inflater, container)
            },
            recyclerViewConfig = {
                val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing_double)
                it.addItemDecoration(GridSpacingDecorator(padding))
            }
        )

        assemble(logic + listUi)
        setContentView(
            CollapsingToolbarUi<BlockState, BlockEvent>(
                layoutInflater,
                listUi
            ).view
        )
    }
}
