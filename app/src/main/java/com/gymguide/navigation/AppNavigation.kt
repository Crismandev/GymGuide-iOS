
package com.gymguide.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination

import com.gymguide.ui.screens.rutinas.RutinasScreen

import com.gymguide.ui.screens.home.HomeScreen
import com.gymguide.ui.screens.grupos.GruposScreen
import com.gymguide.ui.screens.ejercicios.EjerciciosScreen
import com.gymguide.ui.screens.detalle.DetalleEjercicioScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = androidx.compose.ui.unit.Dp(0f)) {
                val tabs = listOf(
                    Triple("home", "Inicio", "⌂"),
                    Triple("grupos", "Ejercicios", "✣"),
                    Triple("rutinas", "Mis rutinas", "⚒")
                )
                tabs.forEach { (route, label, icon) ->
                    val selected = currentRoute == route ||
                        (route == "grupos" && currentRoute in setOf("grupos", "ejercicios/{idGrupo}", "detalle/{idEjercicio}"))
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text(icon) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color(0xFF718096),
                            unselectedTextColor = Color(0xFF718096),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
      NavHost(
        navController = navController,
        startDestination = "home",
        modifier = androidx.compose.ui.Modifier.padding(innerPadding)
    ) {

        // INICIO
        composable("home") {
            HomeScreen(
                onNavigate = { ruta ->
                    navController.navigate(ruta)
                }
            )
        }

        // GRUPOS MUSCULARES
        composable("grupos") {
            GruposScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onGrupoSeleccionado = { idGrupo ->
                    navController.navigate("ejercicios/$idGrupo")
                }
            )
        }

        // LISTADO DE EJERCICIOS
        composable("ejercicios/{idGrupo}") { entrada ->

            val idGrupo = entrada.arguments
                ?.getString("idGrupo")
                ?.toIntOrNull()

            if (idGrupo != null) {
                EjerciciosScreen(
                    idGrupo = idGrupo,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onEjercicioSeleccionado = { idEjercicio ->
                        navController.navigate(
                            "detalle/$idEjercicio"
                        )
                    }
                )
            }
        }

        // DETALLE DEL EJERCICIO Y ALTERNATIVAS
        composable("detalle/{idEjercicio}") { entrada ->

            val idEjercicio = entrada.arguments
                ?.getString("idEjercicio")
                ?.toIntOrNull()

            if (idEjercicio != null) {
                DetalleEjercicioScreen(
                    idEjercicio = idEjercicio,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onVerAlternativa = { idAlternativo ->
                        navController.navigate(
                            "detalle/$idAlternativo"
                        )
                    }
                )
            }
        }

        // RUTINAS

        composable("rutinas") {
            RutinasScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onCrearRutina = {
                    navController.navigate("crear_rutina")
                },
                onEditarRutina = { idRutina ->
                    navController.navigate("editar_rutina/$idRutina")
                }
            )
        }

        composable("crear_rutina") {
            ModuloScreen(
                titulo = "Crear rutina",
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable("editar_rutina/{idRutina}") { entrada ->
            val idRutina = entrada.arguments
                ?.getString("idRutina")
                ?.toIntOrNull()

            if (idRutina != null) {
                ModuloScreen(
                    titulo = "Editar rutina $idRutina",
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // TEMPORIZADOR
        composable("temporizador") {
            ModuloScreen(
                titulo = "Temporizador",
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // CALCULADORA IMC
        composable("imc") {
            ModuloScreen(
                titulo = "Calculadora IMC",
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
      }
    }
}
