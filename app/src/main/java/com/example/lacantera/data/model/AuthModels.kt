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
    val firstName: String = "",

    @SerializedName("last_name")
    val lastName: String = "",

    @SerializedName("nombre_corto")
    val nombreCorto: String = "",

    val email: String = "",
    val rol: String = "",

    val telefono: String? = null,

    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String? = null,

    val nacionalidad: String? = null,
    val sexo: String? = null,

    @SerializedName("segundo_apellido")
    val segundoApellido: String? = null,

    @SerializedName("estado_nacimiento")
    val estadoNacimiento: String? = null,

    @SerializedName("must_change_password")
    val mustChangePassword: Boolean = false,

    @SerializedName("foto_url")
    val fotoUrl: String? = null,

    @SerializedName("is_staff")
    val isStaff: Boolean = false,

    @SerializedName("is_superuser")
    val isSuperuser: Boolean = false,

    @SerializedName("is_capitan")
    val isCapitan: Boolean = false,

    @SerializedName("tipo_usuario")
    val tipoUsuario: String = "sin_rol",

    @SerializedName("is_active")
    val isActive: Boolean = true,

    val permisos: UserPermissions = UserPermissions()
)