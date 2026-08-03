package com.example.lacantera.ui.sports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.CategoryItem
import com.example.lacantera.data.model.CreateCategoryRequest
import com.example.lacantera.data.model.CreatePositionRequest
import com.example.lacantera.data.model.CreateSportRequest
import com.example.lacantera.data.model.SportItem
import com.example.lacantera.data.model.UpdateCategoryRequest
import com.example.lacantera.data.model.UpdateSportRequest
import com.example.lacantera.data.repository.SportsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class SportsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val repository = SportsRepository(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        SportsUiState()
    )

    val uiState: StateFlow<SportsUiState> =
        _uiState.asStateFlow()

    init {
        loadSports()
    }

    fun loadSports() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response = repository.getSports()

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        sports = response.body()
                            ?.sports
                            .orEmpty()
                    )
                } else {
                    handleError(
                        code = response.code(),
                        errorBody = response.errorBody()
                            ?.string()
                    )
                }
            } catch (exception: IOException) {
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

    fun createSport(nombre: String) {
        val cleanName = nombre.trim()

        if (cleanName.isBlank()) {
            showError(
                "Escribe el nombre del deporte."
            )
            return
        }

        executeMutation {
            repository.createSport(
                CreateSportRequest(
                    nombre = cleanName
                )
            )
        }
    }

    fun updateSport(
        sportId: Int,
        nombre: String
    ) {
        val cleanName = nombre.trim()

        if (cleanName.isBlank()) {
            showError(
                "El nombre no puede estar vacío."
            )
            return
        }

        executeMutation {
            repository.updateSport(
                sportId = sportId,
                request = UpdateSportRequest(
                    nombre = cleanName
                )
            )
        }
    }

    fun deleteSport(sportId: Int) {
        executeMutation {
            repository.deleteSport(sportId)
        }
    }

    fun createCategory(
        sportId: Int,
        nombre: String,
        sexo: String,
        edadMinima: Int?,
        edadMaxima: Int?
    ) {
        if (!validateCategory(
                nombre,
                edadMinima,
                edadMaxima
            )
        ) {
            return
        }

        executeMutation {
            repository.createCategory(
                CreateCategoryRequest(
                    nombre = nombre.trim(),
                    sexo = sexo,
                    edadMinima = edadMinima,
                    edadMaxima = edadMaxima,
                    sportId = sportId
                )
            )
        }
    }

    fun updateCategory(
        categoryId: Int,
        nombre: String,
        sexo: String,
        edadMinima: Int?,
        edadMaxima: Int?
    ) {
        if (!validateCategory(
                nombre,
                edadMinima,
                edadMaxima
            )
        ) {
            return
        }

        executeMutation {
            repository.updateCategory(
                categoryId = categoryId,
                request = UpdateCategoryRequest(
                    nombre = nombre.trim(),
                    sexo = sexo,
                    edadMinima = edadMinima,
                    edadMaxima = edadMaxima
                )
            )
        }
    }

    fun deleteCategory(categoryId: Int) {
        executeMutation {
            repository.deleteCategory(
                categoryId
            )
        }
    }

    fun createPosition(
        sportId: Int,
        nombre: String
    ) {
        val cleanName = nombre.trim()

        if (cleanName.isBlank()) {
            showError(
                "Escribe el nombre de la posición."
            )
            return
        }

        executeMutation {
            repository.createPosition(
                sportId = sportId,
                request = CreatePositionRequest(
                    nombre = cleanName
                )
            )
        }
    }

    fun deletePosition(positionId: Int) {
        executeMutation {
            repository.deletePosition(
                positionId
            )
        }
    }

    private fun executeMutation(
        operation: suspend () -> retrofit2.Response<*>
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                val response = operation()

                if (response.isSuccessful) {
                    val successMessage =
                        response.body()
                            ?.let { body ->
                                try {
                                    body.javaClass
                                        .getDeclaredField(
                                            "detail"
                                        )
                                        .apply {
                                            isAccessible = true
                                        }
                                        .get(body)
                                        ?.toString()
                                } catch (_: Exception) {
                                    null
                                }
                            }
                            ?: "Operación realizada correctamente."

                    _uiState.value =
                        _uiState.value.copy(
                            isSaving = false,
                            successMessage =
                                successMessage
                        )

                    loadSports()
                } else {
                    handleError(
                        code = response.code(),
                        errorBody = response.errorBody()
                            ?.string()
                    )
                }
            } catch (exception: IOException) {
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

    private fun validateCategory(
        nombre: String,
        edadMinima: Int?,
        edadMaxima: Int?
    ): Boolean {
        if (nombre.trim().isBlank()) {
            showError(
                "Escribe el nombre de la categoría."
            )
            return false
        }

        if (
            edadMinima != null &&
            edadMaxima != null &&
            edadMinima > edadMaxima
        ) {
            showError(
                "La edad mínima no puede ser mayor " +
                        "que la edad máxima."
            )
            return false
        }

        return true
    }

    private suspend fun handleError(
        code: Int,
        errorBody: String?
    ) {
        if (code == 401) {
            sessionManager.clearSession()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isSaving = false,
                sessionExpired = true,
                errorMessage = "La sesión expiró."
            )

            return
        }

        val message = extractErrorMessage(
            errorBody
        ) ?: when (code) {
            403 -> {
                "No tienes permiso para realizar esta acción."
            }

            404 -> {
                "El registro solicitado no existe."
            }

            409 -> {
                "No se puede eliminar porque tiene información relacionada."
            }

            else -> {
                "Error $code al procesar la solicitud."
            }
        }

        showError(message)
    }

    private fun extractErrorMessage(
        errorBody: String?
    ): String? {
        if (errorBody.isNullOrBlank()) {
            return null
        }

        return try {
            val json = JSONObject(errorBody)

            when {
                json.has("detail") -> {
                    json.getString("detail")
                }

                json.has("nombre") -> {
                    json.get("nombre").toString()
                }

                else -> {
                    errorBody
                }
            }
        } catch (_: Exception) {
            errorBody
        }
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isSaving = false,
            errorMessage = message
        )
    }

    fun consumeSuccessMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null
        )
    }

    fun consumeSessionExpired() {
        _uiState.value = _uiState.value.copy(
            sessionExpired = false
        )
    }
}

data class SportsUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val sports: List<SportItem> = emptyList(),
    val selectedCategory: CategoryItem? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val sessionExpired: Boolean = false
)