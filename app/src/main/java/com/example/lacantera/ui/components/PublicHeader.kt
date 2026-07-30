package com.example.lacantera.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lacantera.R
import com.example.lacantera.ui.theme.LcNavy
import com.example.lacantera.ui.theme.LcWhite

@Composable
fun PublicHeader(
    selectedTab: PublicTab,
    onHomeClick: () -> Unit,
    onStandingsClick: () -> Unit,
    onTeamsClick: () -> Unit,
    onRolesClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LcNavy)
            .statusBarsPadding()
    ) {
        HeaderTopBar(
            onLoginClick = onLoginClick
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFF315A9C)
        )

        PublicNavigation(
            selectedTab = selectedTab,
            onHomeClick = onHomeClick,
            onStandingsClick = onStandingsClick,
            onTeamsClick = onTeamsClick,
            onRolesClick = onRolesClick
        )
    }
}

@Composable
private fun HeaderTopBar(
    onLoginClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(18.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de La Cantera",
            modifier = Modifier
                .width(54.dp)
                .height(42.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "La Cantera",
            modifier = Modifier.weight(1f),
            color = LcWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedButton(
            onClick = onLoginClick,
            modifier = Modifier.height(50.dp),
            shape = RoundedCornerShape(13.dp),
            border = BorderStroke(
                width = 1.dp,
                color = LcWhite.copy(alpha = 0.28f)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = LcWhite.copy(alpha = 0.06f),
                contentColor = LcWhite
            ),
            contentPadding = PaddingValues(
                horizontal = 17.dp,
                vertical = 8.dp
            )
        ) {
            Text(
                text = "Iniciar sesión",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(18.dp))
    }
}