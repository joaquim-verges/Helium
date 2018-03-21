package com.jv.news.view

import android.app.Activity
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.joaquimverges.helium.event.ViewEvent
import com.joaquimverges.helium.state.ViewState
import com.joaquimverges.helium.viewdelegate.BaseViewDelegate
import com.jv.news.App
import com.jv.news.R
import com.jv.news.util.VersionUtil

/**
 * @author joaquim
 */
class DrawerViewDelegate(inflater: LayoutInflater) : BaseViewDelegate<ViewState, ViewEvent>(R.layout.activity_main, inflater) {

    private val drawerLayout: DrawerLayout = view.findViewById(R.id.drawer_layout)

    val mainViewDelegate = ArticleListViewDelegate.create(inflater, view.findViewById(R.id.main_container), {
        drawerLayout.openDrawer(Gravity.START)
    })
    val drawerViewDelegate = SourcesViewDelegate.create(inflater, view.findViewById(R.id.drawer_container))

    init {
        if (VersionUtil.isAtLeastApi(Build.VERSION_CODES.M)) {
            val window = (context as Activity).window
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(App.context, R.color.background_view)
        }
    }

    override fun render(viewState: ViewState) {
        // no-op for now
    }
}