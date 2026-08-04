package com.example.lacantera.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://192.168.0.84:8000/"

    @Volatile
    private var apiServiceInstance: ApiService? = null

    fun getApiService(context: Context): ApiService {
        return apiServiceInstance ?: synchronized(this) {
            apiServiceInstance ?: createApiService(context).also {
                apiServiceInstance = it
            }
        }
    }

    private fun createApiService(context: Context): ApiService {
        val appContext = context.applicationContext

        val okHttpClient = OkHttpClient.Builder()
            .proxy(Proxy.NO_PROXY)
            .addInterceptor(
                AuthInterceptor(
                    context = appContext
                )
            )
            .authenticator(
                TokenAuthenticator(
                    context = appContext
                )
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}