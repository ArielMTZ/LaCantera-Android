package com.example.lacantera.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.UserPermissions
import com.example.lacantera.data.repository.DashboardRepository
import com.example.lacantera.data.wear.WearSessionSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class DashboardViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val dashboardRepository = DashboardRepository(
        context = application.applicationContext
    )

    private val wearSessionSender = WearSessionSender(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        DashboardUiState()
    )

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response =
                    dashboardRepository.getDashboard()

                if (response.isSuccessful) {
                    val dashboard = response.body()

                    if (dashboard == null) {
                        _uiState.value =
                            _uiState.value.copy(
                                isLoading = false,
                                errorMessage =
                                    "El servidor respondió sin datos del dashboard."
                            )

                        return@launch
                    }

                    val profile = dashboard.usuario
                    val stats = dashboard.estadisticas

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,

                            userId = profile.id,
                            username = profile.username,
                            nombreCorto = profile.nombreCorto,
                            rol = profile.rol,
                            tipoUsuario = profile.tipoUsuario,

                            isStaff = profile.isStaff,
                            isSuperuser = profile.isSuperuser,
                            isCapitan = profile.isCapitan,

                            fotoUrl = profile.fotoUrl,
                            permisos = profile.permisos,

                            totalEquipos = stats.equipos,
                            totalJugadores = stats.jugadores,
                            totalArbitros = stats.arbitros,

                            errorMessage = null,
                            sessionExpired = false
                        )
                } else {
                    handleErrorResponse(
                        responseCode = response.code()
                    )
                }
            } catch (exception: IOException) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "Error de conexión: ${exception.message}"
                    )
            } catch (exception: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            exception.message
                                ?: "Ocurrió un error al cargar el dashboard."
                    )
            }
        }
    }

    private suspend fun handleErrorResponse(
        responseCode: Int
    ) {
        when (responseCode) {
            401 -> {
                /*
                 * TokenAuthenticator ya intentó renovar
                 * el access token.
                 *
                 * Si continúa el error 401, cerramos la
                 * sesión tanto en el teléfono como en el reloj.
                 */
                wearSessionSender.sendLogout()

                sessionManager.clearSession()

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        sessionExpired = true,
                        logoutCompleted = false,
                        errorMessage = "La sesión expiró."
                    )
            }

            403 -> {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "No tienes permiso para consultar el dashboard."
                    )
            }

            else -> {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "Error $responseCode al cargar el dashboard."
                    )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            /*
             * Primero notificamos al reloj.
             *
             * El envío no bloquea el cierre de sesión
             * del teléfono si no existe un reloj conectado.
             */
            wearSessionSender.sendLogout()

            sessionManager.clearSession()

            _uiState.value =
                _uiState.value.copy(
                    logoutCompleted = true,
                    sessionExpired = false,
                    errorMessage = null
                )
        }
    }

    fun consumeLogout() {
        _uiState.value =
            _uiState.value.copy(
                logoutCompleted = false
            )
    }

    fun consumeSessionExpired() {
        _uiState.value =
            _uiState.value.copy(
                sessionExpired = false
            )
    }
}

data class DashboardUiState(
    val isLoading: Boolean = true,

    val userId: Int? = null,
    val username: String = "",
    val nombreCorto: String = "",
    val rol: String = "",
    val tipoUsuario: String = "sin_rol",

    val isStaff: Boolean = false,
    val isSuperuser: Boolean = false,
    val isCapitan: Boolean = false,

    val fotoUrl: String? = null,

    val permisos: UserPermissions =
        UserPermissions(),

    val totalEquipos: Int = 0,
    val totalJugadores: Int = 0,
    val totalArbitros: Int = 0,

    val errorMessage: String? = null,

    val logoutCompleted: Boolean = false,
    val sessionExpired: Boolean = false
)