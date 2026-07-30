package com.example.lacantera.ui.login

import com.example.lacantera.data.model.Usuario

enum class DashboardType {
    ADMIN,
    REFEREE,
    CAPTAIN
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val usuario: Usuario? = null,
    val dashboardType: DashboardType? = null
)