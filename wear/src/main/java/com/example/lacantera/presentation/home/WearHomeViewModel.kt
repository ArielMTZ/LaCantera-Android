package com.example.lacantera.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.WearSessionManager
import com.example.lacantera.data.model.WearSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WearHomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager =
        WearSessionManager(
            context = application.applicationContext
        )

    val session: StateFlow<WearSession> =
        sessionManager.session.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000
            ),
            initialValue = WearSession()
        )

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }
}