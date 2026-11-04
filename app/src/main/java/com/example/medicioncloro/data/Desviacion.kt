package com.example.medicioncloro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "desviaciones")
data class Desviacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicionId: Int,
    val puntoNombre: String,
    val descripcion: String,
    val fechaDeteccion: String,
    val estado: String,  // PENDIENTE | EN_PROCESO | RESUELTA
    val accionRegistrada: String = "",
    val supervisorAsignado: String = "",
    val evidenciaUri: String? = null
)