package com.joaquimverges.demoapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.demoapp.presenter.MyDetailPresenter
import com.joaquimverges.demoapp.view.MyDetailViewDelegate
import com.joaquimverges.helium.core.retained.getRetainedPresenter

class DetailFragment : Fragment() {

    private lateinit var presenter: MyDetailPresenter
    private lateinit var viewDelegate: MyDetailViewDelegate

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = getRetainedPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDelegate = MyDetailViewDelegate(inflater, container)
        return viewDelegate.view
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(viewDelegate)
    }
}