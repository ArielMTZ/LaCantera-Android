package com.example.lacantera.data.model

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    val usuario: Usuario,
    val estadisticas: DashboardStats
)

data class DashboardStats(
    val equipos: Int = 0,
    val jugadores: Int = 0,
    val arbitros: Int = 0
)