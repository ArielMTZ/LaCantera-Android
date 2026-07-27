package com.example.lacantera.data.remote



import com.example.lacantera.data.model.LoginRequest
import com.example.lacantera.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/login/")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}