package com.joaquimverges.demoapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.demoapp.logic.MyDetailLogic
import com.joaquimverges.demoapp.ui.MyDetailUi
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock

class DetailFragment : Fragment() {

    private lateinit var logic: MyDetailLogic
    private lateinit var ui: MyDetailUi

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logic = getRetainedLogicBlock()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = MyDetailUi(inflater, container)
        return ui.view
    }

    override fun onStart() {
        super.onStart()
        logic.attach(ui)
    }
}