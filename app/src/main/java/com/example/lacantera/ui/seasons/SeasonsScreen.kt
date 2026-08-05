package com.example.lacantera.ui.seasons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.data.model.SeasonItem
import com.example.lacantera.data.model.SportItem

/*
 * =========================================================
 * COLORES
 * =========================================================
 */

private val SeasonsBackground = Color(0xFFF3F6FB)
private val SeasonsSurface = Color(0xFFFFFFFF)

private val SeasonsNavy = Color(0xFF071E4B)
private val SeasonsNavyLight = Color(0xFF16468C)
private val SeasonsBlue = Color(0xFF2767B9)

private val SeasonsText = Color(0xFF111C35)
private val SeasonsMuted = Color(0xFF68748A)
private val SeasonsBorder = Color(0xFFDCE4EF)

private val SeasonsBlueSoft = Color(0xFFEAF2FF)

private val SeasonsGreen = Color(0xFF148456)
private val SeasonsGreenSoft = Color(0xFFE7F7EF)

private val SeasonsOrange = Color(0xFFB86700)
private val SeasonsOrangeSoft = Color(0xFFFFF1DE)

private val SeasonsRed = Color(0xFFC62828)
private val SeasonsRedSoft = Color(0xFFFFEBEC)

/*
 * =========================================================
 * PANTALLA PRINCIPAL
 * =========================================================
 */

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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SeasonsBackground)
    ) {
        val compactScreen = maxWidth < 380.dp

        val horizontalPadding = if (compactScreen) {
            14.dp
        } else {
            18.dp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            SeasonsHeader(
                compactScreen = compactScreen,
                onBackClick = onBackClick,
                onCreateClick = viewModel::showCreateDialog
            )

            uiState.successMessage?.let { message ->
                SeasonMessageBanner(
                    message = message,
                    success = true,
                    horizontalPadding = horizontalPadding
                )
            }

            uiState.errorMessage?.let { message ->
                SeasonMessageBanner(
                    message = message,
                    success = false,
                    horizontalPadding = horizontalPadding
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    uiState.isLoading -> {
                        SeasonsLoadingState()
                    }

                    uiState.seasons.isEmpty() -> {
                        EmptySeasonsContent(
                            horizontalPadding = horizontalPadding,
                            onCreateClick = viewModel::showCreateDialog
                        )
                    }

                    else -> {
                        ActiveSeasonsList(
                            seasons = uiState.seasons,
                            compactScreen = compactScreen,
                            horizontalPadding = horizontalPadding,
                            onFinalizeClick = viewModel::requestFinalize
                        )
                    }
                }
            }
        }
    }

    if (uiState.showCreateDialog) {
        CreateSeasonDialog(
            sports = uiState.sports,
            seasonName = uiState.seasonName,
            selectedSportId = uiState.selectedSportId,
            selectedSportName = uiState.selectedSportName,
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            isSaving = uiState.isSaving,
            onNameChanged = viewModel::onNameChanged,
            onSportSelected = viewModel::onSportSelected,
            onStartDateChanged = viewModel::onStartDateChanged,
            onEndDateChanged = viewModel::onEndDateChanged,
            onDismiss = viewModel::hideCreateDialog,
            onConfirm = viewModel::createSeason
        )
    }

    uiState.seasonToFinalize?.let { season ->
        FinalizeSeasonDialog(
            season = season,
            isFinalizing = uiState.isFinalizing,
            onDismiss = viewModel::cancelFinalize,
            onConfirm = viewModel::finalizeSeason
        )
    }
}

/*
 * =========================================================
 * ENCABEZADO
 * =========================================================
 */

@Composable
private fun SeasonsHeader(
    compactScreen: Boolean,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SeasonsSurface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (compactScreen) {
                        12.dp
                    } else {
                        16.dp
                    },
                    vertical = 11.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.clickable(
                    onClick = onBackClick
                ),
                shape = RoundedCornerShape(14.dp),
                color = SeasonsBackground,
                border = BorderStroke(
                    width = 1.dp,
                    color = SeasonsBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = if (compactScreen) {
                            10.dp
                        } else {
                            12.dp
                        },
                        vertical = 9.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "‹",
                        color = SeasonsNavy,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    if (!compactScreen) {
                        Spacer(modifier = Modifier.width(3.dp))

                        Text(
                            text = "Volver",
                            color = SeasonsNavy,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Temporadas",
                    color = SeasonsText,
                    fontSize = if (compactScreen) {
                        18.sp
                    } else {
                        20.sp
                    },
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Administración deportiva",
                    color = SeasonsMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Button(
                onClick = onCreateClick,
                modifier = Modifier
                    .height(42.dp)
                    .widthIn(
                        min = if (compactScreen) {
                            75.dp
                        } else {
                            88.dp
                        }
                    ),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(
                    horizontal = if (compactScreen) {
                        10.dp
                    } else {
                        13.dp
                    },
                    vertical = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SeasonsNavy,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "＋ Nueva",
                    fontSize = if (compactScreen) {
                        10.sp
                    } else {
                        11.sp
                    },
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
            }
        }
    }
}

/*
 * =========================================================
 * MENSAJES
 * =========================================================
 */

@Composable
private fun SeasonMessageBanner(
    message: String,
    success: Boolean,
    horizontalPadding: Dp
) {
    val backgroundColor = if (success) {
        SeasonsGreenSoft
    } else {
        SeasonsRedSoft
    }

    val contentColor = if (success) {
        SeasonsGreen
    } else {
        SeasonsRed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = horizontalPadding,
                vertical = 8.dp
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = contentColor.copy(alpha = 0.16f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(30.dp),
                shape = CircleShape,
                color = contentColor.copy(alpha = 0.12f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (success) {
                            "✓"
                        } else {
                            "!"
                        },
                        color = contentColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = message,
                modifier = Modifier.weight(1f),
                color = contentColor,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/*
 * =========================================================
 * LISTA DE TEMPORADAS
 * =========================================================
 */

@Composable
private fun ActiveSeasonsList(
    seasons: List<SeasonItem>,
    compactScreen: Boolean,
    horizontalPadding: Dp,
    onFinalizeClick: (SeasonItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 16.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SeasonsSummaryCard(
                activeSeasons = seasons.size,
                compactScreen = compactScreen
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 5.dp,
                        bottom = 1.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Temporada actual",
                        color = SeasonsText,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Información del ciclo deportivo activo.",
                        color = SeasonsMuted,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = SeasonsGreenSoft
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 7.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = SeasonsGreen,
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = seasons.size.toString(),
                            color = SeasonsGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        items(
            items = seasons,
            key = { season ->
                season.id
            }
        ) { season ->
            SeasonCard(
                season = season,
                compactScreen = compactScreen,
                onFinalizeClick = {
                    onFinalizeClick(season)
                }
            )
        }
    }
}

/*
 * =========================================================
 * TARJETA DE RESUMEN
 * =========================================================
 */

@Composable
private fun SeasonsSummaryCard(
    activeSeasons: Int,
    compactScreen: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(27.dp),
        colors = CardDefaults.cardColors(
            containerColor = SeasonsNavy
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            SeasonsNavy,
                            SeasonsNavyLight,
                            SeasonsBlue
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 10.dp,
                        end = 13.dp
                    )
                    .size(82.dp)
                    .background(
                        color = Color.White.copy(
                            alpha = 0.05f
                        ),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    )
            ) {
                Text(
                    text = "CONTROL DE TEMPORADAS",
                    color = Color.White.copy(alpha = 0.64f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "Gestión del ciclo deportivo",
                    color = Color.White,
                    fontSize = if (compactScreen) {
                        21.sp
                    } else {
                        24.sp
                    },
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = (
                            "Consulta las fechas, el deporte y las " +
                                    "categorías asociadas."
                            ),
                    color = Color.White.copy(alpha = 0.74f),
                    fontSize = 11.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Surface(
                    shape = RoundedCornerShape(17.dp),
                    color = Color.White.copy(alpha = 0.12f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.16f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 11.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(35.dp),
                            shape = RoundedCornerShape(11.dp),
                            color = Color.White.copy(alpha = 0.13f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activeSeasons.toString(),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Temporadas activas",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Text(
                                text = "Ciclo disponible actualmente",
                                color = Color.White.copy(alpha = 0.66f),
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/*
 * =========================================================
 * TARJETA DE TEMPORADA
 * =========================================================
 */

@Composable
private fun SeasonCard(
    season: SeasonItem,
    compactScreen: Boolean,
    onFinalizeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SeasonsSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = SeasonsBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (compactScreen) {
                        15.dp
                    } else {
                        18.dp
                    },
                    vertical = 17.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = SeasonsBlueSoft
                ) {
                    Text(
                        text = "TEMPORADA ACTUAL",
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        color = SeasonsBlue,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = SeasonsGreenSoft
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = SeasonsGreen,
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = season.statusDisplay,
                            color = SeasonsGreen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = season.nombre,
                color = SeasonsText,
                fontSize = if (compactScreen) {
                    21.sp
                } else {
                    23.sp
                },
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SeasonsBlueSoft
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 11.dp,
                        vertical = 7.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "D",
                        color = SeasonsBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = season.sportName,
                        color = SeasonsNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SeasonInformationTile(
                    label = "FECHA DE INICIO",
                    value = season.fechaInicio,
                    symbol = "I",
                    symbolColor = SeasonsBlue,
                    symbolBackground = SeasonsBlueSoft,
                    modifier = Modifier.weight(1f)
                )

                SeasonInformationTile(
                    label = "FECHA FINAL",
                    value = season.fechaFin,
                    symbol = "F",
                    symbolColor = SeasonsOrange,
                    symbolBackground = SeasonsOrangeSoft,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = SeasonsBackground,
                border = BorderStroke(
                    width = 1.dp,
                    color = SeasonsBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 13.dp,
                        vertical = 13.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(38.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = SeasonsGreenSoft
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "C",
                                color = SeasonsGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(11.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "CATEGORÍAS ASOCIADAS",
                            color = SeasonsMuted,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.4.sp
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = if (season.categoryCount == 1) {
                                "1 categoría registrada"
                            } else {
                                "${season.categoryCount} categorías registradas"
                            },
                            color = SeasonsText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = SeasonsSurface
                    ) {
                        Text(
                            text = season.categoryCount.toString(),
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 6.dp
                            ),
                            color = SeasonsGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(17.dp))

            OutlinedButton(
                onClick = onFinalizeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = SeasonsRed.copy(alpha = 0.48f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SeasonsRed
                )
            ) {
                Text(
                    text = "Finalizar temporada",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = (
                        "Al finalizarla dejará de aparecer como " +
                                "temporada activa."
                        ),
                modifier = Modifier.fillMaxWidth(),
                color = SeasonsMuted,
                fontSize = 9.sp,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/*
 * =========================================================
 * BLOQUE DE INFORMACIÓN
 * =========================================================
 */

@Composable
private fun SeasonInformationTile(
    label: String,
    value: String,
    symbol: String,
    symbolColor: Color,
    symbolBackground: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = SeasonsBackground,
        border = BorderStroke(
            width = 1.dp,
            color = SeasonsBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 12.dp
            )
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(10.dp),
                color = symbolBackground
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = symbol,
                        color = symbolColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(9.dp))

            Text(
                text = label,
                color = SeasonsMuted,
                fontSize = 7.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.3.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = SeasonsText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/*
 * =========================================================
 * ESTADO VACÍO
 * =========================================================
 */

@Composable
private fun EmptySeasonsContent(
    horizontalPadding: Dp,
    onCreateClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 460.dp),
            shape = RoundedCornerShape(27.dp),
            colors = CardDefaults.cardColors(
                containerColor = SeasonsSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = SeasonsBorder
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(23.dp),
                    color = SeasonsBlueSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "＋",
                            color = SeasonsBlue,
                            fontSize = 29.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "No hay temporadas activas",
                    color = SeasonsText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = (
                            "Crea una temporada y selecciona el " +
                                    "deporte al que pertenecerá."
                            ),
                    color = SeasonsMuted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onCreateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SeasonsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Crear nueva temporada",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

/*
 * =========================================================
 * ESTADO DE CARGA
 * =========================================================
 */

@Composable
private fun SeasonsLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(66.dp),
                shape = RoundedCornerShape(22.dp),
                color = SeasonsNavy
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "T",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(17.dp))

            CircularProgressIndicator(
                color = SeasonsNavy,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cargando temporadas...",
                color = SeasonsMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/*
 * =========================================================
 * DIÁLOGO PARA CREAR
 * =========================================================
 */

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

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = SeasonsBlue,
        unfocusedBorderColor = SeasonsBorder,
        focusedLabelColor = SeasonsBlue,
        unfocusedLabelColor = SeasonsMuted,
        cursorColor = SeasonsBlue,
        focusedContainerColor = SeasonsBackground,
        unfocusedContainerColor = SeasonsBackground
    )

    AlertDialog(
        onDismissRequest = {
            if (!isSaving) {
                onDismiss()
            }
        },
        shape = RoundedCornerShape(26.dp),
        containerColor = SeasonsSurface,
        title = {
            Column {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = SeasonsBlueSoft
                ) {
                    Text(
                        text = "NUEVA TEMPORADA",
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        color = SeasonsBlue,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Crear temporada",
                    color = SeasonsText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Completa la información del ciclo deportivo.",
                    color = SeasonsMuted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
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
                    enabled = !isSaving,
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(13.dp))

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
                        trailingIcon = {
                            Text(
                                text = "⌄",
                                color = SeasonsMuted,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        enabled = !isSaving,
                        shape = RoundedCornerShape(15.dp),
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (!isSaving) {
                                    sportMenuExpanded = true
                                }
                            }
                    )

                    DropdownMenu(
                        expanded = sportMenuExpanded,
                        onDismissRequest = {
                            sportMenuExpanded = false
                        },
                        modifier = Modifier
                            .background(SeasonsSurface)
                    ) {
                        if (sports.isEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "No hay deportes disponibles",
                                        color = SeasonsMuted,
                                        fontSize = 12.sp
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
                                    Text(
                                        text = sport.nombre,
                                        color = SeasonsText,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
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
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(13.dp),
                        color = SeasonsGreenSoft
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 11.dp,
                                vertical = 9.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✓",
                                color = SeasonsGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(modifier = Modifier.width(7.dp))

                            Text(
                                text = (
                                        "Deporte seleccionado: " +
                                                selectedSportName
                                        ),
                                color = SeasonsGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(13.dp))

                OutlinedTextField(
                    value = startDate,
                    onValueChange = onStartDateChanged,
                    label = {
                        Text("Fecha de inicio")
                    },
                    placeholder = {
                        Text("AAAA-MM-DD")
                    },
                    enabled = !isSaving,
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(13.dp))

                OutlinedTextField(
                    value = endDate,
                    onValueChange = onEndDateChanged,
                    label = {
                        Text("Fecha final")
                    },
                    placeholder = {
                        Text("AAAA-MM-DD")
                    },
                    enabled = !isSaving,
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isSaving,
                modifier = Modifier.height(45.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SeasonsNavy,
                    contentColor = Color.White
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Creando...",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                } else {
                    Text(
                        text = "Crear temporada",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isSaving,
                modifier = Modifier.height(45.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = SeasonsBorder
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SeasonsMuted
                )
            ) {
                Text(
                    text = "Cancelar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

/*
 * =========================================================
 * DIÁLOGO PARA FINALIZAR
 * =========================================================
 */

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
        shape = RoundedCornerShape(26.dp),
        containerColor = SeasonsSurface,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = SeasonsRedSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            color = SeasonsRed,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Finalizar temporada",
                    color = SeasonsText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column {
                Text(
                    text = (
                            "¿Seguro que deseas finalizar " +
                                    "\"${season.nombre}\"?"
                            ),
                    modifier = Modifier.fillMaxWidth(),
                    color = SeasonsText,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp),
                    color = SeasonsBackground,
                    border = BorderStroke(
                        width = 1.dp,
                        color = SeasonsBorder
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Text(
                            text = "TEMPORADA",
                            color = SeasonsMuted,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.4.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = season.nombre,
                            color = SeasonsText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(9.dp))

                        Text(
                            text = "Deporte: ${season.sportName}",
                            color = SeasonsMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(13.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    color = SeasonsRedSoft
                ) {
                    Text(
                        text = (
                                "Después de finalizarla dejará de " +
                                        "aparecer como temporada activa."
                                ),
                        modifier = Modifier.padding(12.dp),
                        color = SeasonsRed,
                        fontSize = 10.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isFinalizing,
                modifier = Modifier.height(45.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SeasonsRed,
                    contentColor = Color.White
                )
            ) {
                if (isFinalizing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Finalizando...",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                } else {
                    Text(
                        text = "Sí, finalizar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isFinalizing,
                modifier = Modifier.height(45.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = SeasonsBorder
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SeasonsMuted
                )
            ) {
                Text(
                    text = "Cancelar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}