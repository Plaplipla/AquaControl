package com.example.medicioncloro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EstadoMedicion { NORMAL, ADVERTENCIA, CRITICO }

@Entity(tableName = "mediciones")
data class Medicion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val puntoId: Int,
    val cloroLibre: Double,
    val ph: Double,
    val temperatura: Double,
    val turbidez: Double = 0.0,
    val fechaHora: String,
    val estado: EstadoMedicion,
    val observaciones: String = "",
    val registradoPor: String = "",
    val evidenciaUri: String? = null,
    val sincronizado: Boolean = false
)