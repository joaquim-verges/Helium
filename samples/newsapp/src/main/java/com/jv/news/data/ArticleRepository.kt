package com.jv.news.data

import com.joaquimverges.helium.ui.repository.BaseRepository
import com.jv.news.data.model.Article
import com.jv.news.data.model.ArticleResponse
import io.reactivex.Single

/**
 * @author joaquim
 */
class ArticleRepository(
    private val sourcesRepository: SourcesRepository = SourcesRepository(),
    private val api: NewsApiServer.NewsApiService = NewsApiServer.service
) : BaseRepository<List<Article>> {

    override fun getData(): Single<List<Article>> {
        val ids = sourcesRepository.getSelectedSourceIds()
        if (ids.isEmpty()) {
            return Single.just(listOf())
        }
        return api
            .getArticles(ids.joinToString(separator = ","))
            .map { response: ArticleResponse ->
                response.articles
                    .filter { it.url != null && it.urlToImage != null }
                    .distinctBy { it.title }
            }
    }

    fun sourcesUpdatedObserver() = sourcesRepository.observer()
}
