package com.example.lacantera.data.model

data class WearSession(
    val userId: Int? = null,
    val username: String = "",
    val nombreCorto: String = "",
    val tipoUsuario: String = "",
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val isLoggedIn: Boolean = false
)