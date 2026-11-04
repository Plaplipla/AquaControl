package com.example.medicioncloro.data

import com.example.medicioncloro.model.Rol
import com.example.medicioncloro.model.Usuario

object UsuarioRepository {
    val usuarios = listOf(
        Usuario(1, "Pedro Ramírez", "operario@empresa.cl", "123456", Rol.OPERARIO, listOf(1, 2)),
        Usuario(2, "María González", "supervisor@empresa.cl", "123456", Rol.SUPERVISOR, listOf(1, 3)),
        Usuario(3, "Carlos Muñoz", "jefatura@empresa.cl", "123456", Rol.JEFATURA)
    )
}