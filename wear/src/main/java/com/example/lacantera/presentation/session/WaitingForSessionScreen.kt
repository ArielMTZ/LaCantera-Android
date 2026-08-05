package com.example.lacantera.presentation.session

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text

@Composable
fun WaitingForSessionScreen() {
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
                        text = "Esperando conexión",
                        color = MaterialTheme
                            .colorScheme
                            .primary
                    )
                }

                item {
                    Text(
                        text = (
                                "Inicia sesión en el teléfono " +
                                        "para vincular el reloj."
                                ),
                        color = MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                    )
                }
            }
        }
    }
}