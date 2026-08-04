package com.example.lacantera.ui.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.CreateUserRequest
import com.example.lacantera.data.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class CreateUserViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = UsersRepository(
        context = application.applicationContext
    )

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        CreateUserUiState()
    )

    val uiState: StateFlow<CreateUserUiState> =
        _uiState.asStateFlow()

    fun onTipoUsuarioChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            tipoUsuario = value,
            username = if (value == "extranjero") {
                ""
            } else {
                _uiState.value.username
            }
        )
    }

    fun onUsernameChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            username = value.uppercase()
        )
    }

    fun onFirstNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            firstName = value
        )
    }

    fun onLastNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            lastName = value
        )
    }

    fun onSegundoApellidoChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            segundoApellido = value
        )
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value
        )
    }

    fun onRoleChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            rol = value,
            certificacion = "",
            aniosExperiencia = "",
            numeroCamiseta = "",
            posicion = ""
        )
    }

    fun onTelefonoChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            telefono = value
        )
    }

    fun onFechaNacimientoChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            fechaNacimiento = value
        )
    }

    fun onNacionalidadChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            nacionalidad = value
        )
    }

    fun onSexoChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            sexo = value
        )
    }

    fun onEstadoNacimientoChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            estadoNacimiento = value
        )
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value
        )
    }

    fun onCertificacionChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            certificacion = value
        )
    }

    fun onAniosExperienciaChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            aniosExperiencia = value.filter {
                it.isDigit()
            }
        )
    }

    fun onNumeroCamisetaChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            numeroCamiseta = value.filter {
                it.isDigit()
            }
        )
    }

    fun onPosicionChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            posicion = value
        )
    }

    fun createUser() {
        val state = _uiState.value

        val validationError = validateState(state)

        if (validationError != null) {
            _uiState.value = state.copy(
                errorMessage = validationError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                isSaving = true,
                errorMessage = null
            )

            try {
                val request = CreateUserRequest(
                    tipoUsuario = state.tipoUsuario,

                    username = state.username
                        .trim()
                        .takeIf {
                            state.tipoUsuario == "mexicano"
                        },

                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim(),

                    segundoApellido = state.segundoApellido
                        .trim()
                        .takeIf {
                            it.isNotBlank()
                        },

                    email = state.email.trim(),
                    rol = state.rol,

                    telefono = state.telefono
                        .trim()
                        .takeIf {
                            it.isNotBlank()
                        },

                    fechaNacimiento =
                        state.fechaNacimiento.trim(),

                    nacionalidad = state.nacionalidad
                        .trim()
                        .takeIf {
                            it.isNotBlank()
                        },

                    sexo = state.sexo,

                    estadoNacimiento =
                        state.estadoNacimiento
                            .trim()
                            .takeIf {
                                it.isNotBlank()
                            },

                    password = state.password
                        .trim()
                        .takeIf {
                            it.isNotBlank()
                        },

                    certificacion = state.certificacion
                        .trim()
                        .takeIf {
                            state.rol == "arbitro" &&
                                    it.isNotBlank()
                        },

                    aniosExperiencia =
                        state.aniosExperiencia
                            .toIntOrNull()
                            .takeIf {
                                state.rol == "arbitro"
                            },

                    numeroCamiseta =
                        state.numeroCamiseta
                            .toIntOrNull()
                            .takeIf {
                                state.rol == "jugador"
                            },

                    posicion = state.posicion
                        .trim()
                        .takeIf {
                            state.rol == "jugador" &&
                                    it.isNotBlank()
                        }
                )

                val response = repository.createUser(
                    request = request
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        createCompleted = true,
                        successMessage = body?.message
                            ?: "Usuario creado correctamente."
                    )
                } else {
                    val errorBody = response
                        .errorBody()
                        ?.string()

                    when (response.code()) {
                        400 -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    isSaving = false,
                                    errorMessage = errorBody
                                        ?: "Revisa los datos enviados."
                                )
                        }

                        401 -> {
                            sessionManager.clearSession()

                            _uiState.value =
                                _uiState.value.copy(
                                    isSaving = false,
                                    sessionExpired = true,
                                    errorMessage =
                                        "La sesión expiró."
                                )
                        }

                        403 -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    isSaving = false,
                                    errorMessage = errorBody
                                        ?: "No tienes permiso para crear usuarios."
                                )
                        }

                        else -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    isSaving = false,
                                    errorMessage = errorBody
                                        ?: "Error ${response.code()} al crear el usuario."
                                )
                        }
                    }
                }
            } catch (exception: IOException) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage =
                        "Error de conexión: ${exception.message}"
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = exception.message
                        ?: "No fue posible crear el usuario."
                )
            }
        }
    }

    private fun validateState(
        state: CreateUserUiState
    ): String? {
        if (
            state.tipoUsuario == "mexicano" &&
            state.username.isBlank()
        ) {
            return "La CURP es obligatoria."
        }

        if (
            state.tipoUsuario == "mexicano" &&
            state.username.length != 18
        ) {
            return "La CURP debe tener 18 caracteres."
        }

        if (state.firstName.isBlank()) {
            return "El nombre es obligatorio."
        }

        if (state.lastName.isBlank()) {
            return "El primer apellido es obligatorio."
        }

        if (state.email.isBlank()) {
            return "El correo es obligatorio."
        }

        if (!state.email.contains("@")) {
            return "El correo no tiene un formato válido."
        }

        if (state.rol.isBlank()) {
            return "Selecciona un rol."
        }

        if (state.fechaNacimiento.isBlank()) {
            return "La fecha de nacimiento es obligatoria."
        }

        if (
            !state.fechaNacimiento.matches(
                Regex("""\d{4}-\d{2}-\d{2}""")
            )
        ) {
            return "La fecha debe tener formato AAAA-MM-DD."
        }

        if (state.sexo !in listOf("H", "M")) {
            return "Selecciona el sexo."
        }

        return null
    }

    fun consumeSessionExpired() {
        _uiState.value = _uiState.value.copy(
            sessionExpired = false
        )
    }
}

data class CreateUserUiState(
    val isSaving: Boolean = false,

    val tipoUsuario: String = "mexicano",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val segundoApellido: String = "",
    val email: String = "",
    val rol: String = "jugador",
    val telefono: String = "",
    val fechaNacimiento: String = "",
    val nacionalidad: String = "",
    val sexo: String = "",
    val estadoNacimiento: String = "",
    val password: String = "",

    val certificacion: String = "",
    val aniosExperiencia: String = "",

    val numeroCamiseta: String = "",
    val posicion: String = "",

    val errorMessage: String? = null,
    val successMessage: String? = null,
    val createCompleted: Boolean = false,
    val sessionExpired: Boolean = false
)