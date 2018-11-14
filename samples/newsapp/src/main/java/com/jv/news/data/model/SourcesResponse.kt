package com.jv.news.data.model

/**
 * @author joaquim
 */
data class SourcesResponse(
    var status: String? = null,
    var sources: List<ArticleSource> = mutableListOf()
)