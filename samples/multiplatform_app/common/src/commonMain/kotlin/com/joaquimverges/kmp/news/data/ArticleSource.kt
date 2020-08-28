package com.joaquimverges.kmp.news.data

import kotlinx.serialization.Serializable

/**
 * @author joaqu
 * created on 1/21/2018.
 */
@Serializable
data class ArticleSource(val id: String?, val name: String?, val category: String? = null)