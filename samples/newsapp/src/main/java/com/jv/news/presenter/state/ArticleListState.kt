package com.jv.news.presenter.state

import com.joaquimverges.helium.core.state.BlockState

/**
 * @author joaquim
 */
sealed class ArticleListState : BlockState {
    object ArticlesLoaded : ArticleListState()
    object MoreSourcesRequested : ArticleListState()
}