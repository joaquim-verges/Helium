package com.joaquimverges.kmp.news.data

class NewsRepository(
    private val api: NewsApi = NewsApi()
) {
    companion object {
        private const val MAX_PAGES = 10
    }

    private var page = 1

    suspend fun getNews(): ArticleResponse {
        page = 1
        return api.getNews(page)
    }

    suspend fun paginate(): ArticleResponse {
        page++
        if (page > MAX_PAGES) {
            return ArticleResponse(null, emptyList())
        }
        return api.getNews(page)
    }
}
