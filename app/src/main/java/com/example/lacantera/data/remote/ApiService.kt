package com.example.lacantera.data.remote

import com.example.lacantera.data.model.DashboardResponse
import com.example.lacantera.data.model.LoginRequest
import com.example.lacantera.data.model.LoginResponse
import com.example.lacantera.data.model.ProfileResponse
import com.example.lacantera.data.model.TeamDetailResponse
import com.example.lacantera.data.model.TeamUpdateResponse
import com.example.lacantera.data.model.TeamsResponse
import com.example.lacantera.data.model.TokenRefreshRequest
import com.example.lacantera.data.model.TokenRefreshResponse
import com.example.lacantera.data.model.UpdateTeamRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("api/teams/")
    suspend fun getTeams(): Response<TeamsResponse>

    @GET("api/teams/{teamId}/")
    suspend fun getTeamDetail(
        @Path("teamId") teamId: Int
    ): Response<TeamDetailResponse>

    @PATCH("api/teams/{teamId}/")
    suspend fun updateTeam(
        @Path("teamId") teamId: Int,
        @Body request: UpdateTeamRequest
    ): Response<TeamUpdateResponse>
}