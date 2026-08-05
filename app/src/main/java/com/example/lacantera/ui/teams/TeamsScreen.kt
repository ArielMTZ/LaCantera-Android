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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.data.model.TeamItem

private val TeamsBackground = Color(0xFFF4F6FA)
private val TeamsSurface = Color(0xFFFFFFFF)

private val TeamsNavy = Color(0xFF071E4B)
private val TeamsNavyLight = Color(0xFF17468A)
private val TeamsBlue = Color(0xFF2463B6)

private val TeamsText = Color(0xFF111C35)
private val TeamsMuted = Color(0xFF68748A)
private val TeamsBorder = Color(0xFFDDE4EE)

private val TeamsBlueSoft = Color(0xFFE9F2FF)
private val TeamsGreen = Color(0xFF168052)
private val TeamsGreenSoft = Color(0xFFE8F7EF)
private val TeamsRed = Color(0xFFC62828)
private val TeamsRedSoft = Color(0xFFFFE9EA)

@Composable
fun TeamsScreen(
    onBackClick: () -> Unit,
    onTeamClick: (Int) -> Unit,
    refreshRequested: Boolean,
    onRefreshConsumed: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: TeamsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(refreshRequested) {
        if (refreshRequested) {
            viewModel.loadTeams()
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
            .background(TeamsBackground)
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
            TeamsTopBar(
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
                        TeamsLoadingState()
                    }

                    uiState.errorMessage != null -> {
                        TeamsErrorState(
                            message = uiState.errorMessage
                                ?: "No fue posible cargar los equipos.",
                            onRetry = viewModel::loadTeams,
                            horizontalPadding = horizontalPadding
                        )
                    }

                    uiState.equipos.isEmpty() -> {
                        TeamsEmptyState(
                            horizontalPadding = horizontalPadding
                        )
                    }

                    else -> {
                        val activeTeams = uiState.equipos.count {
                            it.activo
                        }

                        val inactiveTeams =
                            uiState.equipos.size - activeTeams

                        TeamsListContent(
                            teams = uiState.equipos,
                            totalTeams = uiState.count,
                            activeTeams = activeTeams,
                            inactiveTeams = inactiveTeams,
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

@Composable
private fun TeamsTopBar(
    onBackClick: () -> Unit,
    compactScreen: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(TeamsSurface)
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
            color = TeamsBackground,
            border = BorderStroke(
                width = 1.dp,
                color = TeamsBorder
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
                    color = TeamsNavy,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Volver",
                    color = TeamsNavy,
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
                text = "Equipos",
                color = TeamsText,
                fontSize = if (compactScreen) {
                    18.sp
                } else {
                    20.sp
                },
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Gestión deportiva",
                color = TeamsMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp),
            shape = RoundedCornerShape(14.dp),
            color = TeamsNavy
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "LC",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun TeamsListContent(
    teams: List<TeamItem>,
    totalTeams: Int,
    activeTeams: Int,
    inactiveTeams: Int,
    horizontalPadding: androidx.compose.ui.unit.Dp,
    compactScreen: Boolean,
    onTeamClick: (Int) -> Unit
) {
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
            TeamsSummaryCard(
                totalTeams = totalTeams,
                activeTeams = activeTeams,
                inactiveTeams = inactiveTeams,
                compactScreen = compactScreen
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Directorio de equipos",
                        color = TeamsText,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Selecciona un equipo para consultar o editar su información.",
                        color = TeamsMuted,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = TeamsBlueSoft
                ) {
                    Text(
                        text = "${teams.size} equipos",
                        modifier = Modifier.padding(
                            horizontal = 11.dp,
                            vertical = 6.dp
                        ),
                        color = TeamsBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))
        }

        items(
            items = teams,
            key = { team ->
                team.id
            }
        ) { team ->
            ProfessionalTeamCard(
                team = team,
                onClick = {
                    onTeamClick(team.id)
                }
            )
        }
    }
}

@Composable
private fun TeamsSummaryCard(
    totalTeams: Int,
    activeTeams: Int,
    inactiveTeams: Int,
    compactScreen: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = TeamsNavy
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            TeamsNavy,
                            TeamsNavyLight,
                            TeamsBlue
                        )
                    )
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                )
        ) {
            if (compactScreen) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TeamsSummaryTitle(
                        totalTeams = totalTeams
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {
                        TeamStatusSummary(
                            value = activeTeams,
                            label = "Activos",
                            dotColor = Color(0xFF65E5A4),
                            modifier = Modifier.weight(1f)
                        )

                        TeamStatusSummary(
                            value = inactiveTeams,
                            label = "Inactivos",
                            dotColor = Color(0xFFFF8888),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TeamsSummaryTitle(
                        totalTeams = totalTeams,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        TeamStatusSummary(
                            value = activeTeams,
                            label = "Activos",
                            dotColor = Color(0xFF65E5A4)
                        )

                        TeamStatusSummary(
                            value = inactiveTeams,
                            label = "Inactivos",
                            dotColor = Color(0xFFFF8888)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamsSummaryTitle(
    totalTeams: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "EQUIPOS REGISTRADOS",
            color = Color.White.copy(alpha = 0.65f),
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = totalTeams.toString(),
                color = Color.White,
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (totalTeams == 1) {
                    "equipo"
                } else {
                    "equipos"
                },
                modifier = Modifier.padding(bottom = 6.dp),
                color = Color.White.copy(alpha = 0.76f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Información conectada con La Cantera",
            color = Color.White.copy(alpha = 0.69f),
            fontSize = 11.sp
        )
    }
}

@Composable
private fun TeamStatusSummary(
    value: Int,
    label: String,
    dotColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.12f),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.16f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 9.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = dotColor,
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = value.toString(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = label,
                color = Color.White.copy(alpha = 0.76f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProfessionalTeamCard(
    team: TeamItem,
    onClick: () -> Unit
) {
    val accentColor = if (team.activo) {
        TeamsGreen
    } else {
        TeamsRed
    }

    val accentBackground = if (team.activo) {
        TeamsGreenSoft
    } else {
        TeamsRedSoft
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(
            containerColor = TeamsSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = TeamsBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = accentBackground
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        TeamShieldIcon(
                            tint = accentColor,
                            modifier = Modifier.size(29.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(13.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = team.nombre,
                        color = TeamsText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${team.deporte} · ${team.categoria}",
                        color = TeamsMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(9.dp))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = accentBackground
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
                                    color = accentColor,
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = if (team.activo) {
                                "ACTIVO"
                            } else {
                                "INACTIVO"
                            },
                            color = accentColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.4.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(TeamsBorder)
            )

            Spacer(modifier = Modifier.height(13.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamInfoChip(
                    label = "Deporte",
                    value = team.deporte,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                TeamInfoChip(
                    label = "Categoría",
                    value = team.categoria,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = CircleShape,
                    color = TeamsBlueSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "›",
                            color = TeamsBlue,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamInfoChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = TeamsBackground
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 9.dp
            )
        ) {
            Text(
                text = label.uppercase(),
                color = TeamsMuted,
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = value.ifBlank {
                    "Sin información"
                },
                color = TeamsText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TeamShieldIcon(
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

@Composable
private fun TeamsLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(68.dp),
                shape = RoundedCornerShape(22.dp),
                color = TeamsNavy
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    TeamShieldIcon(
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            CircularProgressIndicator(
                color = TeamsNavy,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cargando equipos...",
                color = TeamsMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TeamsErrorState(
    message: String,
    onRetry: () -> Unit,
    horizontalPadding: androidx.compose.ui.unit.Dp
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
                containerColor = TeamsSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = TeamsBorder
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(19.dp),
                    color = TeamsRedSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            color = TeamsRed,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No se pudieron cargar los equipos",
                    color = TeamsText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = message,
                    color = TeamsMuted,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
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
                        containerColor = TeamsNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Reintentar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamsEmptyState(
    horizontalPadding: androidx.compose.ui.unit.Dp
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
                containerColor = TeamsSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = TeamsBorder
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(68.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = TeamsBlueSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        TeamShieldIcon(
                            tint = TeamsBlue,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sin equipos registrados",
                    color = TeamsText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "Cuando existan equipos registrados aparecerán en este apartado.",
                    color = TeamsMuted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}