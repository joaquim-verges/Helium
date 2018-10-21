package com.jv.news.view

import android.view.LayoutInflater
import com.joaquimverges.helium.navigation.viewdelegate.NavDrawerViewDelegate

/**
 * @author joaquim
 */
class MainViewDelegate(inflater: LayoutInflater,
                       val mainView: ArticleListViewDelegate = ArticleListViewDelegate(inflater),
                       val drawerView: SourcesViewDelegate = SourcesViewDelegate(inflater)) : NavDrawerViewDelegate(mainView, drawerView) {

}