package com.example.lacantera.ui.referee

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
import com.example.lacantera.data.model.RefereeSetItem

@Composable
fun RefereeMatchDetailScreen(
    matchId: Int,
    onBackClick: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: RefereeMatchDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(matchId) {
        viewModel.loadMatch(matchId)
    }

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
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBackClick
            ) {
                Text("Regresar")
            }

            Text(
                text = "Detalle del partido",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
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
                            viewModel.loadMatch(matchId)
                        }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            uiState.match == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontró el partido.")
                }
            }

            else -> {
                val match = uiState.match

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            modifier =
                                Modifier.fillMaxWidth(),
                            shape =
                                RoundedCornerShape(14.dp),
                            elevation =
                                CardDefaults.cardElevation(
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
                                            "${match?.localTeamName} " +
                                                    "vs " +
                                                    "${match?.visitorTeamName}"
                                            ),
                                    fontSize = 21.sp,
                                    fontWeight =
                                        FontWeight.Bold
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                Text(
                                    text = (
                                            "Temporada: " +
                                                    match?.seasonName
                                            )
                                )

                                Text(
                                    text = (
                                            "Categoría: " +
                                                    match?.categoryName
                                            )
                                )

                                Text(
                                    text = (
                                            "Jornada: " +
                                                    match?.jornadaNumero
                                            )
                                )

                                Text(
                                    text = (
                                            "Cancha: " +
                                                    match?.courtDisplay
                                            )
                                )

                                Text(
                                    text = (
                                            "Fecha: " +
                                                    match?.fecha
                                            )
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(10.dp)
                                )

                                Text(
                                    text = (
                                            "Resultado: " +
                                                    match?.result
                                            ),
                                    fontWeight =
                                        FontWeight.SemiBold
                                )

                                Text(
                                    text = if (
                                        match?.finalizado == true
                                    ) {
                                        "Estado: Finalizado"
                                    } else {
                                        "Estado: Pendiente"
                                    }
                                )

                                match?.winnerName?.let {
                                        winner ->
                                    Text(
                                        text = (
                                                "Ganador: $winner"
                                                ),
                                        fontWeight =
                                            FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Sets",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (
                        match?.sets.isNullOrEmpty()
                    ) {
                        item {
                            Text(
                                text = (
                                        "Todavía no hay sets " +
                                                "registrados."
                                        )
                            )
                        }
                    } else {
                        items(
                            items = match?.sets.orEmpty(),
                            key = { set ->
                                set.id
                            }
                        ) { set ->
                            RefereeSetCard(
                                set = set
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Observación",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        Card(
                            modifier =
                                Modifier.fillMaxWidth(),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .surfaceVariant
                                )
                        ) {
                            Text(
                                text = match
                                    ?.observation
                                    ?.ifBlank {
                                        "Sin observaciones."
                                    }
                                    ?: "Sin observaciones.",
                                modifier =
                                    Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RefereeSetCard(
    set: RefereeSetItem
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = "Set ${set.numeroSet}",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = (
                        "${set.puntosLocal} - " +
                                set.puntosVisitante
                        ),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = when {
                    set.finalizado -> {
                        "Finalizado"
                    }

                    set.pausado -> {
                        "Pausado"
                    }

                    else -> {
                        "En curso"
                    }
                }
            )

            set.ganador?.let { winner ->
                Text(
                    text = when (winner) {
                        "local" -> {
                            "Ganador del set: local"
                        }

                        "visitante" -> {
                            "Ganador del set: visitante"
                        }

                        else -> {
                            "Ganador del set: $winner"
                        }
                    }
                )
            }
        }
    }
}