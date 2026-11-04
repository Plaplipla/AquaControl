package com.example.medicioncloro.data

import com.example.medicioncloro.model.formatearCloro
import com.example.medicioncloro.model.calcularEstado
import kotlinx.coroutines.flow.Flow

class MedicionRepository(
    private val medicionDao: MedicionDao,
    private val alertaDao: AlertaDao,
    private val desviacionDao: DesviacionDao
) {
    fun observarMediciones(): Flow<List<Medicion>> = medicionDao.observarTodas()
    fun observarAlertas(): Flow<List<Alerta>> = alertaDao.observarTodas()
    fun observarDesviaciones(): Flow<List<Desviacion>> = desviacionDao.observarTodas()

    /** Inserta medición y genera automáticamente alerta/desviación si es necesaria. */
    suspend fun agregarMedicion(medicion: Medicion, puntoNombre: String): Long {
        val id = medicionDao.insertar(medicion)

        // Si el estado es Advertencia o Crítico, creamos alerta y desviación
        if (medicion.estado != EstadoMedicion.NORMAL) {
            val tipo = if (medicion.cloroLibre < 3.0) "Cloro insuficiente" else "Cloro excesivo"

            alertaDao.insertar(
                Alerta(
                    medicionId = id.toInt(),
                    puntoNombre = puntoNombre,
                    mensaje = "$tipo: ${formatearCloro(medicion.cloroLibre)}",
                    fechaHora = medicion.fechaHora,
                    atendida = false
                )
            )

            desviacionDao.insertar(
                Desviacion(
                    medicionId = id.toInt(),
                    puntoNombre = puntoNombre,
                    descripcion = "$tipo (${formatearCloro(medicion.cloroLibre)})",
                    fechaDeteccion = medicion.fechaHora,
                    estado = "PENDIENTE"
                )
            )
        }
        return id
    }

    /** Actualiza la alerta y sincroniza automáticamente la desviación asociada a "EN_PROCESO". */
    suspend fun marcarAlertaAtendida(alerta: Alerta) {
        alertaDao.actualizar(alerta.copy(atendida = true))
        desviacionDao.actualizarEstadoPorMedicion(alerta.medicionId, "EN_PROCESO")
    }

    /** Actualiza una desviación sin generar una nueva medición (ej. de PENDIENTE a EN_PROCESO). */
    suspend fun actualizarDesviacion(desviacion: Desviacion) {
        desviacionDao.actualizar(desviacion)
    }

    /**
     * Resuelve la desviación creando una nueva medición de validación.
     * Ahora recibe también temperatura y turbidez.
     */
    suspend fun resolverDesviacionConMedicion(
        desviacion: Desviacion,
        nuevoCloro: Double,
        nuevoPh: Double,
        nuevaTemperatura: Double,
        nuevaTurbidez: Double,
        supervisorNombre: String,
        puntoId: Int
    ) {
        // 1. Guardamos la nueva medición corregida
        val nuevaMedicion = Medicion(
            puntoId = puntoId,
            cloroLibre = nuevoCloro,
            ph = nuevoPh,
            temperatura = nuevaTemperatura,
            turbidez = nuevaTurbidez,
            fechaHora = "2026-10-31 12:00",
            estado = calcularEstado(nuevoCloro),
            observaciones = "Medición de resolución: ${desviacion.accionRegistrada}",
            registradoPor = supervisorNombre,
            evidenciaUri = desviacion.evidenciaUri,
            sincronizado = false
        )
        medicionDao.insertar(nuevaMedicion)

        // 2. Actualizamos la desviación a RESUELTA
        desviacionDao.actualizar(desviacion.copy(estado = "RESUELTA"))

        // 3. ELIMINAMOS LA ALERTA de la base de datos
        alertaDao.eliminarPorMedicion(desviacion.medicionId)
    }

    /** Sincroniza las mediciones que estaban pendientes (Simulación de reconexión) */
    suspend fun sincronizarMediciones() {
        medicionDao.sincronizarTodas()
    }

    /** Si la BD está vacía, inserta datos demo. */
    suspend fun seedIfEmpty() {
        if (medicionDao.contar() > 0) return
        val iniciales = listOf(
            Triple(1, 4.20, 7.2), Triple(2, 1.85, 6.8), Triple(3, 5.50, 8.2),
            Triple(4, 4.05, 7.0), Triple(5, 6.80, 7.4), Triple(6, 3.95, 7.1),
            Triple(7, 2.50, 6.9), Triple(8, 4.70, 7.3)
        )
        val puntosNombres = listOf(
            "Bebedero Galpón A-1", "Bebedero Galpón A-2", "Estanque Principal G1", "Línea Distribución G1",
            "Bebedero Galpón B-1", "Bebedero Galpón B-2", "Estanque Principal G2", "Línea Distribución G2"
        )
        iniciales.forEachIndexed { i, (puntoId, cloro, ph) ->
            val m = Medicion(
                puntoId = puntoId,
                cloroLibre = cloro,
                ph = ph,
                temperatura = 18.0 + i * 0.5,
                turbidez = 2.0 + i * 0.3,
                fechaHora = "2026-06-10 0${8 + i / 4}:${(30 + i * 15) % 60}",
                estado = calcularEstado(cloro),
                observaciones = "",
                registradoPor = "Pedro Ramírez",
                sincronizado = true // Los datos iniciales ya están "sincronizados"
            )
            agregarMedicion(m, puntosNombres[i])
        }
    }
}