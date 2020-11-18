package com.joaquimverges.kmp.news.data

import com.joaquimverges.kmp.news.data.models.ArticleResponse
import kotlinx.coroutines.flow.debounce
import kotlin.time.Duration

class NewsRepository(
    private val api: NewsApi = NewsApi(),
    private val sourcesRepository: SourcesRepository = SourcesRepository()
) {
    companion object {
        private const val MAX_PAGES = 10
    }

    private var page = 1

    suspend fun getNews(): ArticleResponse {
        page = 1
        val selectedSources = sourcesRepository.getSelectedSourceIds()
        return api.getNews(page, selectedSources)
    }

    suspend fun paginate(): ArticleResponse {
        page++
        if (page > MAX_PAGES) {
            return ArticleResponse(null, emptyList())
        }
        val selectedSources = sourcesRepository.getSelectedSourceIds()
        return api.getNews(page, selectedSources)
    }

    fun observeSources() = sourcesRepository.observeSelectedSources()
}
