package com.joaquimverges.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joaquimverges.demoapp.logic.MyDetailLogic
import com.joaquimverges.demoapp.ui.MyDetailUi
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock

class DetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val logic = getRetainedLogicBlock<MyDetailLogic>()
        val ui = MyDetailUi(inflater, container)
        assemble(logic + ui)
        return ui.view
    }
}
