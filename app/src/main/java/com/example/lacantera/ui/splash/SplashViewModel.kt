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

            // Permite mostrar brevemente la pantalla Splash.
            delay(1_200)

            val session = sessionManager.userSession.first()

            val hasValidLocalSession =
                session.isLoggedIn &&
                        !session.accessToken.isNullOrBlank() &&
                        !session.refreshToken.isNullOrBlank()

            val destination = if (hasValidLocalSession) {
                SplashDestination.DASHBOARD
            } else {
                SplashDestination.PUBLIC_HOME
            }

            _uiState.value = SplashUiState(
                isLoading = false,
                destination = destination
            )
        }
    }
}

data class SplashUiState(
    val isLoading: Boolean = true,
    val destination: SplashDestination? = null
)

enum class SplashDestination {
    PUBLIC_HOME,
    DASHBOARD
}