
package com.gymguide.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gymguide.ui.screens.home.HomeScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable("grupos") {
            ModuloScreen(
                titulo = "Grupos musculares",
                onVolver = { navController.popBackStack() }
            )
        }

        composable("rutinas") {
            ModuloScreen(
                titulo = "Mis rutinas",
                onVolver = { navController.popBackStack() }
            )
        }

        composable("temporizador") {
            ModuloScreen(
                titulo = "Temporizador",
                onVolver = { navController.popBackStack() }
            )
        }

        composable("imc") {
            ModuloScreen(
                titulo = "Calculadora IMC",
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
