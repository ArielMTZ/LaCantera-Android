package com.example.lacantera.ui.rules

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun RulesScreen(
    onBackClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LcBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            RulesHeader(
                onBackClick = onBackClick
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(14.dp)
            )
        }

        item {
            RulesHero()
        }

        item {
            RulesContent(
                onBackClick = onBackClick
            )
        }

        item {
            RulesFooter(
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
private fun RulesHeader(
    onBackClick: () -> Unit
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
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de La Cantera",
                modifier = Modifier.size(42.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "La Cantera",
                modifier = Modifier.weight(1f),
                color = LcWhite,
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedButton(
                onClick = onBackClick,
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
                    text = "← Inicio",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        HorizontalDivider(
            color = LcWhite.copy(alpha = 0.10f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LcNavyDark),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RulesNavigationItem(
                label = "Inicio",
                symbol = "⌂",
                onClick = onBackClick
            )

            RulesNavigationItem(
                label = "Posiciones",
                symbol = "▦",
                onClick = {}
            )

            RulesNavigationItem(
                label = "Equipos",
                symbol = "●●●",
                onClick = {}
            )

            RulesNavigationItem(
                label = "Roles",
                symbol = "▣",
                onClick = {}
            )
        }
    }
}

@Composable
private fun RowScope.RulesNavigationItem(
    label: String,
    symbol: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(
                top = 10.dp,
                bottom = 9.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = symbol,
            color = LcWhite.copy(alpha = 0.48f),
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
            color = LcWhite.copy(alpha = 0.48f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun RulesHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(405.dp)
            .background(LcNavyDark)
    ) {
        // Patrón de puntos del fondo.
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val spacing = 28

            for (x in 18..size.width.toInt() step spacing) {
                for (y in 18..size.height.toInt() step spacing) {
                    drawCircle(
                        color = LcWhite.copy(alpha = 0.035f),
                        radius = 1.4f,
                        center = Offset(
                            x = x.toFloat(),
                            y = y.toFloat()
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    top = 22.dp,
                    end = 20.dp,
                    bottom = 72.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(LcRed)
            )

            Spacer(modifier = Modifier.height(34.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(
                            color = LcRed,
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.width(9.dp))

                Text(
                    text = "TEMPORADA 2026 · EN VIGOR",
                    color = LcWhite.copy(alpha = 0.55f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = LcWhite
                        )
                    ) {
                        append("REGLAS ")
                    }

                    withStyle(
                        style = SpanStyle(
                            color = LcRed
                        )
                    ) {
                        append("GENERALES")
                    }
                },
                color = LcWhite,
                fontFamily = BrandSerif,
                fontSize = 31.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Aplica en cualquier categoría · Centro Deportivo La Cantera",
                color = LcWhite.copy(alpha = 0.50f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(28.dp))

            SeasonSummaryCard()
        }

        // Corte diagonal inferior, igual al estilo de la web.
        Canvas(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(62.dp)
        ) {
            val diagonalPath = Path().apply {
                moveTo(
                    x = 0f,
                    y = size.height * 0.55f
                )
                lineTo(
                    x = size.width,
                    y = 0f
                )
                lineTo(
                    x = size.width,
                    y = size.height
                )
                lineTo(
                    x = 0f,
                    y = size.height
                )
                close()
            }

            drawPath(
                path = diagonalPath,
                color = LcSurface
            )
        }
    }
}

@Composable
private fun SeasonSummaryCard() {
    Surface(
        modifier = Modifier.width(156.dp),
        shape = RoundedCornerShape(14.dp),
        color = LcWhite.copy(alpha = 0.055f),
        border = BorderStroke(
            width = 1.dp,
            color = LcWhite.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 15.dp
            )
        ) {
            Text(
                text = "🏆",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "Temporada 2026",
                color = LcWhite,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Liga en curso",
                color = LcWhite.copy(alpha = 0.45f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun RulesContent(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LcSurface)
            .padding(
                horizontal = 18.dp,
                vertical = 30.dp
            )
    ) {
        RuleSectionTitle(
            text = "EQUIPAMIENTO Y PRESENTACIÓN"
        )

        Spacer(modifier = Modifier.height(14.dp))

        RuleAccordion(
            number = "1",
            title = "Uniformes",
            subtitle = "Presentación del equipo",
            initialExpanded = true
        ) {
            Text(
                text = "Todos los equipos deberán portar un uniforme del mismo color y con número enfrente y atrás. Fecha límite para tenerlo completo: Jornada 7.",
                color = LcTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        RuleAccordion(
            number = "2",
            title = "Balón",
            subtitle = "Responsabilidad del equipo"
        ) {
            Text(
                text = "Cada equipo es responsable de presentar un balón al árbitro al momento del volado. Sin balón no se inicia el partido.",
                color = LcTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        RuleSectionTitle(
            text = "CATEGORÍAS Y ELEGIBILIDAD"
        )

        Spacer(modifier = Modifier.height(14.dp))

        RuleAccordion(
            number = "3",
            title = "Categorías",
            subtitle = "Niveles de juego"
        ) {
            CategoryRule(
                label = "CAT. A",
                description = "Mayor experiencia de todas. Aplica para Varonil, Femenil y Mixto.",
                background = Color(0xFFFFF2E4),
                accent = Color(0xFFFF7A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategoryRule(
                label = "CAT. B +/-",
                description = "Nivel intermedio. Aplica para Varonil, Femenil y Mixto.",
                background = LcBlueSoft,
                accent = LcBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategoryRule(
                label = "CAT. C +/-",
                description = "Nivel principiante.",
                background = LcGreenSoft,
                accent = LcGreen
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        RuleAccordion(
            number = "4",
            title = "Jugadores no elegibles",
            subtitle = "Causas de inelegibilidad",
            numberColor = LcRed
        ) {
            Text(
                text = "Se considera un jugador inelegible cuando:",
                color = LcTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            RuleBullet(
                text = "No pertenece al equipo (sin registro dentro del mismo)."
            )

            RuleBullet(
                text = "No pertenece a la categoría en la que está jugando."
            )

            RuleBullet(
                text = "Entra únicamente para ayudar a completar el equipo."
            )

            RuleBullet(
                text = "Participa en más de 2 equipos de la misma categoría."
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        RuleSectionTitle(
            text = "TIEMPO Y DOCUMENTACIÓN"
        )

        Spacer(modifier = Modifier.height(14.dp))

        RuleAccordion(
            number = "5",
            title = "Tiempo de tolerancia",
            subtitle = "Puntualidad obligatoria",
            numberColor = LcRed
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFF8E3),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFFFCD58)
                )
            ) {
                Text(
                    text = "⏱  5 minutos de tolerancia si hay al menos 1 integrante para el volado. Al minuto 6 el juego se pierde por default.",
                    modifier = Modifier.padding(14.dp),
                    color = Color(0xFF7A4A00),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        RuleAccordion(
            number = "6",
            title = "Registro vigente",
            subtitle = "Jugador registrado",
            numberColor = LcRed
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = LcRedSoft,
                border = BorderStroke(
                    width = 1.dp,
                    color = LcRed.copy(alpha = 0.25f)
                )
            ) {
                Text(
                    text = "🪪  Todo jugador debe tener su registro vigente dentro de la web de La Cantera. El capitán de cada equipo debe asegurarse de que todos los jugadores estén registrados. Sin registro no se puede participar.",
                    modifier = Modifier.padding(14.dp),
                    color = Color(0xFF7B1D25),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        ContactRulesCard(
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun RuleSectionTitle(
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = LcBorder
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = LcSurface,
            border = BorderStroke(
                width = 1.dp,
                color = LcBorder
            )
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),
                color = LcTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp
            )
        }

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = LcBorder
        )
    }
}

@Composable
private fun RuleAccordion(
    number: String,
    title: String,
    subtitle: String,
    numberColor: Color = LcNavy,
    initialExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(initialExpanded)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = LcSurface
        ),
        border = BorderStroke(
            width = if (expanded) 1.5.dp else 1.dp,
            color = if (expanded) {
                numberColor.copy(alpha = 0.75f)
            } else {
                LcBorder
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (expanded) 4.dp else 2.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = numberColor
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = number,
                            color = LcWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(13.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        color = LcTextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = subtitle,
                        color = LcTextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = if (expanded) {
                        numberColor.copy(alpha = 0.12f)
                    } else {
                        LcSurfaceSoft
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (expanded) "−" else "+",
                            color = if (expanded) {
                                numberColor
                            } else {
                                LcTextSecondary
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (expanded) {
                HorizontalDivider(
                    color = LcBorder
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LcSurfaceSoft.copy(alpha = 0.55f))
                        .padding(16.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun CategoryRule(
    label: String,
    description: String,
    background: Color,
    accent: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = background,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(5.dp),
            color = accent.copy(alpha = 0.16f)
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 5.dp
                ),
                color = accent,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = description,
            modifier = Modifier.weight(1f),
            color = LcTextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun RuleBullet(
    text: String
) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "✕",
            color = LcRed,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = LcTextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ContactRulesCard(
    onBackClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LcNavy
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "¿TIENES DUDAS?",
                color = LcWhite,
                fontFamily = BrandSerif,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Contáctanos por WhatsApp o vuelve al inicio.",
                color = LcWhite.copy(alpha = 0.58f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF12B76A),
                        contentColor = LcWhite
                    )
                ) {
                    Text(
                        text = "◉  (656) 130 8025",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                OutlinedButton(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = LcWhite.copy(alpha = 0.20f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = LcWhite
                    )
                ) {
                    Text(
                        text = "← Inicio",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            HorizontalDivider(
                color = LcWhite.copy(alpha = 0.08f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "ⓘ  Centro Deportivo La Cantera · Temporada 2026 · Aplica en cualquier categoría",
                color = LcWhite.copy(alpha = 0.35f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun RulesFooter(
    onBackClick: () -> Unit
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

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "© 2026 La Cantera | Centro Deportivo",
            color = LcTextSecondary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBackClick
            ) {
                Text(
                    text = "Inicio",
                    color = LcTextSecondary,
                    fontSize = 11.sp
                )
            }

            Text(
                text = "Privacidad",
                color = LcTextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = "Términos",
                color = LcTextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.width(14.dp))

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
    }
}