package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.TeamDetailResponse
import com.example.lacantera.data.model.TeamUpdateResponse
import com.example.lacantera.data.model.TeamsResponse
import com.example.lacantera.data.model.UpdateTeamRequest
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class TeamsRepository(
    context: Context
) {

    private val apiService =
        RetrofitClient.getApiService(
            context.applicationContext
        )

    suspend fun getTeams(): Response<TeamsResponse> {
        return apiService.getTeams()
    }

    suspend fun getTeamDetail(
        teamId: Int
    ): Response<TeamDetailResponse> {
        return apiService.getTeamDetail(
            teamId = teamId
        )
    }

    suspend fun updateTeam(
        teamId: Int,
        request: UpdateTeamRequest
    ): Response<TeamUpdateResponse> {
        return apiService.updateTeam(
            teamId = teamId,
            request = request
        )
    }
}