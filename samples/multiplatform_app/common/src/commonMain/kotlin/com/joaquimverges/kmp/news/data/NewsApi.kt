package com.joaquimverges.kmp.news.data

import com.joaquimverges.kmp.news.data.models.ArticleResponse
import com.joaquimverges.kmp.news.data.models.SourcesResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.Url
import kotlinx.serialization.json.Json

class NewsApi {
    companion object {
        private const val baseUrl = "https://newsapi.org/v2"
    }

    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    private var articlesEndpoint = Url("$baseUrl/everything?language=en&pageSize=10")
    private var sourcesEndpoint = Url("$baseUrl/sources?language=en")

    suspend fun getNews(page: Int, sources: List<String>): ArticleResponse {
        return client.get("$articlesEndpoint&sources=${sources.joinToString(",")}&page=$page") {
            header("X-Api-Key", API_KEY)
        }
    }

    suspend fun getSources(): SourcesResponse {
        return client.get(sourcesEndpoint) {
            header("X-Api-Key", API_KEY)
        }
    }
}
