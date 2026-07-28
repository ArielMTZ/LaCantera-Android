package com.example.lacantera.data.remote

import android.content.Context
import com.example.lacantera.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    context: Context
) : Interceptor {

    private val sessionManager = SessionManager(
        context = context.applicationContext
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = runBlocking {
            sessionManager.accessToken.first()
        }

        val authenticatedRequest =
            if (accessToken.isNullOrBlank()) {
                originalRequest
            } else {
                originalRequest.newBuilder()
                    .header(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .build()
            }

        return chain.proceed(authenticatedRequest)
    }
}