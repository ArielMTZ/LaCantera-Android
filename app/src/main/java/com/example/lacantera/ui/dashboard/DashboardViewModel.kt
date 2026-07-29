package com.example.lacantera.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.UserPermissions
import com.example.lacantera.data.repository.ProfileRepository
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

    private val profileRepository = ProfileRepository(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(DashboardUiState())

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response = profileRepository.getProfile()

                if (response.isSuccessful) {
                    val profile = response.body()

                    if (profile != null) {
                        _uiState.value = _uiState.value.copy(
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
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "El servidor respondió sin perfil."
                        )
                    }
                } else {
                    when (response.code()) {
                        401 -> {
                            sessionManager.clearSession()

                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                sessionExpired = true,
                                errorMessage = "La sesión expiró."
                            )
                        }

                        403 -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = "No tienes permiso para consultar el perfil."
                            )
                        }

                        else -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = "Error ${response.code()} al cargar el perfil."
                            )
                        }
                    }
                }
            } catch (exception: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión: ${exception.message}"
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message
                        ?: "Ocurrió un error al cargar el perfil."
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()

            _uiState.value = _uiState.value.copy(
                logoutCompleted = true
            )
        }
    }

    fun consumeLogout() {
        _uiState.value = _uiState.value.copy(
            logoutCompleted = false
        )
    }

    fun consumeSessionExpired() {
        _uiState.value = _uiState.value.copy(
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

    val permisos: UserPermissions = UserPermissions(),

    val errorMessage: String? = null,

    val logoutCompleted: Boolean = false,
    val sessionExpired: Boolean = false
)