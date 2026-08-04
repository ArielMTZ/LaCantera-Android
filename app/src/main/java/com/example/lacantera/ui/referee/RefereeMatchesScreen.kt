package com.example.lacantera.ui.referee

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.data.model.RefereeMatchItem

@Composable
fun RefereeMatchesScreen(
    onBackClick: () -> Unit,
    onMatchClick: (Int) -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: RefereeMatchesViewModel = viewModel()
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBackClick
            ) {
                Text("Regresar")
            }

            Text(
                text = if (uiState.showingHistory) {
                    "Historial de partidos"
                } else {
                    "Mis partidos"
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                ),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = viewModel::loadPendingMatches,
                modifier = Modifier.weight(1f),
                enabled = !uiState.isLoading
            ) {
                Text("Pendientes")
            }

            OutlinedButton(
                onClick = viewModel::loadHistory,
                modifier = Modifier.weight(1f),
                enabled = !uiState.isLoading
            ) {
                Text("Historial")
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
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
                        text = uiState.errorMessage
                            ?: "Error desconocido",
                        color = MaterialTheme
                            .colorScheme
                            .error
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            if (uiState.showingHistory) {
                                viewModel.loadHistory()
                            } else {
                                viewModel.loadPendingMatches()
                            }
                        }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            uiState.matches.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (
                            uiState.showingHistory
                        ) {
                            "No hay partidos en el historial."
                        } else {
                            "No tienes partidos pendientes."
                        }
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Total: ${uiState.count}",
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }

                    items(
                        items = uiState.matches,
                        key = { match ->
                            match.id
                        }
                    ) { match ->
                        RefereeMatchCard(
                            match = match,
                            onClick = {
                                onMatchClick(match.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RefereeMatchCard(
    match: RefereeMatchItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
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
                text = (
                        "${match.localTeamName} vs " +
                                match.visitorTeamName
                        ),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = match.seasonName
            )

            Text(
                text = (
                        "Categoría: " +
                                match.categoryName
                        )
            )

            Text(
                text = (
                        "Jornada ${match.jornadaNumero}"
                        )
            )

            Text(
                text = match.courtDisplay
            )

            Text(
                text = "Fecha: ${match.fecha}"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = if (match.finalizado) {
                    "Resultado: ${match.result}"
                } else {
                    "Pendiente"
                },
                fontWeight = FontWeight.SemiBold,
                color = if (match.finalizado) {
                    MaterialTheme
                        .colorScheme
                        .primary
                } else {
                    MaterialTheme
                        .colorScheme
                        .secondary
                }
            )

            match.winnerName?.let { winner ->
                Text(
                    text = "Ganador: $winner",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}