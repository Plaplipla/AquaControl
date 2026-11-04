package com.example.medicioncloro.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val password: String,
    val rol: Rol,
    val granjasAsignadas: List<Int> = emptyList()
)