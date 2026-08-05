package com.example.lacantera.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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

private val CaptainBackground = Color(0xFFF3F6FA)
private val CaptainSurface = Color(0xFFFFFFFF)

private val CaptainNavy = Color(0xFF071E4B)
private val CaptainNavyLight = Color(0xFF123B78)
private val CaptainBlue = Color(0xFF1D5DAC)
private val CaptainGreen = Color(0xFF168052)
private val CaptainRed = Color(0xFFD3272E)

private val CaptainText = Color(0xFF111C35)
private val CaptainMuted = Color(0xFF68748B)
private val CaptainBorder = Color(0xFFDCE3EE)

private val CaptainBlueSoft = Color(0xFFE9F2FF)
private val CaptainGreenSoft = Color(0xFFE8F7EF)
private val CaptainRedSoft = Color(0xFFFFE9EA)
private val CaptainOrangeSoft = Color(0xFFFFF1DF)

private enum class CaptainGlyph {
    TEAM,
    CALENDAR,
    SEASON
}

private data class CaptainAction(
    val title: String,
    val description: String,
    val caption: String,
    val glyph: CaptainGlyph,
    val accentColor: Color,
    val backgroundColor: Color,
    val onClick: () -> Unit
)

@Composable
fun CaptainDashboardScreen(
    onLogout: () -> Unit,
    onSessionExpired: () -> Unit,
    onNavigateToMyTeam: () -> Unit,
    onNavigateToMatchdays: () -> Unit,
    onNavigateToSeason: () -> Unit,
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
        CaptainLoadingScreen()
        return
    }

    val displayName = uiState.nombreCorto.ifBlank {
        uiState.username.ifBlank {
            "Capitán"
        }
    }

    val canOpenTeam =
        uiState.permisos.verMisEquipos ||
                uiState.permisos.verPanelCapitan

    val canOpenMatchdays =
        uiState.permisos.verMisPartidos ||
                uiState.permisos.verHistorialJuegosCapitan ||
                uiState.permisos.verPanelCapitan

    val canOpenSeason =
        uiState.permisos.verTemporadaActual

    val teamAction = if (canOpenTeam) {
        CaptainAction(
            title = "Mi equipo",
            description = (
                    "Consulta la información disponible, " +
                            "integrantes y configuración de tu equipo."
                    ),
            caption = "Administración del equipo",
            glyph = CaptainGlyph.TEAM,
            accentColor = CaptainGreen,
            backgroundColor = CaptainGreenSoft,
            onClick = onNavigateToMyTeam
        )
    } else {
        null
    }

    val secondaryActions = buildList {
        if (canOpenMatchdays) {
            add(
                CaptainAction(
                    title = "Jornadas",
                    description = (
                            "Consulta la programación de partidos " +
                                    "y las jornadas disponibles."
                            ),
                    caption = "Calendario deportivo",
                    glyph = CaptainGlyph.CALENDAR,
                    accentColor = CaptainBlue,
                    backgroundColor = CaptainBlueSoft,
                    onClick = onNavigateToMatchdays
                )
            )
        }

        if (canOpenSeason) {
            add(
                CaptainAction(
                    title = "Temporada",
                    description = (
                            "Revisa la temporada activa, " +
                                    "sus fechas y categorías."
                            ),
                    caption = "Competencia actual",
                    glyph = CaptainGlyph.SEASON,
                    accentColor = CaptainRed,
                    backgroundColor = CaptainRedSoft,
                    onClick = onNavigateToSeason
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CaptainBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 18.dp,
                    vertical = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 760.dp)
            ) {
                CaptainHeader(
                    displayName = displayName,
                    username = uiState.username
                )

                uiState.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(14.dp))

                    CaptainErrorCard(
                        message = message,
                        onRetry = viewModel::loadDashboard
                    )
                }

                if (uiState.permisos.verDashboard) {
                    Spacer(modifier = Modifier.height(24.dp))

                    CaptainSectionHeader(
                        title = "La liga",
                        description = "Información general disponible"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CaptainLeagueSummary(
                        totalEquipos = uiState.totalEquipos,
                        totalJugadores = uiState.totalJugadores,
                        totalArbitros = uiState.totalArbitros
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                CaptainSectionHeader(
                    title = "Panel del capitán",
                    description = (
                            "Gestiona tu participación " +
                                    "y consulta la competencia"
                            )
                )

                Spacer(modifier = Modifier.height(13.dp))

                if (
                    teamAction == null &&
                    secondaryActions.isEmpty()
                ) {
                    CaptainEmptyState()
                } else {
                    teamAction?.let { action ->
                        CaptainPrimaryTeamCard(
                            action = action
                        )

                        if (secondaryActions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(13.dp))
                        }
                    }

                    CaptainSecondaryActions(
                        actions = secondaryActions
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                CaptainAccountCard(
                    username = uiState.username,
                    onLogout = viewModel::logout
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun CaptainHeader(
    displayName: String,
    username: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainNavy
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
                            CaptainNavy,
                            CaptainNavyLight,
                            CaptainBlue
                        )
                    )
                )
        ) {
            CaptainFieldDecoration(
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 21.dp,
                        vertical = 22.dp
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(61.dp),
                        shape = CircleShape,
                        color = Color.White.copy(
                            alpha = 0.14f
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.White.copy(
                                alpha = 0.23f
                            )
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = captainInitials(displayName),
                                color = Color.White,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = captainGreeting(),
                            color = Color.White.copy(
                                alpha = 0.72f
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = displayName,
                            color = Color.White,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Capitán de equipo",
                            color = Color.White.copy(
                                alpha = 0.78f
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Surface(
                        modifier = Modifier.size(47.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(
                            alpha = 0.13f
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            CaptainGlyphIcon(
                                glyph = CaptainGlyph.TEAM,
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = Color(0xFF6CE6A8),
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = if (username.isBlank()) {
                            "Acceso de capitán activo"
                        } else {
                            "Sesión activa · @$username"
                        },
                        color = Color.White.copy(
                            alpha = 0.78f
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun CaptainFieldDecoration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val lineColor = Color.White.copy(
            alpha = 0.055f
        )

        drawLine(
            color = lineColor,
            start = Offset(
                x = size.width * 0.72f,
                y = 0f
            ),
            end = Offset(
                x = size.width * 0.72f,
                y = size.height
            ),
            strokeWidth = 2f
        )

        drawCircle(
            color = lineColor,
            radius = size.height * 0.31f,
            center = Offset(
                x = size.width * 0.72f,
                y = size.height * 0.5f
            ),
            style = Stroke(
                width = 2f
            )
        )

        drawRoundRect(
            color = lineColor,
            topLeft = Offset(
                x = size.width * 0.84f,
                y = size.height * 0.17f
            ),
            size = Size(
                width = size.width * 0.24f,
                height = size.height * 0.66f
            ),
            cornerRadius = CornerRadius(
                x = 14f,
                y = 14f
            ),
            style = Stroke(
                width = 2f
            )
        )
    }
}

@Composable
private fun CaptainSectionHeader(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = CaptainText,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = description,
            color = CaptainMuted,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun CaptainLeagueSummary(
    totalEquipos: Int,
    totalJugadores: Int,
    totalArbitros: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = CaptainBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 18.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(42.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = CaptainBlueSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CaptainGlyphIcon(
                            glyph = CaptainGlyph.TEAM,
                            tint = CaptainBlue,
                            modifier = Modifier.size(23.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(11.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Competencia",
                        color = CaptainText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Registros generales de la liga",
                        color = CaptainMuted,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = CaptainGreenSoft
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = CaptainGreen,
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "ACTIVA",
                            color = CaptainGreen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(19.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CaptainSummaryValue(
                    value = totalEquipos,
                    label = "Equipos",
                    modifier = Modifier.weight(1f)
                )

                CaptainSummaryDivider()

                CaptainSummaryValue(
                    value = totalJugadores,
                    label = "Jugadores",
                    modifier = Modifier.weight(1f)
                )

                CaptainSummaryDivider()

                CaptainSummaryValue(
                    value = totalArbitros,
                    label = "Árbitros",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CaptainSummaryValue(
    value: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.toString(),
            color = CaptainText,
            fontSize = 27.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            color = CaptainMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CaptainSummaryDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(44.dp)
            .background(CaptainBorder)
    )
}

@Composable
private fun CaptainPrimaryTeamCard(
    action: CaptainAction
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = action.onClick
            ),
        shape = RoundedCornerShape(27.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainNavy
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            CaptainNavy,
                            CaptainNavyLight
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(63.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(
                        alpha = 0.13f
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(
                            alpha = 0.18f
                        )
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CaptainGlyphIcon(
                            glyph = action.glyph,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = action.caption.uppercase(),
                        color = Color.White.copy(
                            alpha = 0.62f
                        ),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.7.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = action.title,
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = action.description,
                        color = Color.White.copy(
                            alpha = 0.72f
                        ),
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(
                        alpha = 0.13f
                    )
                ) {
                    Text(
                        text = "›",
                        modifier = Modifier.padding(
                            horizontal = 13.dp,
                            vertical = 7.dp
                        ),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CaptainSecondaryActions(
    actions: List<CaptainAction>
) {
    if (actions.isEmpty()) {
        return
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val useSingleColumn = maxWidth < 360.dp

        if (useSingleColumn) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                actions.forEach { action ->
                    CaptainActionCard(
                        action = action,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                actions.forEach { action ->
                    CaptainActionCard(
                        action = action,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (actions.size == 1) {
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CaptainActionCard(
    action: CaptainAction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(181.dp)
            .clickable(
                onClick = action.onClick
            ),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = CaptainBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(15.dp),
                    color = action.backgroundColor
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CaptainGlyphIcon(
                            glyph = action.glyph,
                            tint = action.accentColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = CircleShape,
                    color = action.backgroundColor
                ) {
                    Text(
                        text = "›",
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 3.dp
                        ),
                        color = action.accentColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column {
                Text(
                    text = action.title,
                    color = CaptainText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = action.description,
                    color = CaptainMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = action.caption,
                    color = action.accentColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CaptainAccountCard(
    username: String,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = CaptainBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Cuenta del capitán",
                color = CaptainText,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (username.isBlank()) {
                    "Sesión de capitán activa"
                } else {
                    "Sesión iniciada como @$username"
                },
                color = CaptainMuted,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = CaptainRed.copy(
                        alpha = 0.36f
                    )
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = CaptainRed
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun CaptainGlyphIcon(
    glyph: CaptainGlyph,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val strokeWidth = size.minDimension * 0.075f

        val stroke = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        when (glyph) {
            CaptainGlyph.TEAM -> {
                val shieldPath = Path().apply {
                    moveTo(
                        width * 0.50f,
                        height * 0.07f
                    )

                    lineTo(
                        width * 0.83f,
                        height * 0.21f
                    )

                    lineTo(
                        width * 0.77f,
                        height * 0.61f
                    )

                    quadraticBezierTo(
                        width * 0.70f,
                        height * 0.82f,
                        width * 0.50f,
                        height * 0.94f
                    )

                    quadraticBezierTo(
                        width * 0.30f,
                        height * 0.82f,
                        width * 0.23f,
                        height * 0.61f
                    )

                    lineTo(
                        width * 0.17f,
                        height * 0.21f
                    )

                    close()
                }

                drawPath(
                    path = shieldPath,
                    color = tint,
                    style = stroke
                )

                drawCircle(
                    color = tint,
                    radius = width * 0.10f,
                    center = Offset(
                        x = width * 0.50f,
                        y = height * 0.48f
                    ),
                    style = stroke
                )

                drawLine(
                    color = tint,
                    start = Offset(
                        x = width * 0.34f,
                        y = height * 0.67f
                    ),
                    end = Offset(
                        x = width * 0.66f,
                        y = height * 0.67f
                    ),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            CaptainGlyph.CALENDAR -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(
                        x = width * 0.14f,
                        y = height * 0.20f
                    ),
                    size = Size(
                        width = width * 0.72f,
                        height = height * 0.65f
                    ),
                    cornerRadius = CornerRadius(
                        x = width * 0.09f,
                        y = width * 0.09f
                    ),
                    style = stroke
                )

                drawLine(
                    color = tint,
                    start = Offset(
                        x = width * 0.14f,
                        y = height * 0.40f
                    ),
                    end = Offset(
                        x = width * 0.86f,
                        y = height * 0.40f
                    ),
                    strokeWidth = strokeWidth
                )

                drawLine(
                    color = tint,
                    start = Offset(
                        x = width * 0.34f,
                        y = height * 0.10f
                    ),
                    end = Offset(
                        x = width * 0.34f,
                        y = height * 0.29f
                    ),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = tint,
                    start = Offset(
                        x = width * 0.66f,
                        y = height * 0.10f
                    ),
                    end = Offset(
                        x = width * 0.66f,
                        y = height * 0.29f
                    ),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                drawCircle(
                    color = tint,
                    radius = width * 0.045f,
                    center = Offset(
                        x = width * 0.36f,
                        y = height * 0.59f
                    )
                )

                drawCircle(
                    color = tint,
                    radius = width * 0.045f,
                    center = Offset(
                        x = width * 0.64f,
                        y = height * 0.59f
                    )
                )
            }

            CaptainGlyph.SEASON -> {
                drawCircle(
                    color = tint,
                    radius = width * 0.37f,
                    center = Offset(
                        x = width * 0.50f,
                        y = height * 0.52f
                    ),
                    style = stroke
                )

                val trophy = Path().apply {
                    moveTo(
                        width * 0.35f,
                        height * 0.28f
                    )

                    lineTo(
                        width * 0.65f,
                        height * 0.28f
                    )

                    lineTo(
                        width * 0.60f,
                        height * 0.53f
                    )

                    quadraticBezierTo(
                        width * 0.57f,
                        height * 0.64f,
                        width * 0.50f,
                        height * 0.67f
                    )

                    quadraticBezierTo(
                        width * 0.43f,
                        height * 0.64f,
                        width * 0.40f,
                        height * 0.53f
                    )

                    close()
                }

                drawPath(
                    path = trophy,
                    color = tint,
                    style = stroke
                )

                drawLine(
                    color = tint,
                    start = Offset(
                        x = width * 0.50f,
                        y = height * 0.67f
                    ),
                    end = Offset(
                        x = width * 0.50f,
                        y = height * 0.76f
                    ),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = tint,
                    start = Offset(
                        x = width * 0.40f,
                        y = height * 0.76f
                    ),
                    end = Offset(
                        x = width * 0.60f,
                        y = height * 0.76f
                    ),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun CaptainErrorCard(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainRedSoft
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "No se pudo actualizar el panel",
                color = CaptainRed,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = message,
                color = CaptainRed.copy(
                    alpha = 0.82f
                ),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(11.dp))

            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(13.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = CaptainRed.copy(
                        alpha = 0.40f
                    )
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CaptainRed
                )
            ) {
                Text(
                    text = "Reintentar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CaptainEmptyState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = CaptainBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(58.dp),
                shape = RoundedCornerShape(19.dp),
                color = CaptainOrangeSoft
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CaptainGlyphIcon(
                        glyph = CaptainGlyph.TEAM,
                        tint = Color(0xFFB45A00),
                        modifier = Modifier.size(29.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Sin módulos disponibles",
                color = CaptainText,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = (
                        "Tu cuenta no tiene funciones de capitán " +
                                "habilitadas en este momento."
                        ),
                color = CaptainMuted,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CaptainLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CaptainBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(70.dp),
                shape = RoundedCornerShape(22.dp),
                color = CaptainNavy
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CaptainGlyphIcon(
                        glyph = CaptainGlyph.TEAM,
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            CircularProgressIndicator(
                color = CaptainNavy,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Preparando panel del capitán...",
                color = CaptainMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun captainInitials(
    name: String
): String {
    return name
        .trim()
        .split(" ")
        .filter {
            it.isNotBlank()
        }
        .take(2)
        .mapNotNull {
            it.firstOrNull()
        }
        .joinToString("")
        .uppercase()
        .ifBlank {
            "C"
        }
}

private fun captainGreeting(): String {
    val hour = java.util.Calendar
        .getInstance()
        .get(java.util.Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 5..11 -> "Buenos días"
        in 12..18 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}