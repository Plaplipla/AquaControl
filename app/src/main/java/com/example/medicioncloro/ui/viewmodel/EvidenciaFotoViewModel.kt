package com.example.medicioncloro.ui.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EvidenciaUiState(
    val bitmap: Bitmap? = null,
    val uri: Uri? = null,
    val origen: String = ""
)

class EvidenciaFotoViewModel : ViewModel() {
    private val _estado = MutableStateFlow(EvidenciaUiState())
    val estado: StateFlow<EvidenciaUiState> = _estado.asStateFlow()

    fun fotoDesdeCamara(b: Bitmap?) {
        _estado.value = EvidenciaUiState(bitmap = b, origen = if (b != null) "Cámara" else "")
    }
    fun fotoDesdeGaleria(u: Uri?) {
        _estado.value = EvidenciaUiState(uri = u, origen = if (u != null) "Galería" else "")
    }
    fun limpiar() { _estado.value = EvidenciaUiState() }
}