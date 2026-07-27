package com.example.lacantera.data.model


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val access: String,
    val refresh: String,
    val usuario: Usuario
)

data class Usuario(
    val id: Int,
    val username: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("nombre_corto")
    val nombreCorto: String,

    val email: String,
    val rol: String,
    val telefono: String?,

    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String?,

    val nacionalidad: String?,
    val sexo: String?,

    @SerializedName("segundo_apellido")
    val segundoApellido: String?,

    @SerializedName("estado_nacimiento")
    val estadoNacimiento: String?,

    @SerializedName("must_change_password")
    val mustChangePassword: Boolean,

    @SerializedName("foto_url")
    val fotoUrl: String?
)