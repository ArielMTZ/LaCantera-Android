package com.example.lacantera.data.remote

import com.example.lacantera.data.model.DashboardResponse
import com.example.lacantera.data.model.LoginRequest
import com.example.lacantera.data.model.LoginResponse
import com.example.lacantera.data.model.ProfileResponse
import com.example.lacantera.data.model.TokenRefreshRequest
import com.example.lacantera.data.model.TokenRefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/login/")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/token/refresh/")
    suspend fun refreshToken(
        @Body request: TokenRefreshRequest
    ): Response<TokenRefreshResponse>

    @GET("api/dashboard/")
    suspend fun getDashboard(): Response<DashboardResponse>
    @GET("api/profile/")
    suspend fun getProfile(): Response<ProfileResponse>
}