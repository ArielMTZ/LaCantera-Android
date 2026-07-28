package com.example.lacantera.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(DashboardUiState())

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            sessionManager.userSession.collect { session ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    username = session.username,
                    nombreCorto = session.nombreCorto,
                    rol = session.rol
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
}

data class DashboardUiState(
    val isLoading: Boolean = true,
    val username: String = "",
    val nombreCorto: String = "",
    val rol: String = "",
    val logoutCompleted: Boolean = false
)