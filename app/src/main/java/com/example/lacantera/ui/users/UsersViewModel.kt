package com.example.lacantera.ui.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.Usuario
import com.example.lacantera.data.repository.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class UsersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val usersRepository = UsersRepository(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        UsersUiState()
    )

    val uiState: StateFlow<UsersUiState> =
        _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadUsers()
    }

    fun loadUsers(
        page: Int = 1,
        append: Boolean = false
    ) {
        if (_uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = !append,
                isLoadingMore = append,
                errorMessage = null
            )

            try {
                val currentState = _uiState.value

                val response = usersRepository.getUsers(
                    page = page,
                    search = currentState.search
                        .trim()
                        .takeIf { it.isNotBlank() },
                    rol = currentState.selectedRole,
                    estado = currentState.selectedStatus
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body == null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage =
                                "El servidor respondió sin usuarios."
                        )
                        return@launch
                    }

                    val updatedUsers = if (append) {
                        (
                                currentState.users +
                                        body.results
                                ).distinctBy { user ->
                                user.id
                            }
                    } else {
                        body.results
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        users = updatedUsers,
                        count = body.count,
                        currentPage = page,
                        hasNextPage = body.next != null,
                        errorMessage = null
                    )
                } else {
                    handleErrorCode(
                        code = response.code()
                    )
                }
            } catch (exception: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    errorMessage =
                        "Error de conexión: ${exception.message}"
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    errorMessage = exception.message
                        ?: "Ocurrió un error al cargar los usuarios."
                )
            }
        }
    }

    fun onSearchChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            search = value
        )

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500)
            loadUsers(page = 1)
        }
    }

    fun selectRole(role: String?) {
        _uiState.value = _uiState.value.copy(
            selectedRole = role
        )

        loadUsers(page = 1)
    }

    fun selectStatus(status: String?) {
        _uiState.value = _uiState.value.copy(
            selectedStatus = status
        )

        loadUsers(page = 1)
    }

    fun clearFilters() {
        searchJob?.cancel()

        _uiState.value = _uiState.value.copy(
            search = "",
            selectedRole = null,
            selectedStatus = null
        )

        loadUsers(page = 1)
    }

    fun loadNextPage() {
        val state = _uiState.value

        if (
            state.hasNextPage &&
            !state.isLoading &&
            !state.isLoadingMore
        ) {
            loadUsers(
                page = state.currentPage + 1,
                append = true
            )
        }
    }

    fun refreshUsers() {
        loadUsers(page = 1)
    }

    private suspend fun handleErrorCode(
        code: Int
    ) {
        when (code) {
            401 -> {
                sessionManager.clearSession()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    sessionExpired = true,
                    errorMessage = "La sesión expiró."
                )
            }

            403 -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    accessDenied = true,
                    errorMessage =
                        "No tienes permiso para administrar usuarios."
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    errorMessage =
                        "Error $code al cargar los usuarios."
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

data class UsersUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val users: List<Usuario> = emptyList(),
    val count: Int = 0,
    val search: String = "",
    val selectedRole: String? = null,
    val selectedStatus: String? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false,
    val accessDenied: Boolean = false
)