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
import com.example.lacantera.data.model.ActiveSeasonsResponse
import com.example.lacantera.data.model.CategoriesResponse
import com.example.lacantera.data.model.MatchdaysResponse
import com.example.lacantera.data.model.SportsResponse
import com.example.lacantera.data.model.CategoryMutationResponse
import com.example.lacantera.data.model.CreateCategoryRequest
import com.example.lacantera.data.model.CreatePositionRequest
import com.example.lacantera.data.model.CreateSportRequest
import com.example.lacantera.data.model.DeleteResponse
import com.example.lacantera.data.model.PositionMutationResponse
import com.example.lacantera.data.model.SportMutationResponse
import com.example.lacantera.data.model.UpdateCategoryRequest
import com.example.lacantera.data.model.UpdateSportRequest
import com.example.lacantera.data.model.PaginatedUsersResponse
import com.example.lacantera.data.model.UpdateUserRequest
import com.example.lacantera.data.model.UpdateUserResponse
import com.example.lacantera.data.model.Usuario
import com.example.lacantera.data.model.CreateUserRequest
import com.example.lacantera.data.model.CreateUserResponse
import retrofit2.http.DELETE
import retrofit2.http.Query
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


    @GET("api/sports/")
    suspend fun getSports(): Response<SportsResponse>

    @GET("api/categories/")
    suspend fun getCategories(
        @Query("sport_id") sportId: Int? = null
    ): Response<CategoriesResponse>

    @GET("api/seasons/active/")
    suspend fun getActiveSeasons(): Response<ActiveSeasonsResponse>

    @GET("api/matchdays/")
    suspend fun getMatchdays(): Response<MatchdaysResponse>


    @POST("api/sports/")
    suspend fun createSport(
        @Body request: CreateSportRequest
    ): Response<SportMutationResponse>

    @PATCH("api/sports/{sportId}/")
    suspend fun updateSport(
        @Path("sportId") sportId: Int,
        @Body request: UpdateSportRequest
    ): Response<SportMutationResponse>

    @DELETE("api/sports/{sportId}/")
    suspend fun deleteSport(
        @Path("sportId") sportId: Int
    ): Response<DeleteResponse>

    @POST("api/categories/")
    suspend fun createCategory(
        @Body request: CreateCategoryRequest
    ): Response<CategoryMutationResponse>

    @PATCH("api/categories/{categoryId}/")
    suspend fun updateCategory(
        @Path("categoryId") categoryId: Int,
        @Body request: UpdateCategoryRequest
    ): Response<CategoryMutationResponse>

    @DELETE("api/categories/{categoryId}/")
    suspend fun deleteCategory(
        @Path("categoryId") categoryId: Int
    ): Response<DeleteResponse>

    @POST("api/sports/{sportId}/positions/")
    suspend fun createPosition(
        @Path("sportId") sportId: Int,
        @Body request: CreatePositionRequest
    ): Response<PositionMutationResponse>

    @DELETE("api/positions/{positionId}/")
    suspend fun deletePosition(
        @Path("positionId") positionId: Int
    ): Response<DeleteResponse>

    @GET("api/users/")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null,
        @Query("rol") rol: String? = null,
        @Query("estado") estado: String? = null
    ): Response<PaginatedUsersResponse>

    @GET("api/users/{userId}/")
    suspend fun getUserDetail(
        @Path("userId") userId: Int
    ): Response<Usuario>

    @PATCH("api/users/{userId}/")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Body request: UpdateUserRequest
    ): Response<UpdateUserResponse>

    @POST("api/users/")
    suspend fun createUser(
        @Body request: CreateUserRequest
    ): Response<CreateUserResponse>
}