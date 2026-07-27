package com.example.lacantera.data.repository



import com.example.lacantera.data.model.LoginRequest
import com.example.lacantera.data.model.LoginResponse
import com.example.lacantera.data.remote.ApiService
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    suspend fun login(
        username: String,
        password: String
    ): Response<LoginResponse> {
        val request = LoginRequest(
            username = username.trim(),
            password = password
        )

        return apiService.login(request)
    }
}