package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.TokenRefreshRequest
import com.example.lacantera.data.remote.RetrofitClient
import kotlinx.coroutines.flow.first

class TokenRepository(
    context: Context
) {

    private val appContext = context.applicationContext

    private val sessionManager = SessionManager(
        context = appContext
    )

    private val apiService =
        RetrofitClient.getApiService(appContext)

    suspend fun refreshAccessToken(): String? {
        val refreshToken =
            sessionManager.refreshToken.first()

        if (refreshToken.isNullOrBlank()) {
            return null
        }

        val response = apiService.refreshToken(
            request = TokenRefreshRequest(
                refresh = refreshToken
            )
        )

        if (!response.isSuccessful) {
            return null
        }

        val body = response.body() ?: return null

        sessionManager.updateAccessToken(
            accessToken = body.access
        )

        return body.access
    }
}