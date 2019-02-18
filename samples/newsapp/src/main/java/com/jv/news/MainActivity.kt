package com.jv.news

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.joaquimverges.helium.core.retained.getRetainedPresenter
import com.jv.news.presenter.MainPresenter
import com.jv.news.util.VersionUtil
import com.jv.news.view.MainViewDelegate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar()

        val mainPresenter = getRetainedPresenter<MainPresenter>()
        MainViewDelegate(layoutInflater).also {
            setContentView(it.view)
            mainPresenter.attach(it)
        }
    }

    private fun setLightStatusBar() {
        if (VersionUtil.isAtLeastApi(Build.VERSION_CODES.M)) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(App.context, R.color.background_view)
        }
    }
}
