package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.ProfileResponse
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class ProfileRepository(
    context: Context
) {

    private val apiService =
        RetrofitClient.getApiService(
            context.applicationContext
        )

    suspend fun getProfile(): Response<ProfileResponse> {
        return apiService.getProfile()
    }
}