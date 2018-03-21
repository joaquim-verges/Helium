package com.jv.news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.joaquimverges.helium.retained.RetainedPresenters
import com.jv.news.presenter.MainPresenter
import com.jv.news.view.DrawerViewDelegate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val articlesPresenter = RetainedPresenters.get(this, MainPresenter::class.java)
        DrawerViewDelegate(layoutInflater).let {
            articlesPresenter.attach(it)
            setContentView(it.view)
        }
    }
}
