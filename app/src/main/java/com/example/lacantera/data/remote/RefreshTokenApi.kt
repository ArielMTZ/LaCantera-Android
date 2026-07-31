package com.example.lacantera.data.remote

import com.example.lacantera.data.model.TokenRefreshRequest
import com.example.lacantera.data.model.TokenRefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {

    @POST("api/token/refresh/")
    suspend fun refreshToken(
        @Body request: TokenRefreshRequest
    ): Response<TokenRefreshResponse>
}