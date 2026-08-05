package com.example.lacantera.ui.teams

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.CaptainTeamItem
import com.example.lacantera.data.repository.CaptainTeamsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class CaptainTeamsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val captainTeamsRepository =
        CaptainTeamsRepository(
            context = application.applicationContext
        )

    private val _uiState = MutableStateFlow(
        CaptainTeamsUiState()
    )

    val uiState: StateFlow<CaptainTeamsUiState> =
        _uiState.asStateFlow()

    init {
        loadCaptainTeams()
    }

    fun loadCaptainTeams() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response =
                    captainTeamsRepository.getCaptainTeams()

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = (
                                    "El servidor respondió sin " +
                                            "información de equipos."
                                    )
                        )

                        return@launch
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        count = body.count,
                        equipos = body.equipos,
                        errorMessage = null,
                        sessionExpired = false
                    )
                } else {
                    handleErrorResponse(
                        responseCode = response.code()
                    )
                }
            } catch (exception: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = (
                            "No fue posible conectar con el servidor. " +
                                    "Verifica tu conexión."
                            )
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message
                        ?: "Ocurrió un error al cargar tus equipos."
                )
            }
        }
    }

    private suspend fun handleErrorResponse(
        responseCode: Int
    ) {
        when (responseCode) {
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
                    errorMessage = (
                            "No tienes permiso para consultar " +
                                    "los equipos del capitán."
                            )
                )
            }

            404 -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = (
                            "No se encontró el servicio " +
                                    "de equipos del capitán."
                            )
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = (
                            "Error $responseCode al cargar " +
                                    "los equipos."
                            )
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

data class CaptainTeamsUiState(
    val isLoading: Boolean = true,
    val count: Int = 0,
    val equipos: List<CaptainTeamItem> = emptyList(),
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)