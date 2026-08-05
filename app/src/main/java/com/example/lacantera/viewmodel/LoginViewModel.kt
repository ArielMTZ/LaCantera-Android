package com.example.lacantera.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.Usuario
import com.example.lacantera.data.repository.AuthRepository
import com.example.lacantera.data.wear.WearSessionSender
import com.example.lacantera.ui.login.DashboardType
import com.example.lacantera.ui.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AuthRepository(
        context = application.applicationContext
    )

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val wearSessionSender = WearSessionSender(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        LoginUiState()
    )

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun onUsernameChange(
        username: String
    ) {
        _uiState.value = _uiState.value.copy(
            username = username,
            errorMessage = null
        )
    }

    fun onPasswordChange(
        password: String
    ) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }

    fun login() {
        val username =
            _uiState.value.username.trim()

        val password =
            _uiState.value.password

        if (username.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Ingresa tu usuario."
            )
            return
        }

        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Ingresa tu contraseña."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                loginSuccess = false,
                dashboardType = null
            )

            try {
                val response = repository.login(
                    username = username,
                    password = password
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null) {
                        _uiState.value =
                            _uiState.value.copy(
                                isLoading = false,
                                errorMessage = (
                                        "El servidor respondió " +
                                                "sin datos."
                                        )
                            )

                        return@launch
                    }

                    val dashboardType =
                        resolveDashboardType(
                            usuario = body.usuario
                        )

                    if (dashboardType == null) {
                        sessionManager.clearSession()

                        _uiState.value =
                            _uiState.value.copy(
                                isLoading = false,
                                loginSuccess = false,
                                dashboardType = null,
                                usuario = null,
                                errorMessage = (
                                        "Solo administradores, " +
                                                "árbitros y capitanes " +
                                                "pueden iniciar sesión."
                                        )
                            )

                        return@launch
                    }

                    val sessionRole =
                        body.usuario.tipoUsuario
                            .ifBlank {
                                body.usuario.rol
                            }

                    sessionManager.saveSession(
                        accessToken = body.access,
                        refreshToken = body.refresh,
                        userId = body.usuario.id,
                        username =
                            body.usuario.username,
                        nombreCorto =
                            body.usuario.nombreCorto,
                        rol = sessionRole
                    )

                    /*
                     * Envía la sesión al reloj conectado.
                     * No bloquea el login del teléfono.
                     */
                    wearSessionSender.sendSession(
                        usuario = body.usuario,
                        accessToken = body.access,
                        refreshToken = body.refresh
                    )

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            loginSuccess = true,
                            usuario = body.usuario,
                            dashboardType =
                                dashboardType,
                            errorMessage = null
                        )
                } else {
                    val message = when (
                        response.code()
                    ) {
                        400 -> {
                            "Revisa los datos ingresados."
                        }

                        401 -> {
                            "Usuario o contraseña incorrectos."
                        }

                        403 -> {
                            "No tienes permiso para iniciar sesión."
                        }

                        404 -> {
                            "No se encontró el servicio de inicio de sesión."
                        }

                        500 -> {
                            "Ocurrió un error en el servidor."
                        }

                        else -> {
                            "No fue posible iniciar sesión."
                        }
                    }

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            loginSuccess = false,
                            dashboardType = null,
                            errorMessage = message
                        )
                }
            } catch (exception: IOException) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        loginSuccess = false,
                        dashboardType = null,
                        errorMessage = (
                                "Error de conexión: " +
                                        exception.message
                                )
                    )
            } catch (exception: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        loginSuccess = false,
                        dashboardType = null,
                        errorMessage =
                            exception.message
                                ?: (
                                        "Ocurrió un error " +
                                                "inesperado."
                                        )
                    )
            }
        }
    }

    private fun resolveDashboardType(
        usuario: Usuario
    ): DashboardType? {

        val tipoUsuario = usuario.tipoUsuario
            .trim()
            .lowercase()

        val rol = usuario.rol
            .trim()
            .lowercase()

        return when {
            usuario.isSuperuser -> {
                DashboardType.ADMIN
            }

            usuario.isStaff -> {
                DashboardType.ADMIN
            }

            tipoUsuario in ADMIN_ROLES -> {
                DashboardType.ADMIN
            }

            rol in ADMIN_ROLES -> {
                DashboardType.ADMIN
            }

            tipoUsuario == "arbitro" ||
                    rol == "arbitro" -> {
                DashboardType.REFEREE
            }

            usuario.isCapitan ||
                    tipoUsuario == "capitan" -> {
                DashboardType.CAPTAIN
            }

            else -> null
        }
    }

    fun clearLoginSuccess() {
        _uiState.value = _uiState.value.copy(
            loginSuccess = false,
            dashboardType = null
        )
    }

    private companion object {
        val ADMIN_ROLES = setOf(
            "superadmin",
            "staff",
            "admin",
            "admin_principal"
        )
    }
}