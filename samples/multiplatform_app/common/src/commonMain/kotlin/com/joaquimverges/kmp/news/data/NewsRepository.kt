package com.joaquimverges.kmp.news.data

class NewsRepository(
    private val api: NewsApi = NewsApi()
) {
    suspend fun getNews(): ArticleResponse {
        return api.getNews()
    }
}
