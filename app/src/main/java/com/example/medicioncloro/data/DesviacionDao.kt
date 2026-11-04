package com.example.medicioncloro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DesviacionDao {
    @Insert
    suspend fun insertar(desviacion: Desviacion): Long

    @Update
    suspend fun actualizar(desviacion: Desviacion)

    @Query("SELECT * FROM desviaciones ORDER BY estado ASC, fechaDeteccion DESC")
    fun observarTodas(): Flow<List<Desviacion>>

    @Query("SELECT COUNT(*) FROM desviaciones WHERE estado != 'RESUELTA'")
    suspend fun contarPendientes(): Int

    @Query("UPDATE desviaciones SET estado = :nuevoEstado WHERE medicionId = :medicionId AND estado = 'PENDIENTE'")
    suspend fun actualizarEstadoPorMedicion(medicionId: Int, nuevoEstado: String)
}