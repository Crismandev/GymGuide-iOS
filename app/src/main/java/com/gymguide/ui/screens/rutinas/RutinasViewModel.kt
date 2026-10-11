
package com.gymguide.ui.screens.rutinas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gymguide.data.local.database.AppDatabase
import com.gymguide.data.local.entity.Rutina
import com.gymguide.data.repository.RutinaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class RutinasUiState(
    val cargando: Boolean = true,
    val rutinas: List<Rutina> = emptyList(),
    val filtro: String = "",
    val error: String? = null
)

class RutinasViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RutinaRepository(
        AppDatabase.getInstance(application).rutinaDao()
    )

    private val _uiState = MutableStateFlow(RutinasUiState())
    val uiState = _uiState.asStateFlow()

    private var observacionJob: Job? = null

    init {
        observarRutinas("")
    }

    fun observarRutinas(filtro: String) {
        observacionJob?.cancel()

        observacionJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                cargando = true,
                filtro = filtro,
                error = null
            )

            try {
                repository.observarRutinas(filtro).collectLatest { lista ->
                    _uiState.value = RutinasUiState(
                        cargando = false,
                        rutinas = lista,
                        filtro = filtro
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    error = e.message ?: "Error al cargar rutinas"
                )
            }
        }
    }

    fun activarRutina(idRutina: Int) {
        viewModelScope.launch {
            try {
                repository.activarRutina(idRutina)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al activar rutina"
                )
            }
        }
    }

    fun eliminarRutina(rutina: Rutina) {
        viewModelScope.launch {
            try {
                repository.eliminarRutina(rutina)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al eliminar rutina"
                )
            }
        }
    }
}
