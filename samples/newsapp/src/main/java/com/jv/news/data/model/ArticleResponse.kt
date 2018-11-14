package com.jv.news.data.model

/**
 * @author jverges
 */
data class ArticleResponse(
    var status: String? = null,
    var articles: List<Article> = mutableListOf()
)
