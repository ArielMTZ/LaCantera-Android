package com.example.lacantera.ui.teams

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.TeamItem
import com.example.lacantera.data.model.UpdateTeamRequest
import com.example.lacantera.data.repository.TeamsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class TeamDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val teamsRepository = TeamsRepository(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        TeamDetailUiState()
    )

    val uiState: StateFlow<TeamDetailUiState> =
        _uiState.asStateFlow()

    fun loadTeam(
        teamId: Int
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                updateCompleted = false
            )

            try {
                val response =
                    teamsRepository.getTeamDetail(teamId)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage =
                                "El servidor respondió sin datos del equipo."
                        )

                        return@launch
                    }

                    val team = body.equipo

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        team = team,
                        nombre = team.nombre,
                        activo = team.activo,
                        errorMessage = null
                    )
                } else {
                    handleError(
                        responseCode = response.code(),
                        action = "cargar"
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
                        ?: "Ocurrió un error al cargar el equipo."
                )
            }
        }
    }

    fun onNombreChanged(
        value: String
    ) {
        _uiState.value = _uiState.value.copy(
            nombre = value,
            errorMessage = null,
            updateCompleted = false
        )
    }

    fun onActivoChanged(
        value: Boolean
    ) {
        _uiState.value = _uiState.value.copy(
            activo = value,
            errorMessage = null,
            updateCompleted = false
        )
    }

    fun updateTeam() {
        val currentTeam = _uiState.value.team
            ?: return

        val nombre = _uiState.value.nombre.trim()

        if (nombre.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage =
                    "El nombre del equipo no puede estar vacío."
            )

            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                errorMessage = null,
                updateCompleted = false
            )

            try {
                val request = UpdateTeamRequest(
                    nombre = nombre,
                    activo = _uiState.value.activo
                )

                val response =
                    teamsRepository.updateTeam(
                        teamId = currentTeam.id,
                        request = request
                    )

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null) {
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            errorMessage =
                                "El servidor respondió sin datos."
                        )

                        return@launch
                    }

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        team = body.equipo,
                        nombre = body.equipo.nombre,
                        activo = body.equipo.activo,
                        successMessage = body.detail,
                        updateCompleted = true,
                        errorMessage = null
                    )
                } else {
                    handleError(
                        responseCode = response.code(),
                        action = "actualizar"
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
                        ?: "Ocurrió un error al actualizar el equipo."
                )
            }
        }
    }

    private suspend fun handleError(
        responseCode: Int,
        action: String
    ) {
        when (responseCode) {
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
                        "No tienes permiso para $action este equipo."
                )
            }

            404 -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaving = false,
                    errorMessage =
                        "El equipo solicitado no existe."
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaving = false,
                    errorMessage =
                        "Error $responseCode al $action el equipo."
                )
            }
        }
    }

    fun consumeSessionExpired() {
        _uiState.value = _uiState.value.copy(
            sessionExpired = false
        )
    }

    fun consumeUpdateCompleted() {
        _uiState.value = _uiState.value.copy(
            updateCompleted = false,
            successMessage = null
        )
    }
}

data class TeamDetailUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    val team: TeamItem? = null,

    val nombre: String = "",
    val activo: Boolean = true,

    val errorMessage: String? = null,
    val successMessage: String? = null,

    val updateCompleted: Boolean = false,
    val sessionExpired: Boolean = false
)