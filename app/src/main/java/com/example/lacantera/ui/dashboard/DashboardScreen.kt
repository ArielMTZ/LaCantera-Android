package com.example.lacantera.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.logoutCompleted) {
        if (uiState.logoutCompleted) {
            viewModel.consumeLogout()
            onLogout()
        }
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            viewModel.consumeSessionExpired()
            onSessionExpired()
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val nombreMostrar = uiState.nombreCorto.ifBlank {
        uiState.username
    }

    val nombreRol = when (uiState.tipoUsuario.lowercase()) {
        "superadmin" -> "Superadministrador"
        "staff" -> "Staff"
        "admin_principal" -> "Administrador principal"
        "admin" -> "Administrador"
        "finanzas" -> "Finanzas"
        "arbitro" -> "Árbitro"
        "capitan" -> "Capitán"
        "jugador" -> "Jugador"
        else -> "Sin rol"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Bienvenido a La Cantera",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF071B4A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nombreMostrar,
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Tipo de usuario: $nombreRol",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = viewModel::loadProfile
            ) {
                Text("Reintentar")
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        if (uiState.permisos.verDashboard) {
            DashboardOptionButton("Resumen general")
        }

        if (uiState.permisos.administrarDeportes) {
            DashboardOptionButton("Administrar deportes")
        }

        if (uiState.permisos.administrarEquipos) {
            DashboardOptionButton("Administrar equipos")
        }

        if (uiState.permisos.administrarUsuarios) {
            DashboardOptionButton("Administrar usuarios")
        }

        if (uiState.permisos.verTemporadaActual) {
            DashboardOptionButton("Temporada actual")
        }

        if (uiState.permisos.verHistorialTemporadas) {
            DashboardOptionButton("Historial de temporadas")
        }

        if (uiState.permisos.verInscripciones) {
            DashboardOptionButton("Inscripciones")
        }

        if (uiState.permisos.verArbitrajes) {
            DashboardOptionButton("Arbitrajes")
        }

        if (uiState.permisos.verHistorialInscripciones) {
            DashboardOptionButton("Historial de inscripciones")
        }

        if (uiState.permisos.verMisPartidos) {
            DashboardOptionButton("Mis partidos")
        }

        if (uiState.permisos.verHistorialPartidos) {
            DashboardOptionButton("Historial de partidos")
        }

        if (uiState.permisos.verPanelArbitro) {
            DashboardOptionButton("Panel de árbitro")
        }

        if (uiState.permisos.verMisEquipos) {
            DashboardOptionButton("Mis equipos")
        }

        if (uiState.permisos.verPanelCapitan) {
            DashboardOptionButton("Panel de capitán")
        }

        if (uiState.permisos.verHistorialJuegosCapitan) {
            DashboardOptionButton("Historial de juegos")
        }

        if (uiState.permisos.verHistorialPagosCapitan) {
            DashboardOptionButton("Historial de pagos")
        }

        if (uiState.permisos.verEstadisticas) {
            DashboardOptionButton("Estadísticas")
        }

        if (uiState.permisos.verLogs) {
            DashboardOptionButton("Logs")
        }

        if (uiState.permisos.verUsuariosBloqueados) {
            DashboardOptionButton("Usuarios bloqueados")
        }

        if (uiState.permisos.verSolicitudesRecuperacion) {
            DashboardOptionButton("Solicitudes de recuperación")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = viewModel::logout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DashboardOptionButton(
    title: String
) {
    Button(
        onClick = {
            // Agregaremos navegación en los siguientes pasos.
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(title)
    }
}