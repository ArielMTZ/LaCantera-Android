package com.example.lacantera.ui.teams

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.TeamItem
import com.example.lacantera.data.repository.TeamsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class TeamsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val teamsRepository = TeamsRepository(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        TeamsUiState()
    )

    val uiState: StateFlow<TeamsUiState> =
        _uiState.asStateFlow()

    init {
        loadTeams()
    }

    fun loadTeams() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response = teamsRepository.getTeams()

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "El servidor respondió sin equipos."
                        )
                        return@launch
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        count = body.count,
                        equipos = body.equipos,
                        errorMessage = null
                    )
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
                                errorMessage = "No tienes permiso para ver los equipos."
                            )
                        }

                        else -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage =
                                    "Error ${response.code()} al cargar los equipos."
                            )
                        }
                    }
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
                        ?: "Ocurrió un error al cargar los equipos."
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

data class TeamsUiState(
    val isLoading: Boolean = true,
    val count: Int = 0,
    val equipos: List<TeamItem> = emptyList(),
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)