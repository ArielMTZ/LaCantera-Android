package com.example.lacantera.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .background(Color(0xFF031334)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            label = "Inicio",
            icon = Icons.Rounded.Home,
            selected = selectedTab == PublicTab.HOME,
            onClick = onHomeClick
        )

        NavigationItem(
            label = "Posiciones",
            icon = Icons.Rounded.BarChart,
            selected = selectedTab == PublicTab.STANDINGS,
            onClick = onStandingsClick
        )

        NavigationItem(
            label = "Equipos",
            icon = Icons.Rounded.Groups,
            selected = selectedTab == PublicTab.TEAMS,
            onClick = onTeamsClick
        )

        NavigationItem(
            label = "Roles",
            icon = Icons.Rounded.CalendarMonth,
            selected = selectedTab == PublicTab.MATCHDAYS,
            onClick = onRolesClick
        )
    }
}

@Composable
private fun RowScope.NavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val selectedColor = Color(0xFF5AA7FF)
    val unselectedColor = Color(0xFF8290AA)
    val itemColor = if (selected) selectedColor else unselectedColor

    Column(
        modifier = Modifier
            .weight(1f)
            .height(88.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(25.dp),
            tint = itemColor
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = itemColor,
            fontSize = 11.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(34.dp)
                .height(3.dp)
                .background(
                    color = if (selected) {
                        selectedColor
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(50)
                )
        )
    }
}