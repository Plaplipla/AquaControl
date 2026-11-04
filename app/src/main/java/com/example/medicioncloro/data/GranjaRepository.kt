package com.example.medicioncloro.data

import com.example.medicioncloro.model.Granja
import com.example.medicioncloro.model.PuntoMedicion

object GranjaRepository {
    val granjas = listOf(
        Granja(1, "Granja El Roble", "Melipilla"),
        Granja(2, "Granja Los Aromos", "San Antonio"),
        Granja(3, "Granja La Esperanza", "Talagante")
    )

    val puntos = listOf(
        PuntoMedicion(1, 1, "Bebedero Galpón A-1", "Bebedero"),
        PuntoMedicion(2, 1, "Bebedero Galpón A-2", "Bebedero"),
        PuntoMedicion(3, 1, "Estanque Principal G1", "Estanque"),
        PuntoMedicion(4, 1, "Línea Distribución G1", "Distribución"),
        PuntoMedicion(5, 2, "Bebedero Galpón B-1", "Bebedero"),
        PuntoMedicion(6, 2, "Bebedero Galpón B-2", "Bebedero"),
        PuntoMedicion(7, 2, "Estanque Principal G2", "Estanque"),
        PuntoMedicion(8, 2, "Línea Distribución G2", "Distribución"),
        PuntoMedicion(9, 3, "Bebedero Galpón C-1", "Bebedero"),
        PuntoMedicion(10, 3, "Bebedero Galpón C-2", "Bebedero"),
        PuntoMedicion(11, 3, "Estanque Principal G3", "Estanque"),
        PuntoMedicion(12, 3, "Línea Distribución G3", "Distribución")
    )

    fun obtenerNombrePunto(id: Int): String =
        puntos.find { it.id == id }?.nombre ?: "Desconocido"
}