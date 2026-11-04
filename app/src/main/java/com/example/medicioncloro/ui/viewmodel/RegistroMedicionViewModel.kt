package com.example.medicioncloro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicioncloro.data.*
import com.example.medicioncloro.model.RegistroErrores
import com.example.medicioncloro.model.RegistroMedicionUiState
import com.example.medicioncloro.model.calcularEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroMedicionViewModel(private val repo: MedicionRepository) : ViewModel() {

    private val _estado = MutableStateFlow(RegistroMedicionUiState())
    val estado: StateFlow<RegistroMedicionUiState> = _estado.asStateFlow()

    fun onGranjaChange(id: Int) = _estado.update {
        it.copy(granjaId = id, puntoId = 0,
            errores = it.errores.copy(granja = null, punto = null))
    }

    fun onPuntoChange(id: Int) = _estado.update {
        it.copy(puntoId = id, errores = it.errores.copy(punto = null))
    }

    fun onCloroChange(v: String) = _estado.update {
        it.copy(cloroLibre = v, errores = it.errores.copy(cloro = null))
    }

    fun onPhChange(v: String) = _estado.update {
        it.copy(ph = v, errores = it.errores.copy(ph = null))
    }

    fun onTemperaturaChange(v: String) = _estado.update {
        it.copy(temperatura = v, errores = it.errores.copy(temperatura = null))
    }

    fun onTurbidezChange(v: String) = _estado.update {
        it.copy(turbidez = v, errores = it.errores.copy(turbidez = null))
    }

    fun onObservacionesChange(v: String) = _estado.update {
        it.copy(observaciones = v)
    }

    fun validar(): Boolean {
        val a = _estado.value
        val err = RegistroErrores(
            granja = if (a.granjaId == 0) "Seleccione una granja" else null,
            punto = if (a.puntoId == 0) "Seleccione un punto" else null,
            cloro = when {
                a.cloroLibre.isBlank() -> "Campo obligatorio"
                a.cloroLibre.toDoubleOrNull() == null -> "Debe ser número"
                a.cloroLibre.toDouble() < 0 -> "No negativo"
                a.cloroLibre.toDouble() > 10 -> "Máximo 10.0 ppm"
                else -> null
            },
            ph = when {
                a.ph.isBlank() -> "Campo obligatorio"
                a.ph.toDoubleOrNull() == null -> "Debe ser número"
                a.ph.toDouble() < 0 || a.ph.toDouble() > 14 -> "pH entre 0 y 14"
                else -> null
            },
            temperatura = if (a.temperatura.isNotBlank() &&
                a.temperatura.toDoubleOrNull() == null) "Debe ser número" else null,
            turbidez = if (a.turbidez.isNotBlank() &&
                a.turbidez.toDoubleOrNull() == null) "Debe ser número" else null
        )
        val hay = listOfNotNull(err.granja, err.punto, err.cloro, err.ph,
            err.temperatura, err.turbidez).isNotEmpty()
        _estado.update { it.copy(errores = err) }
        return !hay
    }

    fun guardarMedicion(nombreOperario: String, evidenciaUri: String?,
                        onSuccess: () -> Unit) {
        if (!validar()) return
        val a = _estado.value
        val cloro = a.cloroLibre.toDouble()
        val ph = a.ph.toDouble()
        val nueva = Medicion(
            puntoId = a.puntoId,
            cloroLibre = cloro,
            ph = ph,
            temperatura = a.temperatura.toDoubleOrNull() ?: 0.0,
            turbidez = a.turbidez.toDoubleOrNull() ?: 0.0,
            fechaHora = "2026-06-10 10:00",
            estado = calcularEstado(cloro),
            observaciones = a.observaciones,
            registradoPor = nombreOperario,
            evidenciaUri = evidenciaUri
        )
        viewModelScope.launch {
            val puntoNombre = GranjaRepository.obtenerNombrePunto(a.puntoId)
            repo.agregarMedicion(nueva, puntoNombre)
            _estado.value = RegistroMedicionUiState()
            onSuccess()
        }
    }
}