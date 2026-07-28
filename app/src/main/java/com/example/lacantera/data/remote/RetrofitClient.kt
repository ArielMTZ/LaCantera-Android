package com.example.lacantera.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit

object RetrofitClient {

    /*
     * Conserva aquí la IP que ya te funcionó en el teléfono físico.
     *
     * Ejemplo:
     * http://192.168.1.50:8000/
     */
    private const val BASE_URL = "http://192.168.1.8:8000/"

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
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(context.applicationContext)
            )
            .proxy(Proxy.NO_PROXY)
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