package com.example.medicioncloro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertaDao {
    @Insert
    suspend fun insertar(alerta: Alerta): Long

    @Update
    suspend fun actualizar(alerta: Alerta)

    @Query("SELECT * FROM alertas ORDER BY atendida ASC, fechaHora DESC")
    fun observarTodas(): Flow<List<Alerta>>

    @Query("SELECT COUNT(*) FROM alertas WHERE atendida = 0")
    suspend fun contarActivas(): Int

    @Query("DELETE FROM alertas WHERE medicionId = :medicionId")
    suspend fun eliminarPorMedicion(medicionId: Int)
}