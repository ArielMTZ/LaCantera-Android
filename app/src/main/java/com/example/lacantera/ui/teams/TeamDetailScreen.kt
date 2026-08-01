package com.example.lacantera.ui.teams

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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

@Composable
fun TeamDetailScreen(
    teamId: Int,
    onBackClick: () -> Unit,
    onUpdateCompleted: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: TeamDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(teamId) {
        viewModel.loadTeam(teamId)
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            viewModel.consumeSessionExpired()
            onSessionExpired()
        }
    }

    LaunchedEffect(uiState.updateCompleted) {
        if (uiState.updateCompleted) {
            viewModel.consumeUpdateCompleted()
            onUpdateCompleted()
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

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top
            ) {
                OutlinedButton(
                    onClick = onBackClick
                ) {
                    Text("Regresar")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Editar equipo",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                uiState.team?.let { team ->
                    Text(
                        text = "Deporte: ${team.deporte}",
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Categoría: ${team.categoria}",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = viewModel::onNombreChanged,
                    label = {
                        Text("Nombre del equipo")
                    },
                    enabled = !uiState.isSaving,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Equipo activo",
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = if (uiState.activo) {
                                "El equipo aparecerá en los listados."
                            } else {
                                "El equipo quedará oculto."
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Switch(
                        checked = uiState.activo,
                        onCheckedChange = viewModel::onActivoChanged,
                        enabled = !uiState.isSaving
                    )
                }

                uiState.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.successMessage?.let { message ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = viewModel::updateTeam,
                    enabled = !uiState.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator()
                    } else {
                        Text("Guardar cambios")
                    }
                }
            }
        }
    }
}