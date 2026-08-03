package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.CategoryMutationResponse
import com.example.lacantera.data.model.CategoriesResponse
import com.example.lacantera.data.model.CreateCategoryRequest
import com.example.lacantera.data.model.CreatePositionRequest
import com.example.lacantera.data.model.CreateSportRequest
import com.example.lacantera.data.model.DeleteResponse
import com.example.lacantera.data.model.PositionMutationResponse
import com.example.lacantera.data.model.SportMutationResponse
import com.example.lacantera.data.model.SportsResponse
import com.example.lacantera.data.model.UpdateCategoryRequest
import com.example.lacantera.data.model.UpdateSportRequest
import com.example.lacantera.data.remote.RetrofitClient
import retrofit2.Response

class SportsRepository(
    context: Context
) {
    private val apiService =
        RetrofitClient.getApiService(
            context.applicationContext
        )

    suspend fun getSports(): Response<SportsResponse> {
        return apiService.getSports()
    }

    suspend fun getCategories(
        sportId: Int? = null
    ): Response<CategoriesResponse> {
        return apiService.getCategories(
            sportId = sportId
        )
    }

    suspend fun createSport(
        request: CreateSportRequest
    ): Response<SportMutationResponse> {
        return apiService.createSport(request)
    }

    suspend fun updateSport(
        sportId: Int,
        request: UpdateSportRequest
    ): Response<SportMutationResponse> {
        return apiService.updateSport(
            sportId = sportId,
            request = request
        )
    }

    suspend fun deleteSport(
        sportId: Int
    ): Response<DeleteResponse> {
        return apiService.deleteSport(sportId)
    }

    suspend fun createCategory(
        request: CreateCategoryRequest
    ): Response<CategoryMutationResponse> {
        return apiService.createCategory(request)
    }

    suspend fun updateCategory(
        categoryId: Int,
        request: UpdateCategoryRequest
    ): Response<CategoryMutationResponse> {
        return apiService.updateCategory(
            categoryId = categoryId,
            request = request
        )
    }

    suspend fun deleteCategory(
        categoryId: Int
    ): Response<DeleteResponse> {
        return apiService.deleteCategory(categoryId)
    }

    suspend fun createPosition(
        sportId: Int,
        request: CreatePositionRequest
    ): Response<PositionMutationResponse> {
        return apiService.createPosition(
            sportId = sportId,
            request = request
        )
    }

    suspend fun deletePosition(
        positionId: Int
    ): Response<DeleteResponse> {
        return apiService.deletePosition(positionId)
    }
}