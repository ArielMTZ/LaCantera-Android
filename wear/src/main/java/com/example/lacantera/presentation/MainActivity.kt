package com.example.lacantera.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.example.lacantera.presentation.home.WearHomeScreen
import com.example.lacantera.presentation.home.WearHomeViewModel
import com.example.lacantera.presentation.session.WaitingForSessionScreen
import com.example.lacantera.presentation.theme.LaCanteraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {
            LaCanteraWearApp()
        }
    }
}

@Composable
fun LaCanteraWearApp(
    viewModel: WearHomeViewModel = viewModel()
) {
    val session by viewModel.session.collectAsState()

    LaCanteraTheme {
        if (session.isLoggedIn) {
            WearHomeScreen(
                nombreCorto =
                    session.nombreCorto,
                tipoUsuario =
                    session.tipoUsuario,
                onLogoutClick =
                    viewModel::logout
            )
        } else {
            WaitingForSessionScreen()
        }
    }
}

@WearPreviewDevices
@Composable
private fun LaCanteraWearPreview() {
    LaCanteraTheme {
        WaitingForSessionScreen()
    }
}