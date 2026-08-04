package com.example.lacantera.data.model

import com.google.gson.annotations.SerializedName

data class ActiveSeasonsResponse(
    val count: Int = 0,
    val seasons: List<SeasonItem> = emptyList()
)

data class SeasonsResponse(
    val count: Int = 0,
    val seasons: List<SeasonItem> = emptyList()
)

data class SeasonItem(
    val id: Int,
    val nombre: String,

    @SerializedName("sport_id")
    val sportId: Int,

    @SerializedName("sport_name")
    val sportName: String,

    @SerializedName("fecha_inicio")
    val fechaInicio: String,

    @SerializedName("fecha_fin")
    val fechaFin: String,

    val estado: String,

    @SerializedName("status_display")
    val statusDisplay: String,

    @SerializedName("category_count")
    val categoryCount: Int
)

data class CreateSeasonRequest(
    val nombre: String,

    @SerializedName("sport_id")
    val sportId: Int,

    @SerializedName("fecha_inicio")
    val fechaInicio: String,

    @SerializedName("fecha_fin")
    val fechaFin: String
)

data class FinalizeSeasonRequest(
    val estado: String = "finalizada"
)

data class SeasonMutationResponse(
    val message: String,
    val season: SeasonItem
)