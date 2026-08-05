package com.example.lacantera.data.model

import com.google.gson.annotations.SerializedName

data class CaptainTeamsResponse(
    val count: Int = 0,
    val equipos: List<CaptainTeamItem> = emptyList()
)

data class CaptainTeamItem(
    val id: Int,
    val nombre: String,
    val deporte: String,
    val categoria: String,
    val activo: Boolean,

    @SerializedName("logo_url")
    val logoUrl: String? = null,

    @SerializedName("total_integrantes")
    val totalIntegrantes: Int = 0,

    val capitan: CaptainInfo? = null,

    val jugadores: List<CaptainTeamPlayer> = emptyList()
)

data class CaptainInfo(
    @SerializedName("jugador_id")
    val jugadorId: Int,

    @SerializedName("usuario_id")
    val usuarioId: Int,

    val username: String = "",
    val nombre: String = "",

    @SerializedName("foto_url")
    val fotoUrl: String? = null
)

data class CaptainTeamPlayer(
    val id: Int,

    @SerializedName("jugador_id")
    val jugadorId: Int,

    @SerializedName("usuario_id")
    val usuarioId: Int,

    val username: String = "",
    val nombre: String = "",

    @SerializedName("foto_url")
    val fotoUrl: String? = null,

    @SerializedName("numero_camiseta")
    val numeroCamiseta: String? = null,

    val posicion: String? = null,
    val activo: Boolean = true,

    @SerializedName("es_capitan")
    val esCapitan: Boolean = false
)