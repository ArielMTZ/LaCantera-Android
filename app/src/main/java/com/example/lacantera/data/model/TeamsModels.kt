package com.example.lacantera.data.model

data class TeamsResponse(
    val count: Int,
    val equipos: List<TeamItem>
)

data class TeamItem(
    val id: Int,
    val nombre: String,
    val deporte: String,
    val categoria: String,
    val activo: Boolean
)


data class TeamDetailResponse(
    val equipo: TeamItem
)

data class UpdateTeamRequest(
    val nombre: String? = null,
    val activo: Boolean? = null,
    val deporte_id: Int? = null,
    val categoria_id: Int? = null
)

data class TeamUpdateResponse(
    val detail: String,
    val equipo: TeamItem
)