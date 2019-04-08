package com.joaquimverges.demoapp

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquimverges.demoapp.presenter.MyListPresenter
import com.joaquimverges.demoapp.view.GridSpacingDecorator
import com.joaquimverges.demoapp.view.MyCardRecyclerItem
import com.joaquimverges.helium.core.retained.getRetainedPresenter
import com.joaquimverges.helium.navigation.viewdelegate.CollapsingToolbarScreenViewDelegate
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

        val listViewDelegate = ListViewDelegate(layoutInflater,
            recyclerItemFactory = { inflater, container ->
                MyCardRecyclerItem(inflater, container)
            },
            layoutManager = layoutManager,
            recyclerViewConfig = {
                val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                it.addItemDecoration(GridSpacingDecorator(padding, orientation))
            })

        getRetainedPresenter<MyListPresenter>().attach(listViewDelegate)
        setContentView(
            CollapsingToolbarScreenViewDelegate(
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
