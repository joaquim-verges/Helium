package com.jv.news.data

import com.joaquimverges.helium.ui.list.repository.ListRepository
import com.jv.news.data.model.Article
import java.net.URL

/**
 * @author joaquim
 */
class ArticleRepository(
    private val sourcesRepository: SourcesRepository = SourcesRepository(),
    private val api: NewsApiServer.NewsApiService = NewsApiServer.service
) : ListRepository<List<Article>> {

    private var page = 1

    override suspend fun getFirstPage(): List<Article> {
        page = 1
        return fetch()
    }

    override suspend fun paginate(): List<Article>? {
        page++
        if (page >= 6) {
            return null
        }
        return fetch()
    }

    private suspend fun fetch(): List<Article> {
        val ids = sourcesRepository.getSelectedSourceIds()
        if (ids.isEmpty()) {
            return listOf()
        }
        return api.getArticles(ids.joinToString(separator = ","), page = page).articles
            .filter { it.url != null && it.urlToImage != null }
            .distinctBy { it.title }
            .distinctBy { URL(it.url).path }
    }

    fun sourcesUpdatedObserver() = sourcesRepository.observer()
}
