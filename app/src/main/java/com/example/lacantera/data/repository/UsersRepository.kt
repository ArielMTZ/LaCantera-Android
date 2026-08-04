package com.example.lacantera.data.repository

import android.content.Context
import com.example.lacantera.data.model.PaginatedUsersResponse
import com.example.lacantera.data.model.UpdateUserRequest
import com.example.lacantera.data.model.UpdateUserResponse
import com.example.lacantera.data.model.Usuario
import com.example.lacantera.data.remote.RetrofitClient
import com.example.lacantera.data.model.CreateUserRequest
import com.example.lacantera.data.model.CreateUserResponse
import retrofit2.Response

class UsersRepository(
    context: Context
) {
    private val apiService =
        RetrofitClient.getApiService(
            context.applicationContext
        )

    suspend fun getUsers(
        page: Int = 1,
        search: String? = null,
        rol: String? = null,
        estado: String? = null
    ): Response<PaginatedUsersResponse> {
        return apiService.getUsers(
            page = page,
            search = search,
            rol = rol,
            estado = estado
        )
    }

    suspend fun getUserDetail(
        userId: Int
    ): Response<Usuario> {
        return apiService.getUserDetail(
            userId = userId
        )
    }

    suspend fun updateUser(
        userId: Int,
        request: UpdateUserRequest
    ): Response<UpdateUserResponse> {
        return apiService.updateUser(
            userId = userId,
            request = request
        )
    }

    suspend fun createUser(
        request: CreateUserRequest
    ): Response<CreateUserResponse> {
        return apiService.createUser(
            request = request
        )
    }
}