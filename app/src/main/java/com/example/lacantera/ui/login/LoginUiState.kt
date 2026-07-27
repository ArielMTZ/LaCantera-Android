package com.example.lacantera.ui.login

import com.example.lacantera.data.model.Usuario

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val usuario: Usuario? = null
)