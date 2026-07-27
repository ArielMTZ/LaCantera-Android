package com.example.lacantera.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.repository.AuthRepository
import com.example.lacantera.ui.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }

    fun login() {
        val username = _uiState.value.username.trim()
        val password = _uiState.value.password

        if (username.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Ingresa tu usuario."
            )
            return
        }

        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Ingresa tu contraseña."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response = repository.login(
                    username = username,
                    password = password
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            loginSuccess = true,
                            usuario = body.usuario,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "El servidor respondió sin datos."
                        )
                    }
                } else {
                    val message = when (response.code()) {
                        400 -> "Revisa los datos ingresados."
                        401 -> "Usuario o contraseña incorrectos."
                        403 -> "No tienes permiso para iniciar sesión."
                        404 -> "No se encontró el servicio de inicio de sesión."
                        500 -> "Ocurrió un error en el servidor."
                        else -> "No fue posible iniciar sesión."
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            } catch (_: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo conectar con el servidor Django."
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

    fun clearLoginSuccess() {
        _uiState.value = _uiState.value.copy(
            loginSuccess = false
        )
    }
}