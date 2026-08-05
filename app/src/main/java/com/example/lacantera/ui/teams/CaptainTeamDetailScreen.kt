package com.example.lacantera.ui.teams

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

/*
 * =========================================================
 * COLORES DE LA VISTA DEL CAPITÁN
 * =========================================================
 */

private val CaptainTeamBackground = Color(0xFFF3F6FB)
private val CaptainTeamSurface = Color(0xFFFFFFFF)

private val CaptainTeamNavy = Color(0xFF071E4B)
private val CaptainTeamNavyLight = Color(0xFF163E7A)
private val CaptainTeamBlue = Color(0xFF2463B6)

private val CaptainTeamText = Color(0xFF111C35)
private val CaptainTeamMuted = Color(0xFF68748A)
private val CaptainTeamBorder = Color(0xFFDCE4EF)

private val CaptainTeamGreen = Color(0xFF168052)
private val CaptainTeamGreenSoft = Color(0xFFE8F7EF)

private val CaptainTeamRed = Color(0xFFC62828)
private val CaptainTeamRedSoft = Color(0xFFFFE9EA)

private val CaptainTeamBlueSoft = Color(0xFFEAF2FF)
private val CaptainTeamOrange = Color(0xFFB76500)
private val CaptainTeamOrangeSoft = Color(0xFFFFF1E2)

/*
 * =========================================================
 * PANTALLA PRINCIPAL
 * =========================================================
 */

@Composable
fun CaptainTeamDetailScreen(
    teamId: Int,
    onBackClick: () -> Unit,
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(CaptainTeamBackground)
    ) {
        val compactScreen = maxWidth < 370.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            CaptainTeamTopBar(
                onBackClick = onBackClick,
                compactScreen = compactScreen
            )

            when {
                uiState.isLoading -> {
                    CaptainTeamLoadingState()
                }

                uiState.team == null -> {
                    CaptainTeamErrorState(
                        message = uiState.errorMessage
                            ?: "No fue posible cargar la información del equipo.",
                        onRetry = {
                            viewModel.loadTeam(teamId)
                        }
                    )
                }

                else -> {
                    val team = uiState.team ?: return@Column

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )
                            .padding(
                                horizontal = if (compactScreen) {
                                    14.dp
                                } else {
                                    18.dp
                                },
                                vertical = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 620.dp)
                        ) {
                            CaptainTeamHeroCard(
                                teamName = team.nombre,
                                sport = team.deporte,
                                category = team.categoria,
                                isActive = team.activo
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CaptainTeamInformationCard(
                                sport = team.deporte,
                                category = team.categoria,
                                isActive = team.activo
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            CaptainPermissionCard()

                            uiState.errorMessage?.let { message ->
                                Spacer(modifier = Modifier.height(14.dp))

                                CaptainTeamMessageCard(
                                    message = message
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
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
private fun CaptainTeamTopBar(
    onBackClick: () -> Unit,
    compactScreen: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CaptainTeamSurface)
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
            color = CaptainTeamBackground,
            border = BorderStroke(
                width = 1.dp,
                color = CaptainTeamBorder
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
                    color = CaptainTeamNavy,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Volver",
                    color = CaptainTeamNavy,
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
                text = "Mi equipo",
                color = CaptainTeamText,
                fontSize = if (compactScreen) {
                    17.sp
                } else {
                    19.sp
                },
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Panel del capitán",
                color = CaptainTeamMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp),
            shape = RoundedCornerShape(14.dp),
            color = CaptainTeamNavy
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CaptainTeamShieldIcon(
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

/*
 * =========================================================
 * ENCABEZADO DEL EQUIPO
 * =========================================================
 */

@Composable
private fun CaptainTeamHeroCard(
    teamName: String,
    sport: String,
    category: String,
    isActive: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(29.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainTeamNavy
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
                            CaptainTeamNavy,
                            CaptainTeamNavyLight,
                            CaptainTeamBlue
                        )
                    )
                )
                .padding(21.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(62.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.14f),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.20f)
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            CaptainTeamShieldIcon(
                                tint = Color.White,
                                modifier = Modifier.size(33.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "EQUIPO DEL CAPITÁN",
                            color = Color.White.copy(alpha = 0.62f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.7.sp
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = teamName.ifBlank {
                                "Equipo sin nombre"
                            },
                            color = Color.White,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = buildString {
                                append(
                                    sport.ifBlank {
                                        "Sin deporte"
                                    }
                                )

                                append(" · ")

                                append(
                                    category.ifBlank {
                                        "Sin categoría"
                                    }
                                )
                            },
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(19.dp))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = if (isActive) {
                        Color(0xFF65E5A4).copy(alpha = 0.17f)
                    } else {
                        Color(0xFFFF8A8A).copy(alpha = 0.17f)
                    },
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isActive) {
                            Color(0xFF65E5A4).copy(alpha = 0.35f)
                        } else {
                            Color(0xFFFF8A8A).copy(alpha = 0.35f)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    color = if (isActive) {
                                        Color(0xFF65E5A4)
                                    } else {
                                        Color(0xFFFF8A8A)
                                    },
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = if (isActive) {
                                "EQUIPO ACTIVO"
                            } else {
                                "EQUIPO INACTIVO"
                            },
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

/*
 * =========================================================
 * INFORMACIÓN GENERAL
 * =========================================================
 */

@Composable
private fun CaptainTeamInformationCard(
    sport: String,
    category: String,
    isActive: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CaptainTeamSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = CaptainTeamBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Información general",
                color = CaptainTeamText,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Datos oficiales registrados para este equipo.",
                color = CaptainTeamMuted,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(17.dp))

            CaptainInformationRow(
                letter = "D",
                title = "Deporte",
                value = sport.ifBlank {
                    "No asignado"
                },
                backgroundColor = CaptainTeamBlueSoft,
                contentColor = CaptainTeamBlue
            )

            Spacer(modifier = Modifier.height(11.dp))

            CaptainInformationRow(
                letter = "C",
                title = "Categoría",
                value = category.ifBlank {
                    "No asignada"
                },
                backgroundColor = CaptainTeamOrangeSoft,
                contentColor = CaptainTeamOrange
            )

            Spacer(modifier = Modifier.height(11.dp))

            CaptainInformationRow(
                letter = if (isActive) {
                    "✓"
                } else {
                    "!"
                },
                title = "Estado del equipo",
                value = if (isActive) {
                    "Activo"
                } else {
                    "Inactivo"
                },
                backgroundColor = if (isActive) {
                    CaptainTeamGreenSoft
                } else {
                    CaptainTeamRedSoft
                },
                contentColor = if (isActive) {
                    CaptainTeamGreen
                } else {
                    CaptainTeamRed
                }
            )
        }
    }
}

@Composable
private fun CaptainInformationRow(
    letter: String,
    title: String,
    value: String,
    backgroundColor: Color,
    contentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CaptainTeamBackground,
        border = BorderStroke(
            width = 1.dp,
            color = CaptainTeamBorder.copy(alpha = 0.75f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 13.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(43.dp),
                shape = RoundedCornerShape(14.dp),
                color = backgroundColor
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letter,
                        color = contentColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title.uppercase(),
                    color = CaptainTeamMuted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    color = CaptainTeamText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
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
private fun CaptainPermissionCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(21.dp),
        color = CaptainTeamBlueSoft,
        border = BorderStroke(
            width = 1.dp,
            color = CaptainTeamBlue.copy(alpha = 0.14f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 15.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(45.dp),
                shape = RoundedCornerShape(15.dp),
                color = CaptainTeamBlue.copy(alpha = 0.12f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "i",
                        color = CaptainTeamBlue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Vista para capitán",
                    color = CaptainTeamText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = (
                            "Puedes consultar la información del equipo. " +
                                    "La activación o desactivación corresponde " +
                                    "únicamente a los administradores."
                            ),
                    color = CaptainTeamMuted,
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
private fun CaptainTeamLoadingState() {
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
                color = CaptainTeamNavy
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CaptainTeamShieldIcon(
                        tint = Color.White,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            CircularProgressIndicator(
                color = CaptainTeamNavy,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cargando tu equipo...",
                color = CaptainTeamMuted,
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
private fun CaptainTeamErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 460.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = CaptainTeamSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = CaptainTeamBorder
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
                    color = CaptainTeamRedSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            color = CaptainTeamRed,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No se pudo cargar el equipo",
                    color = CaptainTeamText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = message,
                    color = CaptainTeamMuted,
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
                        containerColor = CaptainTeamNavy,
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

@Composable
private fun CaptainTeamMessageCard(
    message: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CaptainTeamRedSoft,
        border = BorderStroke(
            width = 1.dp,
            color = CaptainTeamRed.copy(alpha = 0.16f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 13.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "!",
                color = CaptainTeamRed,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = message,
                modifier = Modifier.weight(1f),
                color = CaptainTeamRed,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/*
 * =========================================================
 * ICONO DEL EQUIPO
 * =========================================================
 */

@Composable
private fun CaptainTeamShieldIcon(
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