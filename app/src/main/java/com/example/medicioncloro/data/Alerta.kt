package com.example.medicioncloro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alertas")
data class Alerta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicionId: Int,
    val puntoNombre: String,
    val mensaje: String,
    val fechaHora: String,
    val atendida: Boolean = false
)