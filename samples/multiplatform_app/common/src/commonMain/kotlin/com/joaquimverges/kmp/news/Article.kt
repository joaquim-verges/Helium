package com.joaquimverges.kmp.news

import kotlinx.serialization.Serializable

/**
 * @author jverges
 */
@Serializable
data class Article(
    var source: ArticleSource? = null,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var content: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null
)
