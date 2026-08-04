package com.example.lacantera.data.model

import com.google.gson.annotations.SerializedName

data class CreateUserRequest(
    @SerializedName("tipo_usuario")
    val tipoUsuario: String,

    val username: String? = null,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("segundo_apellido")
    val segundoApellido: String? = null,

    val email: String,
    val rol: String,
    val telefono: String? = null,

    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String,

    val nacionalidad: String? = null,
    val sexo: String,

    @SerializedName("estado_nacimiento")
    val estadoNacimiento: String? = null,

    val password: String? = null,

    val certificacion: String? = null,

    @SerializedName("anios_experiencia")
    val aniosExperiencia: Int? = null,

    @SerializedName("numero_camiseta")
    val numeroCamiseta: Int? = null,

    val posicion: String? = null
)

data class CreateUserResponse(
    val message: String,
    val usuario: Usuario
)