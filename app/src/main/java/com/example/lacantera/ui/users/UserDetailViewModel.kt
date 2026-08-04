package com.example.lacantera.ui.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.UpdateUserRequest
import com.example.lacantera.data.model.Usuario
import com.example.lacantera.data.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class UserDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val repository = UsersRepository(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        UserDetailUiState()
    )

    val uiState: StateFlow<UserDetailUiState> =
        _uiState.asStateFlow()

    private var currentUserId: Int? = null

    fun loadUser(
        userId: Int
    ) {
        if (
            currentUserId == userId &&
            _uiState.value.usuario != null
        ) {
            return
        }

        currentUserId = userId

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response = repository.getUserDetail(
                    userId = userId
                )

                if (response.isSuccessful) {
                    val user = response.body()

                    if (user == null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage =
                                "El servidor respondió sin usuario."
                        )
                        return@launch
                    }

                    setUser(
                        user = user
                    )
                } else {
                    handleErrorCode(
                        code = response.code()
                    )
                }
            } catch (exception: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage =
                        "Error de conexión: ${exception.message}"
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message
                        ?: "No fue posible cargar el usuario."
                )
            }
        }
    }

    private fun setUser(
        user: Usuario
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            usuario = user,
            firstName = user.firstName,
            lastName = user.lastName,
            segundoApellido =
                user.segundoApellido.orEmpty(),
            email = user.email,
            telefono = user.telefono.orEmpty(),
            fechaNacimiento =
                user.fechaNacimiento.orEmpty(),
            nacionalidad =
                user.nacionalidad.orEmpty(),
            sexo = user.sexo.orEmpty(),
            estadoNacimiento =
                user.estadoNacimiento.orEmpty(),
            rol = user.rol,
            isActive = user.isActive,
            errorMessage = null
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

    fun onRoleChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            rol = value
        )
    }

    fun onActiveChanged(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            isActive = value
        )
    }

    fun updateUser() {
        val userId = currentUserId ?: return
        val state = _uiState.value

        if (state.firstName.isBlank()) {
            _uiState.value = state.copy(
                errorMessage =
                    "El nombre no puede estar vacío."
            )
            return
        }

        if (state.email.isBlank()) {
            _uiState.value = state.copy(
                errorMessage =
                    "El correo no puede estar vacío."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                isSaving = true,
                errorMessage = null
            )

            try {
                val request = UpdateUserRequest(
                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim(),
                    segundoApellido =
                        state.segundoApellido
                            .trim()
                            .takeIf {
                                it.isNotBlank()
                            },
                    email = state.email.trim(),
                    telefono = state.telefono
                        .trim()
                        .takeIf {
                            it.isNotBlank()
                        },
                    fechaNacimiento =
                        state.fechaNacimiento
                            .trim()
                            .takeIf {
                                it.isNotBlank()
                            },
                    nacionalidad =
                        state.nacionalidad
                            .trim()
                            .takeIf {
                                it.isNotBlank()
                            },
                    sexo = state.sexo
                        .trim()
                        .takeIf {
                            it.isNotBlank()
                        },
                    estadoNacimiento =
                        state.estadoNacimiento
                            .trim()
                            .takeIf {
                                it.isNotBlank()
                            },
                    rol = state.rol,
                    isActive = state.isActive
                )

                val response = repository.updateUser(
                    userId = userId,
                    request = request
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        usuario = body?.usuario
                            ?: _uiState.value.usuario,
                        updateCompleted = true,
                        successMessage =
                            body?.message
                                ?: "Usuario actualizado."
                    )
                } else {
                    val errorBody = response.errorBody()
                        ?.string()

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = errorBody
                            ?: "Error ${response.code()} al actualizar."
                    )
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
                        ?: "No fue posible actualizar el usuario."
                )
            }
        }
    }

    private suspend fun handleErrorCode(
        code: Int
    ) {
        when (code) {
            400 -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaving = false,
                    errorMessage =
                        "Error $code al actualizar. Revisa los datos enviados."
                )
            }

            401 -> {
                sessionManager.clearSession()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaving = false,
                    sessionExpired = true,
                    errorMessage = "La sesión expiró."
                )
            }

            403 -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaving = false,
                    errorMessage =
                        "No tienes permiso para modificar usuarios."
                )
            }

            404 -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaving = false,
                    errorMessage =
                        "El usuario ya no existe."
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaving = false,
                    errorMessage =
                        "Error $code al procesar la solicitud."
                )
            }
        }
    }

    fun consumeSessionExpired() {
        _uiState.value = _uiState.value.copy(
            sessionExpired = false
        )
    }
}

data class UserDetailUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val usuario: Usuario? = null,

    val firstName: String = "",
    val lastName: String = "",
    val segundoApellido: String = "",
    val email: String = "",
    val telefono: String = "",
    val fechaNacimiento: String = "",
    val nacionalidad: String = "",
    val sexo: String = "",
    val estadoNacimiento: String = "",
    val rol: String = "",
    val isActive: Boolean = true,

    val errorMessage: String? = null,
    val successMessage: String? = null,
    val updateCompleted: Boolean = false,
    val sessionExpired: Boolean = false
)