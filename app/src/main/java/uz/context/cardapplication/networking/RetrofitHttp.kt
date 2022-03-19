package uz.context.cardapplication.networking

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHttp {
    private const val BASE_URL = "https://62356fca4d0977f1db3bcaf6.mockapi.io/api/v1/"
    private const val BASE_URL1 = "https://6232aea08364d63035c18563.mockapi.io/api/v1/"

    private val client = getClient()
    private val retrofit = getRetrofit(client)

    private fun getRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.header("x-api-key", "cd115401-446a-4694-b133-c558324ab265")
            chain.proceed(builder.build())
        })
        .build()

    val postService: ApiService = retrofit.create(ApiService::class.java)
}