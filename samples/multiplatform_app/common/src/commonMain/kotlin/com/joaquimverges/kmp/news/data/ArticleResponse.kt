package com.joaquimverges.kmp.news.data

import kotlinx.serialization.Serializable

/**
 * @author jverges
 */
@Serializable
data class ArticleResponse(
    var status: String? = null,
    var articles: List<Article> = mutableListOf()
)
