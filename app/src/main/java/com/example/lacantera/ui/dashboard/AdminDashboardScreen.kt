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

private val AdminBackground = Color(0xFFF4F6FA)
private val AdminSurface = Color(0xFFFFFFFF)
private val AdminNavy = Color(0xFF0A2458)
private val AdminNavyDark = Color(0xFF061638)
private val AdminBlue = Color(0xFF1E56A6)
private val AdminRed = Color(0xFFD0272D)

private val AdminText = Color(0xFF111C36)
private val AdminMuted = Color(0xFF68738A)
private val AdminBorder = Color(0xFFDDE3ED)

private val AdminBlueSoft = Color(0xFFE9F1FF)
private val AdminGreenSoft = Color(0xFFE8F7EF)
private val AdminOrangeSoft = Color(0xFFFFF1DF)
private val AdminRedSoft = Color(0xFFFFE9EA)

private enum class AdminGlyph {
    TEAM,
    USERS,
    SPORTS,
    SEASON
}

private data class AdminAction(
    val title: String,
    val description: String,
    val detail: String,
    val glyph: AdminGlyph,
    val accentColor: Color,
    val iconBackground: Color,
    val onClick: () -> Unit
)

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    onSessionExpired: () -> Unit,
    onNavigateToSports: () -> Unit,
    onNavigateToTeams: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onNavigateToSeasons: () -> Unit,
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
        AdminLoadingScreen()
        return
    }

    val displayName = uiState.nombreCorto.ifBlank {
        uiState.username.ifBlank {
            "Administrador"
        }
    }

    val actions = buildList {
        if (uiState.permisos.administrarEquipos) {
            add(
                AdminAction(
                    title = "Equipos",
                    description = "Consulta, edita y administra los equipos registrados.",
                    detail = "${uiState.totalEquipos} registrados",
                    glyph = AdminGlyph.TEAM,
                    accentColor = Color(0xFF157347),
                    iconBackground = AdminGreenSoft,
                    onClick = onNavigateToTeams
                )
            )
        }

        if (uiState.permisos.administrarUsuarios) {
            add(
                AdminAction(
                    title = "Usuarios",
                    description = "Gestiona cuentas, perfiles, estados y permisos.",
                    detail = "${uiState.totalJugadores} jugadores",
                    glyph = AdminGlyph.USERS,
                    accentColor = Color(0xFFB45A00),
                    iconBackground = AdminOrangeSoft,
                    onClick = onNavigateToUsers
                )
            )
        }

        if (uiState.permisos.administrarDeportes) {
            add(
                AdminAction(
                    title = "Deportes",
                    description = "Administra deportes, categorías y posiciones.",
                    detail = "Configuración deportiva",
                    glyph = AdminGlyph.SPORTS,
                    accentColor = AdminBlue,
                    iconBackground = AdminBlueSoft,
                    onClick = onNavigateToSports
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AdminBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                AdminTopHeader(
                    displayName = displayName,
                    roleName = getAdminRoleName(
                        uiState.tipoUsuario
                    )
                )

                uiState.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(14.dp))

                    AdminErrorMessage(
                        message = message,
                        onRetry = viewModel::loadDashboard
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Vista general",
                    color = AdminText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Resumen actualizado de la liga",
                    color = AdminMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                LeagueSummaryCard(
                    totalEquipos = uiState.totalEquipos,
                    totalJugadores = uiState.totalJugadores,
                    totalArbitros = uiState.totalArbitros
                )

                if (uiState.permisos.verTemporadaActual) {
                    Spacer(modifier = Modifier.height(18.dp))

                    CurrentSeasonCard(
                        onClick = onNavigateToSeasons
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                Text(
                    text = "Administración",
                    color = AdminText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Herramientas principales de gestión",
                    color = AdminMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(13.dp))

                if (actions.isEmpty()) {
                    EmptyAdminActions()
                } else {
                    AdminActionsGrid(
                        actions = actions
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                AdminAccountCard(
                    username = uiState.username,
                    onLogout = viewModel::logout
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun AdminTopHeader(
    displayName: String,
    roleName: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminNavy
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
                            AdminNavyDark,
                            AdminNavy,
                            AdminBlue
                        )
                    )
                )
                .padding(
                    horizontal = 21.dp,
                    vertical = 21.dp
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(58.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.14f),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.22f)
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getInitials(displayName),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = getGreeting(),
                            color = Color.White.copy(alpha = 0.73f),
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
                            text = roleName,
                            color = Color.White.copy(alpha = 0.79f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Surface(
                        modifier = Modifier.size(45.dp),
                        shape = RoundedCornerShape(15.dp),
                        color = Color.White.copy(alpha = 0.12f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "LC",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(17.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = Color(0xFF69E4A4),
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = "Panel conectado con La Cantera",
                        color = Color.White.copy(alpha = 0.77f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun LeagueSummaryCard(
    totalEquipos: Int,
    totalJugadores: Int,
    totalArbitros: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AdminBorder
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
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "La liga hoy",
                        color = AdminText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Datos generales registrados",
                        color = AdminMuted,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = AdminBlueSoft
                ) {
                    Text(
                        text = "ACTUAL",
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),
                        color = AdminBlue,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.7.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryItem(
                    value = totalEquipos,
                    label = "Equipos",
                    modifier = Modifier.weight(1f)
                )

                SummaryDivider()

                SummaryItem(
                    value = totalJugadores,
                    label = "Jugadores",
                    modifier = Modifier.weight(1f)
                )

                SummaryDivider()

                SummaryItem(
                    value = totalArbitros,
                    label = "Árbitros",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(19.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                AdminBlue,
                                Color(0xFF3B82C4),
                                AdminRed
                            )
                        ),
                        shape = RoundedCornerShape(50.dp)
                    )
            )
        }
    }
}

@Composable
private fun SummaryItem(
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
            color = AdminText,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            color = AdminMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SummaryDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(46.dp)
            .background(AdminBorder)
    )
}

@Composable
private fun CurrentSeasonCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminNavy
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AdminNavyDark,
                            AdminNavy
                        )
                    )
                )
                .padding(19.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(54.dp),
                    shape = RoundedCornerShape(17.dp),
                    color = AdminRed.copy(alpha = 0.18f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = AdminRed.copy(alpha = 0.42f)
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        AdminGlyphIcon(
                            glyph = AdminGlyph.SEASON,
                            tint = Color.White,
                            modifier = Modifier.size(27.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Temporada actual",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Consulta fechas, categorías y estado de la temporada.",
                        color = Color.White.copy(alpha = 0.72f),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.12f)
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
private fun AdminActionsGrid(
    actions: List<AdminAction>
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val singleColumn = maxWidth < 360.dp

        if (singleColumn) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                actions.forEach { action ->
                    AdminActionCard(
                        action = action,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                actions.chunked(2).forEach { rowActions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowActions.forEach { action ->
                            AdminActionCard(
                                action = action,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (rowActions.size == 1) {
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminActionCard(
    action: AdminAction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(184.dp)
            .clickable(onClick = action.onClick),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AdminBorder
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
                    color = action.iconBackground
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        AdminGlyphIcon(
                            glyph = action.glyph,
                            tint = action.accentColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = CircleShape,
                    color = action.iconBackground
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
                    color = AdminText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = action.description,
                    color = AdminMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(9.dp))

                Text(
                    text = action.detail,
                    color = action.accentColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AdminGlyphIcon(
    glyph: AdminGlyph,
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
            AdminGlyph.TEAM -> {
                val shield = Path().apply {
                    moveTo(width * 0.50f, height * 0.08f)
                    lineTo(width * 0.82f, height * 0.21f)
                    lineTo(width * 0.76f, height * 0.62f)
                    quadraticBezierTo(
                        width * 0.70f,
                        height * 0.82f,
                        width * 0.50f,
                        height * 0.93f
                    )
                    quadraticBezierTo(
                        width * 0.30f,
                        height * 0.82f,
                        width * 0.24f,
                        height * 0.62f
                    )
                    lineTo(width * 0.18f, height * 0.21f)
                    close()
                }

                drawPath(
                    path = shield,
                    color = tint,
                    style = stroke
                )

                drawLine(
                    color = tint,
                    start = Offset(width * 0.35f, height * 0.49f),
                    end = Offset(width * 0.65f, height * 0.49f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            AdminGlyph.USERS -> {
                drawCircle(
                    color = tint,
                    radius = width * 0.14f,
                    center = Offset(width * 0.50f, height * 0.31f),
                    style = stroke
                )

                drawArc(
                    color = tint,
                    startAngle = 195f,
                    sweepAngle = 150f,
                    useCenter = false,
                    topLeft = Offset(width * 0.20f, height * 0.43f),
                    size = Size(width * 0.60f, height * 0.44f),
                    style = stroke
                )

                drawCircle(
                    color = tint,
                    radius = width * 0.09f,
                    center = Offset(width * 0.22f, height * 0.42f),
                    style = stroke
                )

                drawCircle(
                    color = tint,
                    radius = width * 0.09f,
                    center = Offset(width * 0.78f, height * 0.42f),
                    style = stroke
                )
            }

            AdminGlyph.SPORTS -> {
                drawCircle(
                    color = tint,
                    radius = width * 0.38f,
                    center = Offset(width * 0.50f, height * 0.50f),
                    style = stroke
                )

                drawCircle(
                    color = tint,
                    radius = width * 0.10f,
                    center = Offset(width * 0.50f, height * 0.50f),
                    style = stroke
                )

                drawLine(
                    color = tint,
                    start = Offset(width * 0.50f, height * 0.40f),
                    end = Offset(width * 0.50f, height * 0.14f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = tint,
                    start = Offset(width * 0.41f, height * 0.54f),
                    end = Offset(width * 0.20f, height * 0.69f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = tint,
                    start = Offset(width * 0.59f, height * 0.54f),
                    end = Offset(width * 0.80f, height * 0.69f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            AdminGlyph.SEASON -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(width * 0.15f, height * 0.20f),
                    size = Size(width * 0.70f, height * 0.65f),
                    cornerRadius = CornerRadius(
                        x = width * 0.09f,
                        y = width * 0.09f
                    ),
                    style = stroke
                )

                drawLine(
                    color = tint,
                    start = Offset(width * 0.15f, height * 0.39f),
                    end = Offset(width * 0.85f, height * 0.39f),
                    strokeWidth = strokeWidth
                )

                drawLine(
                    color = tint,
                    start = Offset(width * 0.34f, height * 0.10f),
                    end = Offset(width * 0.34f, height * 0.28f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = tint,
                    start = Offset(width * 0.66f, height * 0.10f),
                    end = Offset(width * 0.66f, height * 0.28f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun AdminAccountCard(
    username: String,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AdminBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Cuenta administrativa",
                color = AdminText,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (username.isBlank()) {
                    "Sesión activa"
                } else {
                    "Sesión iniciada como @$username"
                },
                color = AdminMuted,
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
                    color = AdminRed.copy(alpha = 0.35f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = AdminRed
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
private fun AdminErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminRedSoft
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "No fue posible actualizar el panel",
                color = AdminRed,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = message,
                color = AdminRed.copy(alpha = 0.82f),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onRetry,
                border = BorderStroke(
                    width = 1.dp,
                    color = AdminRed.copy(alpha = 0.4f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AdminRed
                ),
                shape = RoundedCornerShape(13.dp)
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
private fun EmptyAdminActions() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AdminBorder
        )
    ) {
        Text(
            text = "Tu cuenta no tiene módulos administrativos disponibles.",
            modifier = Modifier.padding(22.dp),
            color = AdminMuted,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AdminLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AdminBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(68.dp),
                shape = RoundedCornerShape(21.dp),
                color = AdminNavy
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LC",
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            CircularProgressIndicator(
                color = AdminNavy,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Preparando administración...",
                color = AdminMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun getAdminRoleName(
    tipoUsuario: String
): String {
    return when (tipoUsuario.lowercase()) {
        "superadmin" -> "Superadministrador"
        "staff" -> "Personal administrativo"
        "admin_principal" -> "Administrador principal"
        "admin" -> "Administrador"
        "finanzas" -> "Área de finanzas"
        else -> "Administración"
    }
}

private fun getInitials(
    name: String
): String {
    return name
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull() }
        .joinToString("")
        .uppercase()
        .ifBlank { "LC" }
}

private fun getGreeting(): String {
    val hour = java.util.Calendar
        .getInstance()
        .get(java.util.Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 5..11 -> "Buenos días"
        in 12..18 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}