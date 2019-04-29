package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.demoapp.presenter.MyListPresenter
import com.joaquimverges.demoapp.view.GridSpacingDecorator
import com.joaquimverges.demoapp.view.MyContentCardRecyclerItem
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.retained.getRetainedPresenter
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.navigation.viewdelegate.CollapsingToolbarScreenViewDelegate
import com.joaquimverges.helium.ui.viewdelegate.ListViewDelegate

class CardListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listViewDelegate = ListViewDelegate(
            layoutInflater,
            recyclerItemFactory = { inflater, container ->
                MyContentCardRecyclerItem(inflater, container)
            },
            recyclerViewConfig = {
                val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing_double)
                it.addItemDecoration(GridSpacingDecorator(padding))
            })

        getRetainedPresenter<MyListPresenter>().attach(listViewDelegate)
        setContentView(
            CollapsingToolbarScreenViewDelegate<ViewState, ViewEvent>(
                layoutInflater,
                listViewDelegate
            ).view
        )
    }
}
