package com.joaquimverges.kmp.news

class NewsRepository(
        private val api: NewsApi = NewsApi()
) {
    suspend fun getNews(): String {
        return api.getNews()
    }
}