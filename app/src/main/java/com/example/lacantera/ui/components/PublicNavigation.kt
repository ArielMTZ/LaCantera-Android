package com.example.lacantera.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lacantera.ui.theme.LcNavyDark
import com.example.lacantera.ui.theme.LcWhite

enum class PublicTab {
    HOME,
    STANDINGS,
    TEAMS,
    MATCHDAYS
}

@Composable
fun PublicNavigation(
    selectedTab: PublicTab,
    onHomeClick: () -> Unit,
    onStandingsClick: () -> Unit,
    onTeamsClick: () -> Unit,
    onRolesClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = LcNavyDark
    ) {
        Column {
            HorizontalDivider(
                color = Color(0xFF315A9C),
                thickness = 1.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationItem(
                    label = "Inicio",
                    icon = Icons.Filled.Home,
                    selected = selectedTab == PublicTab.HOME,
                    onClick = onHomeClick
                )

                NavigationItem(
                    label = "Posiciones",
                    icon = Icons.Filled.Leaderboard,
                    selected = selectedTab == PublicTab.STANDINGS,
                    onClick = onStandingsClick
                )

                NavigationItem(
                    label = "Equipos",
                    icon = Icons.Filled.Groups,
                    selected = selectedTab == PublicTab.TEAMS,
                    onClick = onTeamsClick
                )

                NavigationItem(
                    label = "Roles",
                    icon = Icons.Filled.CalendarMonth,
                    selected = selectedTab == PublicTab.MATCHDAYS,
                    onClick = onRolesClick
                )
            }
        }
    }
}

@Composable
private fun RowScope.NavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val selectedColor = Color(0xFF62A3FF)

    val contentColor = if (selected) {
        selectedColor
    } else {
        LcWhite.copy(alpha = 0.43f)
    }

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(
                top = 12.dp,
                bottom = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(23.dp),
            tint = contentColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )

        Spacer(modifier = Modifier.height(7.dp))

        Box(
            modifier = Modifier
                .width(28.dp)
                .height(2.dp),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = selectedColor,
                    shape = CircleShape
                ) {}
            }
        }
    }
}