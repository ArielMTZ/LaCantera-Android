package com.example.lacantera.presentation.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text

@Composable
fun WearHomeScreen(
    nombreCorto: String,
    tipoUsuario: String,
    onLogoutClick: () -> Unit
) {
    AppScaffold {
        val listState =
            rememberTransformingLazyColumnState()

        ScreenScaffold(
            scrollState = listState
        ) { contentPadding ->

            TransformingLazyColumn(
                state = listState,
                contentPadding = contentPadding
            ) {
                item {
                    ListHeader(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "La Cantera"
                        )
                    }
                }

                item {
                    Text(
                        text = nombreCorto.ifBlank {
                            "Usuario"
                        }
                    )
                }

                item {
                    Text(
                        text = userTypeDisplayName(
                            tipoUsuario
                        )
                    )
                }

                item {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Temporada activa"
                        )
                    }
                }

                item {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (
                                tipoUsuario.lowercase()
                            ) {
                                "capitan" ->
                                    "Mis equipos"

                                "arbitro" ->
                                    "Mis partidos"

                                else ->
                                    "Partidos de hoy"
                            }
                        )
                    }
                }

                item {
                    Button(
                        onClick = onLogoutClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cerrar sesión"
                        )
                    }
                }
            }
        }
    }
}

private fun userTypeDisplayName(
    tipoUsuario: String
): String {
    return when (
        tipoUsuario.lowercase()
    ) {
        "superadmin" ->
            "Superadministrador"

        "staff" ->
            "Staff"

        "admin_principal" ->
            "Administrador principal"

        "admin" ->
            "Administrador"

        "capitan" ->
            "Capitán"

        "arbitro" ->
            "Árbitro"

        else ->
            "Sin rol"
    }
}