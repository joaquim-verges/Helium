package com.joaquimverges.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaquimverges.demoapp.logic.MyListLogic
import com.joaquimverges.demoapp.ui.GridSpacingDecorator
import com.joaquimverges.demoapp.ui.MyCardListItem
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.ui.list.ListUi

/**
 * @author joaquim
 */
class AdvancedListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    position % 5 == 0 -> 2
                    else -> 1
                }
            }
        }

        val logic = getRetainedLogicBlock<MyListLogic>()
        val listUi = ListUi(
            layoutInflater,
            recyclerItemFactory = { inflater, container ->
                MyCardListItem(inflater, container)
            },
            layoutManager = layoutManager,
            recyclerViewConfig = {
                val padding = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                it.addItemDecoration(GridSpacingDecorator(padding))
            }
        )

        assemble(logic + listUi)
        return listUi.view
    }
}
