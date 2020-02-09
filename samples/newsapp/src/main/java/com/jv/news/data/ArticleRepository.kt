package com.jv.news.data

import com.joaquimverges.helium.ui.repository.BaseRepository
import com.jv.news.data.model.Article
import com.jv.news.data.model.ArticleResponse
import io.reactivex.Maybe
import io.reactivex.Single
import java.net.URL

/**
 * @author joaquim
 */
class ArticleRepository(
    private val sourcesRepository: SourcesRepository = SourcesRepository(),
    private val api: NewsApiServer.NewsApiService = NewsApiServer.service
) : BaseRepository<List<Article>> {

    private var page = 1

    override fun getData(): Single<List<Article>> {
        page = 1
        return fetch()
    }

    override fun paginate(): Maybe<List<Article>> {
        page++
        if (page >= 6) {
            return Maybe.empty()
        }
        return fetch().toMaybe()
    }

    private fun fetch(): Single<List<Article>> {
        val ids = sourcesRepository.getSelectedSourceIds()
        if (ids.isEmpty()) {
            return Single.just(listOf())
        }
        return api
            .getArticles(ids.joinToString(separator = ","), page = page)
            .map { response: ArticleResponse ->
                response.articles
                    .filter { it.url != null && it.urlToImage != null }
                    .distinctBy { it.title }
                    .distinctBy { URL(it.url).path }
            }
    }

    fun sourcesUpdatedObserver() = sourcesRepository.observer()
}
