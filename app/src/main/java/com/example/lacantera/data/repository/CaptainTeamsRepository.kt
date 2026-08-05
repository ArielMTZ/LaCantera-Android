package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.CaptainTeamsResponse
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class CaptainTeamsRepository(
    context: Context
) {

    private val apiService =
        RetrofitClient.getApiService(
            context.applicationContext
        )

    suspend fun getCaptainTeams(): Response<CaptainTeamsResponse> {
        return apiService.getCaptainTeams()
    }
}