package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.DashboardResponse
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class DashboardRepository(
    context: Context
) {
    private val apiService =
        RetrofitClient.getApiService(context.applicationContext)

    suspend fun getDashboard(): Response<DashboardResponse> {
        return apiService.getDashboard()
    }
}