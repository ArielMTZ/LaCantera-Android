package com.example.lacantera.ui.publichome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.lacantera.R
import com.example.lacantera.ui.theme.BrandSerif
import com.example.lacantera.ui.theme.LcBackground
import com.example.lacantera.ui.theme.LcBlue
import com.example.lacantera.ui.theme.LcBlueSoft
import com.example.lacantera.ui.theme.LcBorder
import com.example.lacantera.ui.theme.LcGreen
import com.example.lacantera.ui.theme.LcGreenSoft
import com.example.lacantera.ui.theme.LcNavy
import com.example.lacantera.ui.theme.LcNavyDark
import com.example.lacantera.ui.theme.LcRed
import com.example.lacantera.ui.theme.LcRedSoft
import com.example.lacantera.ui.theme.LcSurface
import com.example.lacantera.ui.theme.LcSurfaceSoft
import com.example.lacantera.ui.theme.LcTextMuted
import com.example.lacantera.ui.theme.LcTextPrimary
import com.example.lacantera.ui.theme.LcTextSecondary
import com.example.lacantera.ui.theme.LcWhite

private const val PROGRAM_INSCRIPTION = 1
private const val PROGRAM_BEACH_VOLLEYBALL = 2

@Composable
fun PublicHomeScreen(
    onLoginClick: () -> Unit,
    onProgramsClick: () -> Unit,
    onRulesClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onSupportClick: () -> Unit,
    onStandingsClick: () -> Unit = {},
    onTeamsClick: () -> Unit = {},
    onRolesClick: () -> Unit = {}
) {
    var showFeaturedEvent by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedProgram by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    var showTransferDialog by rememberSaveable {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LcBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            PublicHeader(
                onLoginClick = onLoginClick,
                onStandingsClick = onStandingsClick,
                onTeamsClick = onTeamsClick,
                onRolesClick = onRolesClick
            )
        }

        item {
            HeroSection(
                onFeaturedEventClick = {
                    showFeaturedEvent = true
                }
            )
        }

        item {
            ImportantNoticesSection(
                onTransferClick = {
                    showTransferDialog = true
                },
                onRulesClick = onRulesClick
            )
        }

        item {
            ProgramsSection(
                onProgramsClick = onProgramsClick,
                onProgramClick = { programId ->
                    selectedProgram = programId
                }
            )
        }

        item {
            PublicFooter(
                onPrivacyClick = onPrivacyClick,
                onTermsClick = onTermsClick,
                onSupportClick = onSupportClick
            )
        }
    }

    if (showFeaturedEvent) {
        PosterDialog(
            imageRes = R.drawable.cantera_alien_2,
            label = "VOLI · MIXTO",
            title = "Torneo Alien 2026",
            description = "17va Edición, De otro planeta. Torneo de voleibol, con equipos de toda la región. ¡Inscríbete y participa!",
            contentDescription = "Cartel del Torneo Alien 2026",
            onDismiss = {
                showFeaturedEvent = false
            }
        )
    }

    if (showTransferDialog) {
        TransferDataDialog(
            onDismiss = {
                showTransferDialog = false
            }
        )
    }

    when (selectedProgram) {
        PROGRAM_INSCRIPTION -> {
            PosterDialog(
                imageRes = R.drawable.cantera_inscripcion,
                label = "VOLEIBOL · COMPETITIVO",
                title = "Inscripción de equipos",
                description = "Consulta los requisitos, categorías disponibles y la información para inscribir a tu equipo.",
                contentDescription = "Cartel de inscripción de equipos",
                onDismiss = {
                    selectedProgram = null
                }
            )
        }

        PROGRAM_BEACH_VOLLEYBALL -> {
            PosterDialog(
                imageRes = R.drawable.playa_remodelada,
                label = "CONVOCATORIA",
                title = "Voleibol de playa",
                description = "Participa en torneos de voleibol de playa con entrenamiento y preparación física.",
                contentDescription = "Cartel de voleibol de playa",
                onDismiss = {
                    selectedProgram = null
                }
            )
        }
    }
}

@Composable
private fun PublicHeader(
    onLoginClick: () -> Unit,
    onStandingsClick: () -> Unit,
    onTeamsClick: () -> Unit,
    onRolesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LcNavy)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LogoMark()

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "La Cantera",
                modifier = Modifier.weight(1f),
                color = LcWhite,
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedButton(
                onClick = onLoginClick,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = LcWhite.copy(alpha = 0.22f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = LcWhite.copy(alpha = 0.07f),
                    contentColor = LcWhite
                ),
                contentPadding = PaddingValues(
                    horizontal = 14.dp,
                    vertical = 8.dp
                )
            ) {
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        HorizontalDivider(
            color = LcWhite.copy(alpha = 0.10f)
        )

        PublicNavigation(
            onStandingsClick = onStandingsClick,
            onTeamsClick = onTeamsClick,
            onRolesClick = onRolesClick
        )
    }
}

@Composable
private fun LogoMark() {
    Image(
        painter = painterResource(
            id = R.drawable.logo
        ),
        contentDescription = "Logo de La Cantera",
        modifier = Modifier.size(42.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun PublicNavigation(
    onStandingsClick: () -> Unit,
    onTeamsClick: () -> Unit,
    onRolesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LcNavyDark),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PublicNavigationItem(
            label = "Inicio",
            symbol = "⌂",
            selected = true,
            onClick = {}
        )

        PublicNavigationItem(
            label = "Posiciones",
            symbol = "▦",
            selected = false,
            onClick = onStandingsClick
        )

        PublicNavigationItem(
            label = "Equipos",
            symbol = "●●●",
            selected = false,
            onClick = onTeamsClick
        )

        PublicNavigationItem(
            label = "Roles",
            symbol = "▣",
            selected = false,
            onClick = onRolesClick
        )
    }
}

@Composable
private fun RowScope.PublicNavigationItem(
    label: String,
    symbol: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(
                top = 10.dp,
                bottom = 7.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = symbol,
            color = if (selected) {
                Color(0xFF62A3FF)
            } else {
                LcWhite.copy(alpha = 0.45f)
            },
            fontSize = if (label == "Equipos") {
                9.sp
            } else {
                17.sp
            },
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            color = if (selected) {
                Color(0xFF62A3FF)
            } else {
                LcWhite.copy(alpha = 0.48f)
            },
            fontSize = 10.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .width(24.dp)
                .height(2.dp)
                .background(
                    color = if (selected) {
                        Color(0xFF62A3FF)
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun HeroSection(
    onFeaturedEventClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LcSurface)
            .padding(
                horizontal = 20.dp,
                vertical = 32.dp
            )
    ) {
        SeasonBadge()

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Tu liga de",
            color = LcTextPrimary,
            fontFamily = BrandSerif,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )

        Text(
            text = "Voleibol",
            color = LcRed,
            fontFamily = BrandSerif,
            fontStyle = FontStyle.Italic,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )

        Text(
            text = "favorita",
            color = LcTextPrimary,
            fontFamily = BrandSerif,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )

        Spacer(modifier = Modifier.height(17.dp))

        Text(
            text = "Consulta la tabla general, revisa los próximos partidos y sigue el rendimiento de todos los equipos en tiempo real.",
            color = LcTextSecondary,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        FeaturedEventCard(
            onClick = onFeaturedEventClick
        )
    }
}

@Composable
private fun SeasonBadge() {
    Surface(
        shape = RoundedCornerShape(50),
        color = LcRedSoft,
        border = BorderStroke(
            width = 1.dp,
            color = LcRed.copy(alpha = 0.20f)
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
                    .size(6.dp)
                    .background(
                        color = LcRed,
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = "TEMPORADA 2026 · EN CURSO",
                color = LcRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.7.sp
            )
        }
    }
}

@Composable
private fun FeaturedEventCard(
    onClick: () -> Unit
) {
    PosterCard(
        imageRes = R.drawable.cantera_alien_2,
        label = "VOLI · MIXTO",
        title = "Torneo Alien 2026",
        description = "17va Edición, De otro planeta. Torneo de voleibol, con equipos de toda la región. ¡Inscríbete y participa!",
        actionText = "Ver evento",
        contentDescription = "Cartel del Torneo Alien 2026",
        onClick = onClick
    )
}

@Composable
private fun ImportantNoticesSection(
    onTransferClick: () -> Unit,
    onRulesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LcSurface)
            .padding(
                horizontal = 20.dp,
                vertical = 30.dp
            )
    ) {
        HorizontalDivider(
            color = LcBorder
        )

        Spacer(modifier = Modifier.height(30.dp))

        SectionEyebrow(
            text = "AVISOS IMPORTANTES"
        )

        Spacer(modifier = Modifier.height(18.dp))

        NoticeCard(
            title = "Datos para transferencia bancaria",
            description = "Consulta aquí la cuenta y los datos necesarios para realizar tu pago de inscripción o mensualidad.",
            actionText = "Ver datos",
            headerColor = LcBlueSoft,
            titleColor = LcNavy,
            actionColor = LcBlue,
            onClick = onTransferClick
        )

        Spacer(modifier = Modifier.height(14.dp))

        NoticeCard(
            title = "Reglas generales de la liga",
            description = "Uniformes, balón, categorías, jugadores no elegibles, tiempo de tolerancia y más.",
            actionText = "Ver reglamento",
            headerColor = LcGreenSoft,
            titleColor = LcGreen,
            actionColor = LcGreen,
            onClick = onRulesClick
        )
    }
}

@Composable
private fun SectionEyebrow(
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(18.dp)
                .height(2.dp)
                .background(
                    color = LcRed,
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = LcRed,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp
        )
    }
}

@Composable
private fun NoticeCard(
    title: String,
    description: String,
    actionText: String,
    headerColor: Color,
    titleColor: Color,
    actionColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = LcSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = LcBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(
                        horizontal = 18.dp,
                        vertical = 15.dp
                    )
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = description,
                    color = LcTextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "$actionText  →",
                    modifier = Modifier.clickable(onClick = onClick),
                    color = actionColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun ProgramsSection(
    onProgramsClick: () -> Unit,
    onProgramClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LcSurface)
            .padding(
                horizontal = 20.dp,
                vertical = 30.dp
            )
    ) {
        SectionEyebrow(
            text = "FORMACIÓN DEPORTIVA"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nuestros programas",
            color = LcTextPrimary,
            fontFamily = BrandSerif,
            fontSize = 31.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Entrenamiento, competencia y desarrollo deportivo para todas las edades.",
            color = LcTextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onProgramsClick,
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LcBlueSoft,
                contentColor = LcBlue
            ),
            contentPadding = PaddingValues(
                horizontal = 18.dp,
                vertical = 11.dp
            )
        ) {
            Text(
                text = "Ver todos los programas",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PosterCard(
            imageRes = R.drawable.cantera_inscripcion,
            label = "VOLEIBOL · COMPETITIVO",
            title = "Inscripción de equipos",
            description = "Consulta los requisitos, categorías disponibles y la información para inscribir a tu equipo.",
            actionText = "Más información",
            contentDescription = "Cartel de inscripción de equipos",
            onClick = {
                onProgramClick(PROGRAM_INSCRIPTION)
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        PosterCard(
            imageRes = R.drawable.playa_remodelada,
            label = "CONVOCATORIA",
            title = "Voleibol de playa",
            description = "Participa en torneos de voleibol de playa con entrenamiento y preparación física.",
            actionText = "Más información",
            contentDescription = "Cartel de voleibol de playa",
            onClick = {
                onProgramClick(PROGRAM_BEACH_VOLLEYBALL)
            }
        )
    }
}

@Composable
private fun PosterCard(
    imageRes: Int,
    label: String,
    title: String,
    description: String,
    actionText: String,
    contentDescription: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = LcNavy
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.55f),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = label,
                    color = Color(0xFF9EC5FF),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = title,
                    color = LcWhite,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = description,
                    color = LcWhite.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$actionText  →",
                    color = Color(0xFF9EC5FF),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun PublicFooter(
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LcSurface)
            .padding(
                horizontal = 20.dp,
                vertical = 28.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            color = LcBorder
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "© 2026 La Cantera | Centro Deportivo",
            color = LcTextSecondary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onPrivacyClick
            ) {
                Text(
                    text = "Privacidad",
                    color = LcTextSecondary,
                    fontSize = 11.sp
                )
            }

            TextButton(
                onClick = onTermsClick
            ) {
                Text(
                    text = "Términos",
                    color = LcTextSecondary,
                    fontSize = 11.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = LcSurfaceSoft
            ) {
                Text(
                    text = "v1.0.0",
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 5.dp
                    ),
                    color = LcTextMuted,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            SocialButton(
                text = "f",
                onClick = onSupportClick
            )

            SocialButton(
                text = "w",
                onClick = onSupportClick
            )

            SocialButton(
                text = "t",
                onClick = onSupportClick
            )
        }
    }
}

@Composable
private fun SocialButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(34.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = LcSurfaceSoft
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = LcTextSecondary,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun TransferDataDialog(
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val clabe = "0021 6470 2272 2869 33"

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = LcSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            )
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF2459D8),
                                    Color(0xFF438AF4)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(end = 42.dp)
                    ) {
                        Text(
                            text = "Datos para\ntransferencia\nbancaria",
                            color = LcWhite,
                            fontSize = 22.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Liga de Voleibol La Cantera · Temporada 2026",
                            color = LcWhite.copy(alpha = 0.78f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(34.dp)
                            .clickable(onClick = onDismiss),
                        shape = CircleShape,
                        color = LcWhite.copy(alpha = 0.18f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "×",
                                color = LcWhite,
                                fontSize = 23.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Realiza tu pago de inscripción o mensualidad mediante transferencia a la siguiente cuenta:",
                        color = LcTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    TransferDataRow(
                        label = "NOMBRE",
                        value = "Daniel Treviño Soto"
                    )

                    HorizontalDivider(color = LcBorder)

                    TransferDataRow(
                        label = "BANCO",
                        value = "Banamex"
                    )

                    HorizontalDivider(color = LcBorder)

                    TransferDataRow(
                        label = "CLABE",
                        value = clabe,
                        valueColor = LcBlue
                    )

                    HorizontalDivider(color = LcBorder)

                    TransferDataRow(
                        label = "CONCEPTO",
                        value = "Pago y equipo"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            clipboardManager.setText(
                                AnnotatedString(clabe.replace(" ", ""))
                            )
                        },
                        shape = RoundedCornerShape(9.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LcBlueSoft,
                            contentColor = LcBlue
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 14.dp,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            text = "▣  Copiar número de cuenta",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    HorizontalDivider(color = LcBorder)

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Recuerda enviar tu comprobante de pago al WhatsApp de La Cantera: (656) 130 8025",
                        color = LcTextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun TransferDataRow(
    label: String,
    value: String,
    valueColor: Color = LcTextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(78.dp),
            color = LcTextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.7.sp
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            color = valueColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PosterDialog(
    imageRes: Int,
    label: String,
    title: String,
    description: String,
    contentDescription: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = LcNavy
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 14.dp
            )
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.76f),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(36.dp)
                            .clickable(onClick = onDismiss),
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.68f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "×",
                                color = LcWhite,
                                fontSize = 24.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = label,
                        color = Color(0xFF9EC5FF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = title,
                        color = LcWhite,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = description,
                        color = LcWhite.copy(alpha = 0.78f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}