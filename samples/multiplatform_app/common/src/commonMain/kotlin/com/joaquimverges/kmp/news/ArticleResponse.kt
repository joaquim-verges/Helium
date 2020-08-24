package com.joaquimverges.kmp.news

import kotlinx.serialization.Serializable

/**
 * @author jverges
 */
@Serializable
data class ArticleResponse(
    var status: String? = null,
    var articles: List<Article> = mutableListOf()
)
