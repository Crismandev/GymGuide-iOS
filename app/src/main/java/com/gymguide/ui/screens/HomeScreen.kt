
package com.gymguide.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 4.dp
            ) {
                Text(
                    text = "GymGuide",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Bienvenido a GymGuide",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Organiza tus entrenamientos y consulta ejercicios."
            )

            MenuCard(
                titulo = "Ejercicios",
                descripcion = "Consulta grupos musculares y alternativas.",
                onClick = { onNavigate("grupos") }
            )

            MenuCard(
                titulo = "Mis rutinas",
                descripcion = "Organiza tus entrenamientos semanales.",
                onClick = { onNavigate("rutinas") }
            )

            MenuCard(
                titulo = "Temporizador",
                descripcion = "Controla tus tiempos de descanso.",
                onClick = { onNavigate("temporizador") }
            )

            MenuCard(
                titulo = "Calculadora IMC",
                descripcion = "Consulta tu índice de masa corporal.",
                onClick = { onNavigate("imc") }
            )
        }
    }
}

@Composable
fun MenuCard(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
