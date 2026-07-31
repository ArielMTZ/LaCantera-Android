package com.example.lacantera.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(SplashUiState())

    val uiState: StateFlow<SplashUiState> =
        _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            delay(1_200)

            val session = sessionManager.userSession.first()

            val hasValidLocalSession =
                session.isLoggedIn &&
                        !session.accessToken.isNullOrBlank() &&
                        !session.refreshToken.isNullOrBlank()

            val destination = if (!hasValidLocalSession) {
                SplashDestination.PUBLIC_HOME
            } else {
                resolveDestination(session.rol)
            }

            _uiState.value = SplashUiState(
                isLoading = false,
                destination = destination
            )
        }
    }

    private fun resolveDestination(
        role: String
    ): SplashDestination {
        return when (role.trim().lowercase()) {
            "arbitro" -> SplashDestination.DASHBOARD_REFEREE
            "capitan" -> SplashDestination.DASHBOARD_CAPTAIN

            "superadmin",
            "staff",
            "admin_principal",
            "admin",
            "finanzas",
            "subadmin" -> SplashDestination.DASHBOARD_ADMIN

            else -> SplashDestination.PUBLIC_HOME
        }
    }
}

data class SplashUiState(
    val isLoading: Boolean = true,
    val destination: SplashDestination? = null
)

enum class SplashDestination {
    PUBLIC_HOME,
    DASHBOARD_ADMIN,
    DASHBOARD_REFEREE,
    DASHBOARD_CAPTAIN
}