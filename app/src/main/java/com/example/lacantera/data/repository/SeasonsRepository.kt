package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.ActiveSeasonsResponse
import com.example.lacantera.data.model.CreateSeasonRequest
import com.example.lacantera.data.model.FinalizeSeasonRequest
import com.example.lacantera.data.model.SeasonMutationResponse
import com.example.lacantera.data.model.SeasonsResponse
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class SeasonsRepository(
    context: Context
) {
    private val apiService =
        RetrofitClient.getApiService(
            context.applicationContext
        )

    suspend fun getActiveSeasons():
            Response<ActiveSeasonsResponse> {
        return apiService.getActiveSeasons()
    }

    suspend fun getSeasons():
            Response<SeasonsResponse> {
        return apiService.getSeasons()
    }

    suspend fun createSeason(
        request: CreateSeasonRequest
    ): Response<SeasonMutationResponse> {
        return apiService.createSeason(
            request = request
        )
    }

    suspend fun finalizeSeason(
        seasonId: Int
    ): Response<SeasonMutationResponse> {
        return apiService.finalizeSeason(
            seasonId = seasonId,
            request = FinalizeSeasonRequest()
        )
    }
}