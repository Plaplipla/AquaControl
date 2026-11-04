package com.example.medicioncloro.model

data class RegistroMedicionUiState(
    val granjaId: Int = 0,
    val puntoId: Int = 0,
    val cloroLibre: String = "",
    val ph: String = "",
    val temperatura: String = "",
    val turbidez: String = "",
    val observaciones: String = "",
    val errores: RegistroErrores = RegistroErrores()
)

data class RegistroErrores(
    val granja: String? = null,
    val punto: String? = null,
    val cloro: String? = null,
    val ph: String? = null,
    val temperatura: String? = null,
    val turbidez: String? = null
)