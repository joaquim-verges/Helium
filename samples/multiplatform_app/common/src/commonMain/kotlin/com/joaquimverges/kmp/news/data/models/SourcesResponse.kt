package com.joaquimverges.kmp.news.data.models

import kotlinx.serialization.Serializable

/**
 * @author joaquim
 */
@Serializable
data class SourcesResponse(
    var status: String? = null,
    var sources: List<ArticleSource> = mutableListOf()
)
