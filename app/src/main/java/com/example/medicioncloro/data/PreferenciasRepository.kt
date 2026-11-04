package com.example.medicioncloro.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferencias_cloro")

class PreferenciasRepository(private val context: Context) {
    companion object {
        private val ROL_USUARIO = stringPreferencesKey("rol_usuario")
        private val NOTIF_ACTIVAS = booleanPreferencesKey("notif_activas")
    }

    val rolUsuario = context.dataStore.data.map { it[ROL_USUARIO] }
    val notifActivas = context.dataStore.data.map { it[NOTIF_ACTIVAS] ?: true }

    suspend fun guardarRol(rol: String) {
        context.dataStore.edit { it[ROL_USUARIO] = rol }
    }

    suspend fun guardarNotif(activas: Boolean) {
        context.dataStore.edit { it[NOTIF_ACTIVAS] = activas }
    }

    suspend fun limpiarSesion() {
        context.dataStore.edit { it.remove(ROL_USUARIO) }
    }
}