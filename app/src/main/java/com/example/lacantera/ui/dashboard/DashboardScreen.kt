package com.example.lacantera.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onSessionExpired: () -> Unit,
    onNavigateToSports: () -> Unit,
    onNavigateToTeams: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onNavigateToSeasons: () -> Unit,
    onNavigateToRefereeMatches: () -> Unit,
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

    val nombreRol = when (
        uiState.tipoUsuario.lowercase()
    ) {
        "superadmin" -> "Superadministrador"
        "staff" -> "Staff"
        "admin_principal" -> "Administrador principal"
        "admin" -> "Administrador"
        "finanzas" -> "Finanzas"
        "arbitro" -> "Árbitro"
        "capitan" -> "Capitán"
        else -> "Sin rol"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6FA)
            )
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Top
    ) {
        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Bienvenido a La Cantera",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF071B4A)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = nombreMostrar,
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Tipo de usuario: $nombreRol",
            fontSize = 15.sp,
            color = MaterialTheme
                .colorScheme
                .onSurfaceVariant
        )

        uiState.errorMessage?.let { message ->
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = message,
                color = MaterialTheme
                    .colorScheme
                    .error,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedButton(
                onClick = viewModel::loadDashboard
            ) {
                Text("Reintentar")
            }
        }

        if (uiState.permisos.verDashboard) {
            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = "Resumen general",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF071B4A)
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            DashboardStatsSection(
                totalEquipos = uiState.totalEquipos,
                totalJugadores =
                    uiState.totalJugadores,
                totalArbitros =
                    uiState.totalArbitros
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "Módulos disponibles",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF071B4A)
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        if (
            uiState.permisos
                .administrarDeportes
        ) {
            DashboardOptionButton(
                title = "Administrar deportes",
                onClick = onNavigateToSports
            )
        }

        if (
            uiState.permisos
                .administrarEquipos
        ) {
            DashboardOptionButton(
                title = "Administrar equipos",
                onClick = onNavigateToTeams
            )
        }

        if (
            uiState.permisos
                .administrarUsuarios
        ) {
            DashboardOptionButton(
                title = "Administrar usuarios",
                onClick = onNavigateToUsers
            )
        }

        if (
            uiState.permisos
                .verTemporadaActual
        ) {
            DashboardOptionButton(
                title = "Temporada actual",
                onClick = onNavigateToSeasons
            )
        }

        if (
            uiState.permisos
                .verHistorialTemporadas
        ) {
            DashboardOptionButton(
                title = "Historial de temporadas"
            )
        }

        if (
            uiState.permisos
                .verInscripciones
        ) {
            DashboardOptionButton(
                title = "Inscripciones"
            )
        }

        if (
            uiState.permisos
                .verArbitrajes
        ) {
            DashboardOptionButton(
                title = "Arbitrajes"
            )
        }

        if (
            uiState.permisos
                .verHistorialInscripciones
        ) {
            DashboardOptionButton(
                title = "Historial de inscripciones"
            )
        }

        if (
            uiState.permisos
                .verMisPartidos
        ) {
            DashboardOptionButton(
                title = "Mis partidos",
                onClick =
                    onNavigateToRefereeMatches
            )
        }

        if (
            uiState.permisos
                .verHistorialPartidos
        ) {
            DashboardOptionButton(
                title = "Historial de partidos",
                onClick =
                    onNavigateToRefereeMatches
            )
        }

        if (
            uiState.permisos
                .verPanelArbitro
        ) {
            DashboardOptionButton(
                title = "Panel de árbitro",
                onClick =
                    onNavigateToRefereeMatches
            )
        }

        if (
            uiState.permisos
                .verMisEquipos
        ) {
            DashboardOptionButton(
                title = "Mis equipos"
            )
        }

        if (
            uiState.permisos
                .verPanelCapitan
        ) {
            DashboardOptionButton(
                title = "Panel de capitán"
            )
        }

        if (
            uiState.permisos
                .verHistorialJuegosCapitan
        ) {
            DashboardOptionButton(
                title = "Historial de juegos"
            )
        }

        if (
            uiState.permisos
                .verHistorialPagosCapitan
        ) {
            DashboardOptionButton(
                title = "Historial de pagos"
            )
        }

        if (
            uiState.permisos
                .verEstadisticas
        ) {
            DashboardOptionButton(
                title = "Estadísticas"
            )
        }

        if (
            uiState.permisos
                .verLogs
        ) {
            DashboardOptionButton(
                title = "Logs"
            )
        }

        if (
            uiState.permisos
                .verUsuariosBloqueados
        ) {
            DashboardOptionButton(
                title = "Usuarios bloqueados"
            )
        }

        if (
            uiState.permisos
                .verSolicitudesRecuperacion
        ) {
            DashboardOptionButton(
                title = "Solicitudes de recuperación"
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedButton(
            onClick = viewModel::logout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
private fun DashboardStatsSection(
    totalEquipos: Int,
    totalJugadores: Int,
    totalArbitros: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {
        DashboardStatCard(
            title = "Equipos",
            value = totalEquipos,
            modifier = Modifier.weight(1f)
        )

        DashboardStatCard(
            title = "Jugadores",
            value = totalJugadores,
            modifier = Modifier.weight(1f)
        )

        DashboardStatCard(
            title = "Árbitros",
            value = totalArbitros,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DashboardStatCard(
    title: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 16.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Text(
                text = value.toString(),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF071B4A)
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme
                    .colorScheme
                    .onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DashboardOptionButton(
    title: String,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 10.dp
            )
    ) {
        Text(title)
    }
}