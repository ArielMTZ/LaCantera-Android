package com.example.lacantera.data.model


import com.google.gson.annotations.SerializedName

data class MatchdaysResponse(
    val count: Int = 0,
    val matchdays: List<MatchdayItem> = emptyList()
)

data class MatchdayItem(
    val id: Int,
    val numero: Int,

    @SerializedName("display_name")
    val displayName: String,

    val fecha: String,

    @SerializedName("season_category_id")
    val seasonCategoryId: Int,

    @SerializedName("season_id")
    val seasonId: Int,

    @SerializedName("season_name")
    val seasonName: String,

    @SerializedName("category_id")
    val categoryId: Int,

    @SerializedName("category_name")
    val categoryName: String,

    @SerializedName("sport_id")
    val sportId: Int,

    @SerializedName("sport_name")
    val sportName: String
)