package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.ActiveSeasonsResponse
import com.example.lacantera.data.model.MatchdaysResponse
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class SeasonsRepository(
    context: Context
) {
    private val apiService =
        RetrofitClient.getApiService(context.applicationContext)

    suspend fun getActiveSeasons(): Response<ActiveSeasonsResponse> {
        return apiService.getActiveSeasons()
    }

    suspend fun getMatchdays(): Response<MatchdaysResponse> {
        return apiService.getMatchdays()
    }
}