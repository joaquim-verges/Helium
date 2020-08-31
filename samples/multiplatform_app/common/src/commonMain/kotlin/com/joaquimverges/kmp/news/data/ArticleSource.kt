package com.joaquimverges.kmp.news.data

import kotlinx.serialization.Serializable

/**
 * @author joaquim
 */
@Serializable
data class ArticleSource(val id: String?, val name: String?, val category: String? = null)
