package com.example.lacantera.ui.teams

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.data.model.CaptainTeamItem

/*
 * =========================================================
 * COLORES
 * =========================================================
 */

private val CaptainTeamsBackground = Color(0xFFF3F6FB)
private val CaptainTeamsSurface = Color(0xFFFFFFFF)

private val CaptainTeamsNavy = Color(0xFF071E4B)
private val CaptainTeamsNavyLight = Color(0xFF153E7C)
private val CaptainTeamsBlue = Color(0xFF2463B6)

private val CaptainTeamsText = Color(0xFF111C35)
private val CaptainTeamsMuted = Color(0xFF68748A)
private val CaptainTeamsBorder = Color(0xFFDCE4EF)

private val CaptainTeamsBlueSoft = Color(0xFFEAF2FF)
private val CaptainTeamsGreen = Color(0xFF168052)
private val CaptainTeamsGreenSoft = Color(0xFFE8F7EF)
private val CaptainTeamsRed = Color(0xFFC62828)
private val CaptainTeamsRedSoft = Color(0xFFFFE9EA)

/*
 * =========================================================
 * PANTALLA PRINCIPAL
 * =========================================================
 */

@Composable
fun CaptainTeamsScreen(
    onBackClick: () -> Unit,
    onTeamClick: (Int) -> Unit,
    refreshRequested: Boolean,
    onRefreshConsumed: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: CaptainTeamsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(refreshRequested) {
        if (refreshRequested) {
            viewModel.loadCaptainTeams()
            onRefreshConsumed()
        }
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            viewModel.consumeSessionExpired()
            onSessionExpired()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(CaptainTeamsBackground)
    ) {
        val compactScreen = maxWidth < 370.dp

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
            CaptainTeamsTopBar(
                onBackClick = onBackClick,
                compactScreen = compactScreen
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    uiState.isLoading -> {
                        CaptainTeamsLoadingState()
                    }

                    uiState.errorMessage != null -> {
                        CaptainTeamsErrorState(
                            message = uiState.errorMessage
                                ?: "No fue posible cargar los equipos.",
                            horizontalPadding = horizontalPadding,
                            onRetry = viewModel::loadCaptainTeams
                        )
                    }

                    uiState.equipos.isEmpty() -> {
                        CaptainTeamsEmptyState(
                            horizontalPadding = horizontalPadding,
                            onRetry = viewModel::loadCaptainTeams
                        )
                    }

                    else -> {
                        CaptainTeamsContent(
                            teams = uiState.equipos,
                            horizontalPadding = horizontalPadding,
                            compactScreen = compactScreen,
                            onTeamClick = onTeamClick
                        )
                    }
                }
            }
        }
    }
}

/*
 * =========================================================
 * BARRA SUPERIOR
 * =========================================================
 */

@Composable
private fun CaptainTeamsTopBar(
    onBackClick: () -> Unit,
    compactScreen: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CaptainTeamsSurface)
            .padding(
                horizontal = if (compactScreen) {
                    14.dp
                } else {
                    18.dp
                },
                vertical = 12.dp
            )
            .height(50.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(onClick = onBackClick),
            shape = RoundedCornerShape(15.dp),
            color = CaptainTeamsBackground,
            border = BorderStroke(
                width = 1.dp,
                color = CaptainTeamsBorder
            )
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 13.dp,
                    vertical = 9.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "‹",
                    color = CaptainTeamsNavy,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Volver",
                    color = CaptainTeamsNavy,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mis equipos",
                color = CaptainTeamsText,
                fontSize = if (compactScreen) {
                    17.sp
                } else {
                    19.sp
                },
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Panel del capitán",
                color = CaptainTeamsMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp),
            shape = RoundedCornerShape(14.dp),
            color = CaptainTeamsNavy
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CaptainTeamsShieldIcon(
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

/*
 * =========================================================
 * CONTENIDO
 * =========================================================
 */

@Composable
private fun CaptainTeamsContent(
    teams: List<CaptainTeamItem>,
    horizontalPadding: Dp,
    compactScreen: Boolean,
    onTeamClick: (Int) -> Unit
) {
    val activeTeams = teams.count { team ->
        team.activo
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = 16.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            CaptainTeamsHeroCard(
                totalTeams = teams.size,
                activeTeams = activeTeams,
                compactScreen = compactScreen
            )
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Equipos asignados",
                        color = CaptainTeamsText,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Selecciona un equipo para consultar su información.",
                        color = CaptainTeamsMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = CaptainTeamsBlueSoft
                ) {
                    Text(
                        text = teams.size.toString(),
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        ),
                        color = CaptainTeamsBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
        }

        items(
            items = teams,
            key = { team ->
                team.id
            }
        ) { team ->
            CaptainTeamCard(
                team = team,
                onClick = {
                    onTeamClick(team.id)
                }
            )
        }

        item {
            CaptainTeamsPermissionNotice()
        }
    }
}

/*
 * =========================================================
 * RESUMEN
 * =========================================================
 */

@Composable
private fun CaptainTeamsHeroCard(
    totalTeams: Int,
    activeTeams: Int,
    compactScreen: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(29.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainTeamsNavy
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
                            CaptainTeamsNavy,
                            CaptainTeamsNavyLight,
                            CaptainTeamsBlue
                        )
                    )
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PANEL DEL CAPITÁN",
                    color = Color.White.copy(alpha = 0.64f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Consulta tus equipos",
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
                    text = "Revisa la información general y el estado de cada equipo.",
                    color = Color.White.copy(alpha = 0.73f),
                    fontSize = 11.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CaptainTeamsMetric(
                        value = totalTeams,
                        label = "Asignados",
                        modifier = Modifier.weight(1f)
                    )

                    CaptainTeamsMetric(
                        value = activeTeams,
                        label = "Activos",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CaptainTeamsMetric(
    value: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.12f),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.17f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 13.dp,
                vertical = 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(34.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.12f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = value.toString(),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(9.dp))

            Text(
                text = label,
                color = Color.White.copy(alpha = 0.78f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/*
 * =========================================================
 * TARJETA DEL EQUIPO
 * =========================================================
 */

@Composable
private fun CaptainTeamCard(
    team: CaptainTeamItem,
    onClick: () -> Unit
) {
    val statusColor = if (team.activo) {
        CaptainTeamsGreen
    } else {
        CaptainTeamsRed
    }

    val statusBackground = if (team.activo) {
        CaptainTeamsGreenSoft
    } else {
        CaptainTeamsRedSoft
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainTeamsSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = CaptainTeamsBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(19.dp),
                color = CaptainTeamsBlueSoft
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = team.nombre
                            .trim()
                            .take(2)
                            .uppercase()
                            .ifBlank {
                                "EQ"
                            },
                        color = CaptainTeamsBlue,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = team.nombre,
                    color = CaptainTeamsText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = team.deporte.ifBlank {
                        "Sin deporte"
                    },
                    color = CaptainTeamsMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(7.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = CaptainTeamsBackground
                    ) {
                        Text(
                            text = team.categoria.ifBlank {
                                "Sin categoría"
                            },
                            modifier = Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 5.dp
                            ),
                            color = CaptainTeamsMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(7.dp))

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = statusBackground
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 5.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = statusColor,
                                        shape = CircleShape
                                    )
                            )

                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                text = if (team.activo) {
                                    "Activo"
                                } else {
                                    "Inactivo"
                                },
                                color = statusColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = CaptainTeamsBlueSoft
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "›",
                        color = CaptainTeamsBlue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/*
 * =========================================================
 * AVISO DE PERMISOS
 * =========================================================
 */

@Composable
private fun CaptainTeamsPermissionNotice() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp),
        shape = RoundedCornerShape(20.dp),
        color = CaptainTeamsBlueSoft,
        border = BorderStroke(
            width = 1.dp,
            color = CaptainTeamsBlue.copy(alpha = 0.13f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 14.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(14.dp),
                color = CaptainTeamsBlue.copy(alpha = 0.12f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "i",
                        color = CaptainTeamsBlue,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(11.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Información de tus equipos",
                    color = CaptainTeamsText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "El estado del equipo solo puede ser modificado por un administrador.",
                    color = CaptainTeamsMuted,
                    fontSize = 10.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

/*
 * =========================================================
 * CARGA
 * =========================================================
 */

@Composable
private fun CaptainTeamsLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(70.dp),
                shape = RoundedCornerShape(23.dp),
                color = CaptainTeamsNavy
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CaptainTeamsShieldIcon(
                        tint = Color.White,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            CircularProgressIndicator(
                color = CaptainTeamsNavy,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cargando equipos...",
                color = CaptainTeamsMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/*
 * =========================================================
 * ERROR
 * =========================================================
 */

@Composable
private fun CaptainTeamsErrorState(
    message: String,
    horizontalPadding: Dp,
    onRetry: () -> Unit
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
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = CaptainTeamsSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = CaptainTeamsBorder
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(62.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = CaptainTeamsRedSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            color = CaptainTeamsRed,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No se pudieron cargar los equipos",
                    color = CaptainTeamsText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = message,
                    color = CaptainTeamsMuted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CaptainTeamsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Reintentar",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

/*
 * =========================================================
 * ESTADO VACÍO
 * =========================================================
 */

@Composable
private fun CaptainTeamsEmptyState(
    horizontalPadding: Dp,
    onRetry: () -> Unit
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
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = CaptainTeamsSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = CaptainTeamsBorder
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(68.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = CaptainTeamsBlueSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CaptainTeamsShieldIcon(
                            tint = CaptainTeamsBlue,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No hay equipos disponibles",
                    color = CaptainTeamsText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "No se encontraron equipos relacionados con tu cuenta.",
                    color = CaptainTeamsMuted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CaptainTeamsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Actualizar",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

/*
 * =========================================================
 * ICONO
 * =========================================================
 */

@Composable
private fun CaptainTeamsShieldIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.075f

        val stroke = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        val shield = Path().apply {
            moveTo(
                size.width * 0.50f,
                size.height * 0.07f
            )

            lineTo(
                size.width * 0.83f,
                size.height * 0.21f
            )

            lineTo(
                size.width * 0.77f,
                size.height * 0.61f
            )

            quadraticBezierTo(
                size.width * 0.70f,
                size.height * 0.82f,
                size.width * 0.50f,
                size.height * 0.94f
            )

            quadraticBezierTo(
                size.width * 0.30f,
                size.height * 0.82f,
                size.width * 0.23f,
                size.height * 0.61f
            )

            lineTo(
                size.width * 0.17f,
                size.height * 0.21f
            )

            close()
        }

        drawPath(
            path = shield,
            color = tint,
            style = stroke
        )

        drawCircle(
            color = tint,
            radius = size.width * 0.10f,
            center = Offset(
                x = size.width * 0.50f,
                y = size.height * 0.46f
            ),
            style = stroke
        )

        drawLine(
            color = tint,
            start = Offset(
                x = size.width * 0.34f,
                y = size.height * 0.67f
            ),
            end = Offset(
                x = size.width * 0.66f,
                y = size.height * 0.67f
            ),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}