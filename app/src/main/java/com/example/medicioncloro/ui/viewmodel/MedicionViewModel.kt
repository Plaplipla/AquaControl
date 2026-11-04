package com.example.medicioncloro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicioncloro.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicionViewModel(private val repo: MedicionRepository) : ViewModel() {

    // Flows que observan la base de datos en tiempo real
    val mediciones: StateFlow<List<Medicion>> = repo.observarMediciones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alertas: StateFlow<List<Alerta>> = repo.observarAlertas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val desviaciones: StateFlow<List<Desviacion>> = repo.observarDesviaciones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Datos estáticos de granjas y puntos
    val granjas = GranjaRepository.granjas
    val puntos = GranjaRepository.puntos

    init {
        // Inserta los datos de prueba si la base de datos está vacía
        viewModelScope.launch { repo.seedIfEmpty() }
    }

    // --- Funciones de utilidad ---

    fun obtenerNombrePunto(puntoId: Int): String =
        GranjaRepository.obtenerNombrePunto(puntoId)

    /** Obtiene solo la última medición de cada punto para el Dashboard (Estado Actual) */
    fun obtenerMedicionesActuales(): List<Medicion> {
        return mediciones.value
            .groupBy { it.puntoId }
            .map { (_, lista) -> lista.maxByOrNull { it.fechaHora }!! }
    }

    // --- Funciones de acción ---

    fun marcarAlertaAtendida(alerta: Alerta) {
        viewModelScope.launch { repo.marcarAlertaAtendida(alerta) }
    }

    fun actualizarDesviacion(desviacion: Desviacion) {
        viewModelScope.launch { repo.actualizarDesviacion(desviacion) }
    }

    /** Sincroniza las mediciones que estaban pendientes (Simulación de reconexión) */
    fun sincronizarMediciones() {
        viewModelScope.launch { repo.sincronizarMediciones() }
    }

    /**
     * Resuelve una desviación guardando una nueva medición con los 4 parámetros.
     */
    fun resolverDesviacion(
        desviacion: Desviacion,
        cloro: Double,
        ph: Double,
        temperatura: Double,
        turbidez: Double,
        supervisor: String
    ) {
        viewModelScope.launch {
            // Buscamos la medición original para obtener el puntoId
            val medicionOriginal = mediciones.value.find { it.id == desviacion.medicionId }
            val puntoId = medicionOriginal?.puntoId ?: 1 // Fallback por seguridad

            repo.resolverDesviacionConMedicion(
                desviacion = desviacion,
                nuevoCloro = cloro,
                nuevoPh = ph,
                nuevaTemperatura = temperatura,
                nuevaTurbidez = turbidez,
                supervisorNombre = supervisor,
                puntoId = puntoId
            )
        }
    }
}