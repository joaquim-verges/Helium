package com.jv.news.presenter.state

import com.joaquimverges.helium.core.state.ViewState

/**
 * @author joaquim
 */
sealed class ArticleListState : ViewState {
    object ArticlesLoaded: ArticleListState()
    object MoreSourcesRequested: ArticleListState()
}