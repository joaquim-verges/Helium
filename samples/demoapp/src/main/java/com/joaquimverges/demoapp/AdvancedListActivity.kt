package com.joaquimverges.demoapp

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquimverges.demoapp.logic.MyListLogic
import com.joaquimverges.demoapp.ui.GridSpacingDecorator
import com.joaquimverges.demoapp.ui.MyCardListItem
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.toolbar.CollapsingToolbarUi
import com.joaquimverges.helium.ui.list.ListUi

class AdvancedListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orientation = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> LinearLayoutManager.HORIZONTAL
            else -> LinearLayoutManager.VERTICAL
        }

        val layoutManager = GridLayoutManager(this, 2, orientation, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    position % 5 == 0 -> 2
                    else -> 1
                }
            }
        }

        val logic = getRetainedLogicBlock<MyListLogic>()
        val listUi = ListUi(layoutInflater,
            recyclerItemFactory = { inflater, container ->
                MyCardListItem(inflater, container)
            },
            layoutManager = layoutManager,
            recyclerViewConfig = {
                val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                it.addItemDecoration(GridSpacingDecorator(padding, orientation))
            })

        assemble(logic + listUi)
        setContentView(
            CollapsingToolbarUi<BlockState, BlockEvent>(
                layoutInflater,
                listUi,
                collapsingLayoutCustomization = {
                    val visibility = when (resources.configuration.orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> View.GONE
                        else -> View.VISIBLE
                    }
                    it.visibility = visibility
                }
            ).view
        )
    }
}
