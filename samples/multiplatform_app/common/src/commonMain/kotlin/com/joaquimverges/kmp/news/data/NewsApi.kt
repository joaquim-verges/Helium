package com.joaquimverges.kmp.news.data

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.Url
import kotlinx.serialization.json.Json

class NewsApi {
    companion object {
        private const val baseUrl = "https://newsapi.org/v2/"
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

    private var articlesEndpoint = Url("$baseUrl/everything?language=en&pageSize=50")

    suspend fun getNews(): ArticleResponse {
        return client.get("$articlesEndpoint&sources=engadget,techcrunch,polygon,the-verge") {
            header("X-Api-Key", API_KEY)
        }
    }
}
