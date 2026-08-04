package com.example.lacantera.ui.seasons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacantera.data.local.SessionManager
import com.example.lacantera.data.model.CreateSeasonRequest
import com.example.lacantera.data.model.SeasonItem
import com.example.lacantera.data.repository.SeasonsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import com.example.lacantera.data.model.SportItem
import com.example.lacantera.data.remote.RetrofitClient

class SeasonsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val apiService =
        RetrofitClient.getApiService(
            application.applicationContext
        )

    private val repository = SeasonsRepository(
        context = application.applicationContext
    )

    private val sessionManager = SessionManager(
        context = application.applicationContext
    )

    private val _uiState = MutableStateFlow(
        SeasonsUiState()
    )

    val uiState: StateFlow<SeasonsUiState> =
        _uiState.asStateFlow()

    init {
        loadScreenData()
    }

    fun loadActiveSeasons() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response =
                    repository.getActiveSeasons()

                if (response.isSuccessful) {
                    val body = response.body()

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            seasons = body
                                ?.seasons
                                .orEmpty(),
                            errorMessage = null
                        )
                } else {
                    handleHttpError(
                        code = response.code(),
                        serverMessage = response
                            .errorBody()
                            ?.string()
                    )
                }
            } catch (exception: IOException) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "Error de conexión: ${exception.message}"
                    )
            } catch (exception: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            exception.message
                                ?: "No fue posible cargar las temporadas."
                    )
            }
        }
    }

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            seasonName = value
        )
    }

    fun onSportSelected(
        sportId: Int,
        sportName: String
    ) {
        _uiState.value = _uiState.value.copy(
            selectedSportId = sportId,
            selectedSportName = sportName
        )
    }

    fun onStartDateChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            startDate = value
        )
    }

    fun onEndDateChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            endDate = value
        )
    }

    fun showCreateDialog() {
        _uiState.value = _uiState.value.copy(
            showCreateDialog = true,
            errorMessage = null
        )
    }

    fun hideCreateDialog() {
        _uiState.value = _uiState.value.copy(
            showCreateDialog = false,
            seasonName = "",
            selectedSportId = null,
            selectedSportName = "",
            startDate = "",
            endDate = "",
            errorMessage = null
        )
    }

    fun requestFinalize(
        season: SeasonItem
    ) {
        _uiState.value = _uiState.value.copy(
            seasonToFinalize = season
        )
    }

    fun cancelFinalize() {
        _uiState.value = _uiState.value.copy(
            seasonToFinalize = null
        )
    }

    fun createSeason() {
        val state = _uiState.value

        val validationError =
            validateCreateForm(state)

        if (validationError != null) {
            _uiState.value = state.copy(
                errorMessage = validationError
            )
            return
        }

        val sportId =
            state.selectedSportId ?: return

        viewModelScope.launch {
            _uiState.value = state.copy(
                isSaving = true,
                errorMessage = null
            )

            try {
                val response =
                    repository.createSeason(
                        request = CreateSeasonRequest(
                            nombre =
                                state.seasonName.trim(),
                            sportId = sportId,
                            fechaInicio =
                                state.startDate.trim(),
                            fechaFin =
                                state.endDate.trim()
                        )
                    )

                if (response.isSuccessful) {
                    _uiState.value =
                        _uiState.value.copy(
                            isSaving = false,
                            showCreateDialog = false,
                            seasonName = "",
                            selectedSportId = null,
                            selectedSportName = "",
                            startDate = "",
                            endDate = "",
                            successMessage =
                                response.body()?.message
                                    ?: "Temporada creada."
                        )

                    loadActiveSeasons()
                } else {
                    handleHttpError(
                        code = response.code(),
                        serverMessage = response
                            .errorBody()
                            ?.string()
                    )
                }
            } catch (exception: IOException) {
                _uiState.value =
                    _uiState.value.copy(
                        isSaving = false,
                        errorMessage =
                            "Error de conexión: ${exception.message}"
                    )
            } catch (exception: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        isSaving = false,
                        errorMessage =
                            exception.message
                                ?: "No fue posible crear la temporada."
                    )
            }
        }
    }

    fun finalizeSeason() {
        val season =
            _uiState.value.seasonToFinalize
                ?: return

        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    isFinalizing = true,
                    errorMessage = null
                )

            try {
                val response =
                    repository.finalizeSeason(
                        seasonId = season.id
                    )

                if (response.isSuccessful) {
                    _uiState.value =
                        _uiState.value.copy(
                            isFinalizing = false,
                            seasonToFinalize = null,
                            successMessage =
                                response.body()?.message
                                    ?: "Temporada finalizada."
                        )

                    loadActiveSeasons()
                } else {
                    handleHttpError(
                        code = response.code(),
                        serverMessage = response
                            .errorBody()
                            ?.string()
                    )
                }
            } catch (exception: IOException) {
                _uiState.value =
                    _uiState.value.copy(
                        isFinalizing = false,
                        errorMessage =
                            "Error de conexión: ${exception.message}"
                    )
            } catch (exception: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        isFinalizing = false,
                        errorMessage =
                            exception.message
                                ?: "No fue posible finalizar la temporada."
                    )
            }
        }
    }

    private fun validateCreateForm(
        state: SeasonsUiState
    ): String? {
        if (state.seasonName.isBlank()) {
            return "Escribe el nombre de la temporada."
        }

        if (state.selectedSportId == null) {
            return "Selecciona un deporte."
        }

        if (
            !isValidDateFormat(state.startDate)
        ) {
            return (
                    "La fecha inicial debe tener formato "
                            + "AAAA-MM-DD."
                    )
        }

        if (
            !isValidDateFormat(state.endDate)
        ) {
            return (
                    "La fecha final debe tener formato "
                            + "AAAA-MM-DD."
                    )
        }

        if (state.endDate < state.startDate) {
            return (
                    "La fecha final no puede ser anterior "
                            + "a la fecha inicial."
                    )
        }

        return null
    }

    private fun isValidDateFormat(
        value: String
    ): Boolean {
        return value.matches(
            Regex("""\d{4}-\d{2}-\d{2}""")
        )
    }

    private suspend fun handleHttpError(
        code: Int,
        serverMessage: String?
    ) {
        when (code) {
            401 -> {
                sessionManager.clearSession()

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        isSaving = false,
                        isFinalizing = false,
                        sessionExpired = true,
                        errorMessage =
                            "La sesión expiró."
                    )
            }

            403 -> {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        isSaving = false,
                        isFinalizing = false,
                        errorMessage =
                            serverMessage
                                ?: "No tienes permiso para administrar temporadas."
                    )
            }

            else -> {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        isSaving = false,
                        isFinalizing = false,
                        errorMessage =
                            serverMessage
                                ?: "Error $code al procesar la temporada."
                    )
            }
        }
    }

    fun consumeSessionExpired() {
        _uiState.value = _uiState.value.copy(
            sessionExpired = false
        )
    }

    fun loadScreenData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val seasonsResponse =
                    repository.getActiveSeasons()

                val sportsResponse =
                    apiService.getSports()

                if (
                    seasonsResponse.isSuccessful &&
                    sportsResponse.isSuccessful
                ) {
                    val activeSeasons =
                        seasonsResponse.body()
                            ?.seasons
                            .orEmpty()

                    val sports =
                        sportsResponse.body()
                            ?.sports
                            .orEmpty()

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            seasons = activeSeasons,
                            sports = sports,
                            errorMessage = null
                        )
                } else {
                    val failedResponse =
                        if (!seasonsResponse.isSuccessful) {
                            seasonsResponse
                        } else {
                            sportsResponse
                        }

                    handleHttpError(
                        code = failedResponse.code(),
                        serverMessage = failedResponse
                            .errorBody()
                            ?.string()
                    )
                }
            } catch (exception: IOException) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            "Error de conexión: ${exception.message}"
                    )
            } catch (exception: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: "No fue posible cargar las temporadas."
                    )
            }
        }
    }

    fun consumeSuccessMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null
        )
    }
}
data class SeasonsUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isFinalizing: Boolean = false,

    val seasons: List<SeasonItem> = emptyList(),
    val sports: List<SportItem> = emptyList(),

    val showCreateDialog: Boolean = false,
    val seasonToFinalize: SeasonItem? = null,

    val seasonName: String = "",
    val selectedSportId: Int? = null,
    val selectedSportName: String = "",
    val startDate: String = "",
    val endDate: String = "",

    val errorMessage: String? = null,
    val successMessage: String? = null,
    val sessionExpired: Boolean = false
)