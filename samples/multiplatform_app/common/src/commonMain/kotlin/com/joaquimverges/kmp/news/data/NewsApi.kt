package com.joaquimverges.kmp.news.data

import io.ktor.client.HttpClient
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.Url

class NewsApi {
    companion object {
        private const val baseUrl = "https://newsapi.org/v2/"
    }

    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var articlesEndpoint = Url("$baseUrl/everything?language=en&pageSize=20")

    suspend fun getNews(): ArticleResponse {
        return client.get("$articlesEndpoint&sources=engadget") {
            header("X-Api-Key", API_KEY)
        }
    }
}

