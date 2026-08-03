package com.example.lacantera.data.model

import com.google.gson.annotations.SerializedName

data class SportsResponse(
    val sports: List<SportItem> = emptyList()
)

data class SportItem(
    val id: Int,
    val nombre: String,
    val categories: List<CategoryItem> = emptyList(),
    val positions: List<PositionItem> = emptyList()
)

data class CategoriesResponse(
    val categories: List<CategoryItem> = emptyList()
)

data class CategoryItem(
    val id: Int,
    val nombre: String,
    val sexo: String,

    @SerializedName("sex_display")
    val sexDisplay: String,

    @SerializedName("edad_minima")
    val edadMinima: Int?,

    @SerializedName("edad_maxima")
    val edadMaxima: Int?,

    @SerializedName("sport_id")
    val sportId: Int,

    @SerializedName("sport_name")
    val sportName: String
)

data class PositionItem(
    val id: Int,
    val nombre: String,

    @SerializedName("sport_name")
    val sportName: String? = null
)

// Requests

data class CreateSportRequest(
    val nombre: String
)

data class UpdateSportRequest(
    val nombre: String
)

data class CreateCategoryRequest(
    val nombre: String,
    val sexo: String,

    @SerializedName("edad_minima")
    val edadMinima: Int?,

    @SerializedName("edad_maxima")
    val edadMaxima: Int?,

    @SerializedName("sport_id")
    val sportId: Int
)

data class UpdateCategoryRequest(
    val nombre: String,
    val sexo: String,

    @SerializedName("edad_minima")
    val edadMinima: Int?,

    @SerializedName("edad_maxima")
    val edadMaxima: Int?
)

data class CreatePositionRequest(
    val nombre: String
)

// Responses

data class SportMutationResponse(
    val detail: String,
    val sport: SportItem
)

data class CategoryMutationResponse(
    val detail: String,
    val category: CategoryItem
)

data class PositionMutationResponse(
    val detail: String,
    val position: PositionItem
)

data class DeleteResponse(
    val detail: String
)