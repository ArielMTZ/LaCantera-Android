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
import com.example.lacantera.ui.rules.RulesScreen
import com.example.lacantera.ui.matchdays.MatchdaysScreen
import com.example.lacantera.ui.login.DashboardType

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

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
                onNavigateToAdminDashboard = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onNavigateToRefereeDashboard = {
                    navController.navigate(Routes.DASHBOARD_REFEREE) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onNavigateToCaptainDashboard = {
                    navController.navigate(Routes.DASHBOARD_CAPTAIN) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }
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
                },
                onStandingsClick = {
                    // Posiciones
                },
                onTeamsClick = {
                    // Equipos
                },
                onRolesClick = {
                    navController.navigate(Routes.MATCHDAYS) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { dashboardType ->

                    val destination = when (dashboardType) {
                        DashboardType.ADMIN -> {
                            Routes.DASHBOARD
                        }

                        DashboardType.REFEREE -> {
                            Routes.DASHBOARD_REFEREE
                        }

                        DashboardType.CAPTAIN -> {
                            Routes.DASHBOARD_CAPTAIN
                        }
                    }

                    navController.navigate(destination) {
                        popUpTo(Routes.PUBLIC_HOME) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onBackToHome = {
                    navController.popBackStack()
                },
                onForgotPasswordClick = {

                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onLogout = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.DASHBOARD) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onSessionExpired = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.DASHBOARD) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.DASHBOARD_REFEREE) {
            DashboardScreen(
                onLogout = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.DASHBOARD_REFEREE) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onSessionExpired = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.DASHBOARD_REFEREE) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.DASHBOARD_CAPTAIN) {
            DashboardScreen(
                onLogout = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.DASHBOARD_CAPTAIN) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onSessionExpired = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.DASHBOARD_CAPTAIN) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

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
            RulesScreen(
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

        composable(Routes.MATCHDAYS) {
            MatchdaysScreen(
                onHomeClick = {
                    navController.navigate(Routes.PUBLIC_HOME) {
                        popUpTo(Routes.PUBLIC_HOME) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onRolesClick = {},
                onLoginClick = {
                    navController.navigate(Routes.LOGIN)
                },
                onPrivacyClick = {
                    navController.navigate(Routes.PRIVACY)
                },
                onTermsClick = {
                    navController.navigate(Routes.TERMS)
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

