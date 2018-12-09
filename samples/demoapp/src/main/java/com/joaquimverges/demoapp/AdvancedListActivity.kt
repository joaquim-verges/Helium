package com.joaquimverges.demoapp

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.joaquimverges.demoapp.presenter.MyListPresenter
import com.joaquimverges.demoapp.view.GridSpacingDecorator
import com.joaquimverges.demoapp.view.MyRecyclerItem
import com.joaquimverges.helium.core.retained.getRetainedPresenter
import com.joaquimverges.helium.ui.viewdelegate.ListViewDelegate

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

        val viewDelegate = ListViewDelegate(layoutInflater,
            recyclerItemFactory = { inflater, container ->
                MyRecyclerItem(R.layout.grid_item_layout, inflater, container)
            },
            layoutManager = layoutManager,
            recyclerViewConfig = {
                val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                it.addItemDecoration(GridSpacingDecorator(padding, orientation))
            })


        getRetainedPresenter<MyListPresenter>().attach(viewDelegate)
        setContentView(viewDelegate.view)
    }
}
