package com.example.lacantera.ui.referee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.RefereeMatchItem
import com.example.lacantera.data.repository.RefereeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class RefereeMatchesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RefereeRepository(
        context = application.applicationContext
    )

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        RefereeMatchesUiState()
    )

    val uiState: StateFlow<RefereeMatchesUiState> =
        _uiState.asStateFlow()

    init {
        loadPendingMatches()
    }

    fun loadPendingMatches() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                showingHistory = false
            )

            try {
                val response = repository
                    .getPendingMatches()

                if (response.isSuccessful) {
                    val body = response.body()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        count = body?.count ?: 0,
                        matches = body
                            ?.matches
                            .orEmpty(),
                        errorMessage = null,
                        showingHistory = false
                    )
                } else {
                    handleError(
                        response.code()
                    )
                }
            } catch (_: IOException) {
                showError(
                    "No se pudo conectar con el servidor."
                )
            } catch (exception: Exception) {
                showError(
                    exception.message
                        ?: "Ocurrió un error inesperado."
                )
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                showingHistory = true
            )

            try {
                val response = repository
                    .getMatchHistory()

                if (response.isSuccessful) {
                    val body = response.body()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        count = body?.count ?: 0,
                        matches = body
                            ?.matches
                            .orEmpty(),
                        errorMessage = null,
                        showingHistory = true
                    )
                } else {
                    handleError(
                        response.code()
                    )
                }
            } catch (_: IOException) {
                showError(
                    "No se pudo conectar con el servidor."
                )
            } catch (exception: Exception) {
                showError(
                    exception.message
                        ?: "Ocurrió un error inesperado."
                )
            }
        }
    }

    private suspend fun handleError(
        code: Int
    ) {
        when (code) {
            401 -> {
                sessionManager.clearSession()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    sessionExpired = true,
                    errorMessage = "La sesión expiró."
                )
            }

            403 -> {
                showError(
                    "El usuario no tiene perfil de árbitro."
                )
            }

            else -> {
                showError(
                    "Error $code al cargar partidos."
                )
            }
        }
    }

    private fun showError(
        message: String
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message
        )
    }

    fun consumeSessionExpired() {
        _uiState.value = _uiState.value.copy(
            sessionExpired = false
        )
    }
}

data class RefereeMatchesUiState(
    val isLoading: Boolean = true,
    val count: Int = 0,
    val matches: List<RefereeMatchItem> = emptyList(),
    val showingHistory: Boolean = false,
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)