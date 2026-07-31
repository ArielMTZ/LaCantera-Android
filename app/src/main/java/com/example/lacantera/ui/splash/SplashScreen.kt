package com.example.lacantera.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplashScreen(
    onNavigateToPublicHome: () -> Unit,
    onNavigateToAdminDashboard: () -> Unit,
    onNavigateToRefereeDashboard: () -> Unit,
    onNavigateToCaptainDashboard: () -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.destination) {
        when (uiState.destination) {
            SplashDestination.PUBLIC_HOME -> {
                onNavigateToPublicHome()
            }

            SplashDestination.DASHBOARD_ADMIN -> {
                onNavigateToAdminDashboard()
            }

            SplashDestination.DASHBOARD_REFEREE -> {
                onNavigateToRefereeDashboard()
            }

            SplashDestination.DASHBOARD_CAPTAIN -> {
                onNavigateToCaptainDashboard()
            }

            null -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF071B4A)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LC",
                color = Color(0xFF071B4A),
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "LA CANTERA",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Centro Deportivo",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(34.dp),
                color = Color.White
            )
        }
    }
}