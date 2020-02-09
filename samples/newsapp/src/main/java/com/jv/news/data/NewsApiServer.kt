package com.jv.news.data

import com.jv.news.App
import com.jv.news.R
import com.jv.news.data.model.ArticleResponse
import com.jv.news.data.model.SourcesResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author joaquim
 */
object NewsApiServer {

    private val API_KEY = App.context.resources.getString(R.string.api_key)
    private const val BASE_URL = "https://newsapi.org/v2/"
    private const val ENDPOINT_ARTICLES = "everything?language=en&pageSize=20"
    private const val ENDPOINT_SOURCES = "sources?language=en"

    val service: NewsApiService

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor {
                val original = it.request()
                it.proceed(
                    original
                        .newBuilder()
                        .header("X-Api-Key", API_KEY)
                        .method(original.method(), original.body())
                        .build()
                )
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(NewsApiService::class.java)
    }

    interface NewsApiService {
        @GET(ENDPOINT_ARTICLES)
        fun getArticles(@Query("sources") source: String, @Query("page") page: Int): Single<ArticleResponse>

        @GET(ENDPOINT_SOURCES)
        fun getSources(): Single<SourcesResponse>
    }
}
