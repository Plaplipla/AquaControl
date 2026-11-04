package com.example.medicioncloro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicionDao {
    @Insert
    suspend fun insertar(medicion: Medicion): Long

    @Update
    suspend fun actualizar(medicion: Medicion)

    @Delete
    suspend fun eliminar(medicion: Medicion)

    @Query("SELECT * FROM mediciones ORDER BY fechaHora DESC")
    fun observarTodas(): Flow<List<Medicion>>

    @Query("SELECT * FROM mediciones WHERE id = :id")
    suspend fun buscarPorId(id: Int): Medicion?

    @Query("SELECT COUNT(*) FROM mediciones")
    suspend fun contar(): Int


    @Query("UPDATE mediciones SET sincronizado = 1 WHERE sincronizado = 0")
    suspend fun sincronizarTodas()
}