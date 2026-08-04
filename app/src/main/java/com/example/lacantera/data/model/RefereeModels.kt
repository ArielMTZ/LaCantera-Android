package com.example.lacantera.data.model

import com.google.gson.annotations.SerializedName

data class RefereeMatchesResponse(
    val count: Int = 0,
    val matches: List<RefereeMatchItem> = emptyList()
)

data class RefereeMatchDetailResponse(
    val match: RefereeMatchItem
)

data class RefereeMatchItem(
    val id: Int,

    @SerializedName("local_team_id")
    val localTeamId: Int,

    @SerializedName("local_team_name")
    val localTeamName: String,

    @SerializedName("visitor_team_id")
    val visitorTeamId: Int,

    @SerializedName("visitor_team_name")
    val visitorTeamName: String,

    val fecha: String,
    val cancha: String,

    @SerializedName("court_display")
    val courtDisplay: String,

    @SerializedName("season_id")
    val seasonId: Int,

    @SerializedName("season_name")
    val seasonName: String,

    @SerializedName("category_id")
    val categoryId: Int,

    @SerializedName("category_name")
    val categoryName: String,

    @SerializedName("jornada_numero")
    val jornadaNumero: Int,

    @SerializedName("puntos_local")
    val puntosLocal: Int,

    @SerializedName("puntos_visitante")
    val puntosVisitante: Int,

    val result: String,
    val finalizado: Boolean,

    @SerializedName("winner_name")
    val winnerName: String?,

    val sets: List<RefereeSetItem> = emptyList(),

    val observation: String = ""
)

data class RefereeSetItem(
    val id: Int,

    @SerializedName("numero_set")
    val numeroSet: Int,

    @SerializedName("puntos_local")
    val puntosLocal: Int,

    @SerializedName("puntos_visitante")
    val puntosVisitante: Int,

    @SerializedName("lado_local")
    val ladoLocal: String,

    @SerializedName("rotacion_mitad_aplicada")
    val rotacionMitadAplicada: Boolean,

    val pausado: Boolean,
    val finalizado: Boolean,
    val ganador: String?
)