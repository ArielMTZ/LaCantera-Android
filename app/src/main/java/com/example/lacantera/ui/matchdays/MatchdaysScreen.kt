package com.example.lacantera.ui.matchdays

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lacantera.ui.components.PublicFooter
import com.example.lacantera.ui.components.PublicHeader
import com.example.lacantera.ui.components.PublicHero
import com.example.lacantera.ui.components.PublicNavigation
import com.example.lacantera.ui.components.PublicTab
import com.example.lacantera.ui.theme.LcBackground
import com.example.lacantera.ui.theme.LcBorder
import com.example.lacantera.ui.theme.LcNavy
import com.example.lacantera.ui.theme.LcRed
import com.example.lacantera.ui.theme.LcSurface
import com.example.lacantera.ui.theme.LcSurfaceSoft
import com.example.lacantera.ui.theme.LcTextMuted
import com.example.lacantera.ui.theme.LcTextPrimary
import com.example.lacantera.ui.theme.LcTextSecondary
import com.example.lacantera.ui.theme.LcWhite

private data class CalendarDay(
    val dayName: String,
    val dayNumber: String
)

private data class MockMatch(
    val schedule: String,
    val startTime: String,
    val homeTeam: String,
    val awayTeam: String,
    val score: String
)

@Composable
fun MatchdaysScreen(
    onHomeClick: () -> Unit,
    onStandingsClick: () -> Unit = {},
    onTeamsClick: () -> Unit = {},
    onRolesClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {}
) {
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var selectedDay by rememberSaveable {
        mutableStateOf("26")
    }

    var selectedCourt by rememberSaveable {
        mutableStateOf("CANCHA 1")
    }

    val days = listOf(
        CalendarDay("JUE", "23"),
        CalendarDay("VIE", "24"),
        CalendarDay("SÁB", "25"),
        CalendarDay("DOM", "26"),
        CalendarDay("LUN", "27"),
        CalendarDay("MAR", "28"),
        CalendarDay("MIÉ", "29")
    )

    val matches = listOf(
        MockMatch(
            schedule = "8:00 a.m.",
            startTime = "08:30",
            homeTeam = "WILDCATS GIRLS",
            awayTeam = "COBRAS",
            score = "0 - 2"
        ),
        MockMatch(
            schedule = "9:00 a.m.",
            startTime = "09:20",
            homeTeam = "WILDCATS B",
            awayTeam = "LLAMA EN LLAMAS",
            score = "2 - 0"
        ),
        MockMatch(
            schedule = "10:00 a.m.",
            startTime = "10:10",
            homeTeam = "PRIMOS",
            awayTeam = "NO DISPONIBLE",
            score = "—"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LcBackground),
        contentPadding = PaddingValues(bottom = 12.dp)
    ) {
        item {
            PublicHeader(
                selectedTab = PublicTab.MATCHDAYS,
                onHomeClick = onHomeClick,
                onStandingsClick = onStandingsClick,
                onTeamsClick = onTeamsClick,
                onRolesClick = onRolesClick,
                onLoginClick = onLoginClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
            PublicHero(
                eyebrow = "TEMPORADA 2026 · EN CURSO",
                titleStart = "CALENDARIO ",
                titleHighlight = "DE PARTIDOS",
                subtitle = "Consulta los horarios y busca los próximos partidos de tu equipo."
            )
        }

        item {
            Column(
                modifier = Modifier.padding(
                    horizontal = 18.dp
                )
            ) {
                SectionTitle(text = "ROLES")

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Buscar equipo para ver sus próximos partidos",
                            color = LcTextMuted,
                            fontSize = 12.sp
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "⌕",
                            color = LcTextMuted,
                            fontSize = 23.sp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(13.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LcNavy,
                        unfocusedBorderColor = LcBorder,
                        focusedContainerColor = LcSurface,
                        unfocusedContainerColor = LcSurface
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                MonthSelector()

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    days.forEach { day ->
                        DayCard(
                            day = day,
                            selected = selectedDay == day.dayNumber,
                            onClick = {
                                selectedDay = day.dayNumber
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                CourtSelector(
                    selectedCourt = selectedCourt,
                    onPrevious = {
                        selectedCourt = previousCourt(selectedCourt)
                    },
                    onNext = {
                        selectedCourt = nextCourt(selectedCourt)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Desliza para ver otra cancha  ›",
                    modifier = Modifier.fillMaxWidth(),
                    color = LcTextMuted,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                matches.forEach { match ->
                    MatchCard(match = match)

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        item {
            PublicFooter(
                onPrivacyClick = onPrivacyClick,
                onTermsClick = onTermsClick
            )
        }
    }
}

@Composable
private fun SectionTitle(
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
            color = LcWhite,
            border = BorderStroke(
                width = 1.dp,
                color = LcBorder
            )
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 5.dp
                ),
                color = LcTextMuted,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = LcBorder
        )
    }
}

@Composable
private fun MonthSelector() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleActionButton(
            text = "←",
            onClick = {}
        )

        Spacer(modifier = Modifier.width(12.dp))

        Surface(
            color = LcSurfaceSoft,
            shape = RoundedCornerShape(50),
            border = BorderStroke(
                width = 1.dp,
                color = LcBorder
            )
        ) {
            Text(
                text = "Julio 2026  ▣",
                modifier = Modifier.padding(
                    horizontal = 18.dp,
                    vertical = 11.dp
                ),
                color = LcTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        CircleActionButton(
            text = "→",
            onClick = {}
        )
    }
}

@Composable
private fun CircleActionButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(38.dp)
            .clickable(onClick = onClick),
        color = LcWhite,
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = LcBorder
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = LcNavy,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DayCard(
    day: CalendarDay,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(62.dp)
            .clickable(onClick = onClick),
        color = if (selected) {
            LcNavy
        } else {
            LcWhite
        },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                LcNavy
            } else {
                LcBorder
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 12.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.dayName,
                color = if (selected) {
                    LcWhite.copy(alpha = 0.72f)
                } else {
                    LcTextMuted
                },
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = day.dayNumber,
                color = if (selected) {
                    LcWhite
                } else {
                    LcTextPrimary
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun CourtSelector(
    selectedCourt: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleActionButton(
            text = "‹",
            onClick = onPrevious
        )

        Spacer(modifier = Modifier.width(12.dp))

        Surface(
            shape = RoundedCornerShape(50),
            color = LcWhite,
            border = BorderStroke(
                width = 1.dp,
                color = LcBorder
            )
        ) {
            Text(
                text = selectedCourt,
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 9.dp
                ),
                color = LcNavy,
                fontSize = 12.sp,
                letterSpacing = 0.7.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        CircleActionButton(
            text = "›",
            onClick = onNext
        )
    }
}

@Composable
private fun MatchCard(
    match: MockMatch
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = LcSurfaceSoft
        ),
        border = BorderStroke(
            width = 1.dp,
            color = LcBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = match.schedule,
                modifier = Modifier.width(68.dp),
                color = LcTextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = match.startTime,
                    color = LcRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                TeamLine(
                    name = match.homeTeam,
                    initials = initialsOf(match.homeTeam)
                )

                Spacer(modifier = Modifier.height(9.dp))

                Text(
                    text = "VS",
                    modifier = Modifier.fillMaxWidth(),
                    color = LcTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(9.dp))

                TeamLine(
                    name = match.awayTeam,
                    initials = initialsOf(match.awayTeam),
                    reverse = true
                )

                Spacer(modifier = Modifier.height(13.dp))

                HorizontalDivider(color = LcBorder)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = match.score,
                    modifier = Modifier.fillMaxWidth(),
                    color = LcNavy,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TeamLine(
    name: String,
    initials: String,
    reverse: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (reverse) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        if (!reverse) {
            TeamBadge(initials)

            Spacer(modifier = Modifier.width(9.dp))
        }

        Text(
            text = name,
            color = LcTextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        if (reverse) {
            Spacer(modifier = Modifier.width(9.dp))

            TeamBadge(initials)
        }
    }
}

@Composable
private fun TeamBadge(
    initials: String
) {
    Surface(
        modifier = Modifier.size(30.dp),
        color = LcNavy,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

private fun initialsOf(
    teamName: String
): String {
    return teamName
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") {
            it.first().uppercase()
        }
}

private fun previousCourt(
    current: String
): String {
    return when (current) {
        "CANCHA 1" -> "CANCHA 4"
        "CANCHA 2" -> "CANCHA 1"
        "CANCHA 3" -> "CANCHA 2"
        else -> "CANCHA 3"
    }
}

private fun nextCourt(
    current: String
): String {
    return when (current) {
        "CANCHA 1" -> "CANCHA 2"
        "CANCHA 2" -> "CANCHA 3"
        "CANCHA 3" -> "CANCHA 4"
        else -> "CANCHA 1"
    }
}