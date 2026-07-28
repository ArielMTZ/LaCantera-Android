package com.example.lacantera.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lacantera.ui.dashboard.DashboardScreen
import com.example.lacantera.ui.login.LoginScreen
import com.example.lacantera.ui.publichome.PublicHomeScreen
import com.example.lacantera.ui.splash.SplashScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        // Splash inicial
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToPublicHome = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Inicio sin sesión
        composable(Routes.PUBLIC_HOME) {
            PublicHomeScreen(
                onLoginClick = {
                    navController.navigate(Routes.LOGIN)
                },
                onProgramsClick = {
                    navController.navigate(Routes.PROGRAMS)
                },
                onRulesClick = {
                    navController.navigate(Routes.RULES)
                },
                onPrivacyClick = {
                    navController.navigate(Routes.PRIVACY)
                },
                onTermsClick = {
                    navController.navigate(Routes.TERMS)
                },
                onSupportClick = {
                    navController.navigate(Routes.SUPPORT)
                }
            )
        }

        // Login
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.PUBLIC_HOME) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Dashboard privado
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onLogout = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.DASHBOARD) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Pantallas públicas provisionales
        composable(Routes.PROGRAMS) {
            PublicInformationScreen(
                title = "Programas",
                message = "Aquí mostraremos los programas y deportes disponibles.",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.RULES) {
            PublicInformationScreen(
                title = "Reglamento",
                message = "Aquí mostraremos el reglamento de La Cantera.",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PRIVACY) {
            PublicInformationScreen(
                title = "Aviso de privacidad",
                message = "Aquí mostraremos el aviso de privacidad.",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.TERMS) {
            PublicInformationScreen(
                title = "Términos y condiciones",
                message = "Aquí mostraremos los términos y condiciones.",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SUPPORT) {
            PublicInformationScreen(
                title = "Soporte",
                message = "Aquí mostraremos la información de contacto y soporte.",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun PublicInformationScreen(
    title: String,
    message: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title)

        Text(
            text = message,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        Button(
            onClick = onBackClick
        ) {
            Text(text = "Regresar")
        }
    }
}