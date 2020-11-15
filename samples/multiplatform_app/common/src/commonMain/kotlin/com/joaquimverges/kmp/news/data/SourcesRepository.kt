package com.joaquimverges.kmp.news.data

import com.joaquimverges.kmp.news.data.models.ArticleSource
import com.joaquimverges.kmp.news.data.models.SourcesResponse

class SourcesRepository(private val api: NewsApi = NewsApi()) {
    suspend fun getSources(): List<ArticleSource> {
        return api.getSources().sources
    }
}