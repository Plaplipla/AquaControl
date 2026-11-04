package com.example.medicioncloro.model

import com.example.medicioncloro.data.EstadoMedicion
import java.util.Locale

fun formatearCloro(valor: Double): String =
    String.format(Locale.US, "%.2f ppm", valor)

fun formatearPh(valor: Double): String =
    String.format(Locale.US, "%.2f", valor)

fun formatearTemp(valor: Double): String =
    String.format(Locale.US, "%.1f °C", valor)

fun formatearTurbidez(valor: Double): String =
    String.format(Locale.US, "%.1f NTU", valor)

/** Rangos oficiales: Normal 3.00-5.00 | Adv 2.00-3.00 o 5.00-6.00 | Crítico <2 o >6 */
fun calcularEstado(cloro: Double): EstadoMedicion = when {
    cloro < 2.00 || cloro > 6.00 -> EstadoMedicion.CRITICO
    cloro < 3.00 || cloro > 5.00 -> EstadoMedicion.ADVERTENCIA
    else -> EstadoMedicion.NORMAL
}