package com.joaquimverges.demoapp

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquimverges.demoapp.presenter.MyListLogic
import com.joaquimverges.demoapp.view.GridSpacingDecorator
import com.joaquimverges.demoapp.view.MyCardListItem
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.toolbar.CollapsingToolbarScreenUi
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

        val listViewDelegate = ListUi(layoutInflater,
            recyclerItemFactory = { inflater, container ->
                MyCardListItem(inflater, container)
            },
            layoutManager = layoutManager,
            recyclerViewConfig = {
                val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                it.addItemDecoration(GridSpacingDecorator(padding, orientation))
            })

        getRetainedLogicBlock<MyListLogic>().attach(listViewDelegate)
        setContentView(
            CollapsingToolbarScreenUi<BlockState, BlockEvent>(
                layoutInflater,
                listViewDelegate,
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
