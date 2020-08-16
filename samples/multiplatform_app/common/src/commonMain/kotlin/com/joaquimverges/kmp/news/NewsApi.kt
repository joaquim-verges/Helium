package com.joaquimverges.kmp.news

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.Url

class NewsApi {
    companion object {
        private const val baseUrl = "https://samples.openweathermap.org"
    }

    private val client = HttpClient()

    private var address = Url("$baseUrl/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22")

    suspend fun getNews(): String {
        return client.get(address.toString())
    }
}