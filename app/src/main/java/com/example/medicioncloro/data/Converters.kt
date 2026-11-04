package com.example.medicioncloro.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromEstado(estado: EstadoMedicion): String = estado.name

    @TypeConverter
    fun toEstado(value: String): EstadoMedicion = EstadoMedicion.valueOf(value)
}