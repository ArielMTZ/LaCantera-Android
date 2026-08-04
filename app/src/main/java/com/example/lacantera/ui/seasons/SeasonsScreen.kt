package com.example.lacantera.ui.seasons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.data.model.SeasonItem
import com.example.lacantera.data.model.SportItem

@Composable
fun SeasonsScreen(
    onBackClick: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: SeasonsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            viewModel.consumeSessionExpired()
            onSessionExpired()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SeasonsHeader(
            onBackClick = onBackClick,
            onCreateClick = viewModel::showCreateDialog
        )

        uiState.successMessage?.let { message ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme
                            .primaryContainer
                )
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme
                        .onPrimaryContainer
                )
            }
        }

        uiState.errorMessage?.let { message ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme
                            .errorContainer
                )
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme
                        .onErrorContainer
                )
            }
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.seasons.isEmpty() -> {
                EmptySeasonsContent(
                    onCreateClick =
                        viewModel::showCreateDialog
                )
            }

            else -> {
                ActiveSeasonsList(
                    seasons = uiState.seasons,
                    onFinalizeClick =
                        viewModel::requestFinalize
                )
            }
        }
    }

    if (uiState.showCreateDialog) {
        CreateSeasonDialog(
            sports = uiState.sports,
            seasonName = uiState.seasonName,
            selectedSportId =
                uiState.selectedSportId,
            selectedSportName =
                uiState.selectedSportName,
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            isSaving = uiState.isSaving,
            onNameChanged =
                viewModel::onNameChanged,
            onSportSelected =
                viewModel::onSportSelected,
            onStartDateChanged =
                viewModel::onStartDateChanged,
            onEndDateChanged =
                viewModel::onEndDateChanged,
            onDismiss =
                viewModel::hideCreateDialog,
            onConfirm =
                viewModel::createSeason
        )
    }

    uiState.seasonToFinalize?.let { season ->
        FinalizeSeasonDialog(
            season = season,
            isFinalizing =
                uiState.isFinalizing,
            onDismiss =
                viewModel::cancelFinalize,
            onConfirm =
                viewModel::finalizeSeason
        )
    }
}

@Composable
private fun SeasonsHeader(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBackClick
            ) {
                Text("Regresar")
            }

            Text(
                text = "Temporada actual",
                modifier = Modifier.padding(
                    start = 16.dp
                ),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onCreateClick
        ) {
            Text("Nueva")
        }
    }
}

@Composable
private fun ActiveSeasonsList(
    seasons: List<SeasonItem>,
    onFinalizeClick: (SeasonItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Temporadas activas: ${seasons.size}",
                fontWeight = FontWeight.SemiBold
            )
        }

        items(
            items = seasons,
            key = { season ->
                season.id
            }
        ) { season ->
            SeasonCard(
                season = season,
                onFinalizeClick = {
                    onFinalizeClick(season)
                }
            )
        }
    }
}

@Composable
private fun SeasonCard(
    season: SeasonItem,
    onFinalizeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = season.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Deporte: ${season.sportName}"
            )

            Text(
                text = "Inicio: ${season.fechaInicio}"
            )

            Text(
                text = "Fin: ${season.fechaFin}"
            )

            Text(
                text = (
                        "Categorías asociadas: " +
                                season.categoryCount
                        )
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Estado: ${season.statusDisplay}",
                color = MaterialTheme
                    .colorScheme
                    .primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Button(
                onClick = onFinalizeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar temporada")
            }
        }
    }
}

@Composable
private fun EmptySeasonsContent(
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = "No hay temporadas activas.",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = (
                    "Puedes crear una temporada " +
                            "para uno de los deportes."
                    ),
            color = MaterialTheme
                .colorScheme
                .onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = onCreateClick
        ) {
            Text("Crear temporada")
        }
    }
}

@Composable
private fun CreateSeasonDialog(
    sports: List<SportItem>,
    seasonName: String,
    selectedSportId: Int?,
    selectedSportName: String,
    startDate: String,
    endDate: String,
    isSaving: Boolean,
    onNameChanged: (String) -> Unit,
    onSportSelected: (Int, String) -> Unit,
    onStartDateChanged: (String) -> Unit,
    onEndDateChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var sportMenuExpanded by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = {
            if (!isSaving) {
                onDismiss()
            }
        },
        title = {
            Text("Crear temporada")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = seasonName,
                    onValueChange = onNameChanged,
                    label = {
                        Text("Nombre")
                    },
                    placeholder = {
                        Text("Ej. Apertura 2026")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedSportName,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Deporte")
                        },
                        placeholder = {
                            Text("Selecciona un deporte")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                sportMenuExpanded = true
                            }
                    )

                    DropdownMenu(
                        expanded = sportMenuExpanded,
                        onDismissRequest = {
                            sportMenuExpanded = false
                        }
                    ) {
                        if (sports.isEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "No hay deportes disponibles"
                                    )
                                },
                                onClick = {
                                    sportMenuExpanded = false
                                },
                                enabled = false
                            )
                        }

                        sports.forEach { sport ->
                            DropdownMenuItem(
                                text = {
                                    Text(sport.nombre)
                                },
                                onClick = {
                                    sportMenuExpanded = false

                                    onSportSelected(
                                        sport.id,
                                        sport.nombre
                                    )
                                }
                            )
                        }
                    }
                }

                if (selectedSportId != null) {
                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = (
                                "Deporte seleccionado: " +
                                        selectedSportName
                                ),
                        color = MaterialTheme
                            .colorScheme
                            .primary
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = startDate,
                    onValueChange =
                        onStartDateChanged,
                    label = {
                        Text("Fecha de inicio")
                    },
                    placeholder = {
                        Text("AAAA-MM-DD")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = endDate,
                    onValueChange =
                        onEndDateChanged,
                    label = {
                        Text("Fecha final")
                    },
                    placeholder = {
                        Text("AAAA-MM-DD")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator()
                } else {
                    Text("Crear")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isSaving
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun FinalizeSeasonDialog(
    season: SeasonItem,
    isFinalizing: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!isFinalizing) {
                onDismiss()
            }
        },
        title = {
            Text("Finalizar temporada")
        },
        text = {
            Column {
                Text(
                    text = (
                            "¿Seguro que deseas finalizar " +
                                    "\"${season.nombre}\"?"
                            )
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Deporte: ${season.sportName}"
                )

                Text(
                    text = (
                            "Después de finalizarla dejará " +
                                    "de aparecer como temporada activa."
                            ),
                    color = MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isFinalizing
            ) {
                if (isFinalizing) {
                    CircularProgressIndicator()
                } else {
                    Text("Finalizar")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isFinalizing
            ) {
                Text("Cancelar")
            }
        }
    )
}