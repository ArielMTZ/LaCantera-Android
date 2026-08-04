package com.example.lacantera.data.model

import com.google.gson.annotations.SerializedName

data class PaginatedUsersResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Usuario> = emptyList()
)

data class UpdateUserRequest(
    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("last_name")
    val lastName: String? = null,

    @SerializedName("segundo_apellido")
    val segundoApellido: String? = null,

    val email: String? = null,
    val telefono: String? = null,

    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String? = null,

    val nacionalidad: String? = null,
    val sexo: String? = null,

    @SerializedName("estado_nacimiento")
    val estadoNacimiento: String? = null,

    val rol: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null
)

data class UpdateUserResponse(
    val message: String = "",
    val usuario: Usuario
)