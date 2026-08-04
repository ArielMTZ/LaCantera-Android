package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.RefereeMatchDetailResponse
import com.example.lacantera.data.model.RefereeMatchesResponse
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class RefereeRepository(
    context: Context
) {
    private val apiService =
        RetrofitClient.getApiService(
            context.applicationContext
        )

    suspend fun getPendingMatches():
            Response<RefereeMatchesResponse> {
        return apiService.getRefereeMatches()
    }

    suspend fun getMatchHistory():
            Response<RefereeMatchesResponse> {
        return apiService.getRefereeMatchHistory()
    }

    suspend fun getMatchDetail(
        matchId: Int
    ): Response<RefereeMatchDetailResponse> {
        return apiService.getRefereeMatchDetail(
            matchId = matchId
        )
    }
}