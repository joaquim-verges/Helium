package com.jv.news.data.model

import java.util.*

/**
 * @author jverges
 */
data class Article (
    var source: ArticleSource? = null,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: Date? = null
)
