
package com.gymguide.ui.screens.rutinas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gymguide.data.local.entity.Rutina

@Composable
fun RutinasScreen(
    onVolver: () -> Unit,
    onCrearRutina: () -> Unit,
    onEditarRutina: (Int) -> Unit,
    viewModel: RutinasViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var rutinaEliminar by remember {
        mutableStateOf<Rutina?>(null)
    }

    LaunchedEffect(busqueda) {
        viewModel.observarRutinas(busqueda)
    }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }

                    Text(
                        "Mis rutinas",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onCrearRutina
            ) {
                Text("+")
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar rutina") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            when {
                estado.cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                estado.error != null -> {
                    Column {
                        Text(estado.error ?: "Error desconocido")

                        Button(
                            onClick = {
                                viewModel.observarRutinas(busqueda)
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                estado.rutinas.isEmpty() -> {
                    Text(
                        "No hay rutinas registradas. Presiona + para crear una."
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = estado.rutinas,
                            key = { it.idRutina }
                        ) { rutina ->

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        rutina.nombre,
                                        style =
                                            MaterialTheme.typography.titleLarge
                                    )

                                    Text(
                                        "Objetivo: ${rutina.objetivo}"
                                    )

                                    Text(
                                        "Días: ${rutina.cantidadDias}"
                                    )

                                    if (rutina.activa == 1) {
                                        Text(
                                            "Rutina activa",
                                            color =
                                                MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Row(
                                        horizontalArrangement =
                                            Arrangement.spacedBy(8.dp)
                                    ) {
                                        TextButton(
                                            onClick = {
                                                onEditarRutina(
                                                    rutina.idRutina
                                                )
                                            }
                                        ) {
                                            Text("Editar")
                                        }

                                        if (rutina.activa != 1) {
                                            TextButton(
                                                onClick = {
                                                    viewModel.activarRutina(
                                                        rutina.idRutina
                                                    )
                                                }
                                            ) {
                                                Text("Activar")
                                            }
                                        }

                                        TextButton(
                                            onClick = {
                                                rutinaEliminar = rutina
                                            }
                                        ) {
                                            Text("Eliminar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    rutinaEliminar?.let { rutina ->
        AlertDialog(
            onDismissRequest = {
                rutinaEliminar = null
            },
            title = {
                Text("Eliminar rutina")
            },
            text = {
                Text(
                    "¿Estás segura de eliminar '${rutina.nombre}'? " +
                            "También se eliminarán sus días y ejercicios."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarRutina(rutina)
                        rutinaEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        rutinaEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
