package com.example.lacantera.data.model

data class TokenRefreshRequest(
    val refresh: String
)

data class TokenRefreshResponse(
    val access: String,
    val refresh: String? = null
)
