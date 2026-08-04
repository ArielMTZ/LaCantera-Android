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

class RefereeMatchDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RefereeRepository(
        context = application.applicationContext
    )

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        RefereeMatchDetailUiState()
    )

    val uiState: StateFlow<RefereeMatchDetailUiState> =
        _uiState.asStateFlow()

    fun loadMatch(
        matchId: Int
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response = repository.getMatchDetail(
                    matchId = matchId
                )

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        match = response.body()?.match,
                        errorMessage = null
                    )
                } else {
                    when (response.code()) {
                        401 -> {
                            sessionManager.clearSession()

                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    sessionExpired = true,
                                    errorMessage =
                                        "La sesión expiró."
                                )
                        }

                        404 -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    errorMessage = (
                                            "El partido no existe " +
                                                    "o no está asignado."
                                            )
                                )
                        }

                        else -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    errorMessage = (
                                            "Error ${response.code()} " +
                                                    "al cargar el partido."
                                            )
                                )
                        }
                    }
                }
            } catch (_: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage =
                        "No se pudo conectar con el servidor."
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message
                        ?: "Ocurrió un error inesperado."
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

data class RefereeMatchDetailUiState(
    val isLoading: Boolean = true,
    val match: RefereeMatchItem? = null,
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)