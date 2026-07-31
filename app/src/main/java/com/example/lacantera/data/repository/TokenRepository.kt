package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.TokenRefreshRequest
import com.example.lacantera.data.remote.RefreshTokenClient
import kotlinx.coroutines.flow.first

class TokenRepository(
    context: Context
) {
    private val sessionManager = SessionManager(
        context = context.applicationContext
    )

    suspend fun refreshAccessToken(): String? {
        val refreshToken = sessionManager.refreshToken.first()

        if (refreshToken.isNullOrBlank()) {
            return null
        }

        val response = RefreshTokenClient.api.refreshToken(
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