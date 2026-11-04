package com.example.medicioncloro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicioncloro.data.PreferenciasRepository
import com.example.medicioncloro.data.UsuarioRepository
import com.example.medicioncloro.model.Usuario
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SessionViewModel(private val prefs: PreferenciasRepository) : ViewModel() {

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    val notifActivas: StateFlow<Boolean> = prefs.notifActivas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        viewModelScope.launch {
            prefs.rolUsuario.collect { rol ->
                if (rol != null && _usuario.value == null) {
                    _usuario.value = UsuarioRepository.usuarios.firstOrNull { it.rol.name == rol }
                }
            }
        }
    }

    fun login(correo: String, password: String) {
        val user = UsuarioRepository.usuarios.find { it.correo == correo && it.password == password }
        if (user != null) {
            _usuario.value = user
            _errorLogin.value = null
            viewModelScope.launch { prefs.guardarRol(user.rol.name) }
        } else {
            _errorLogin.value = "Correo o contraseña incorrectos"
        }
    }

    fun limpiarError() {
        _errorLogin.value = null
    }

    fun setNotifActivas(v: Boolean) {
        viewModelScope.launch { prefs.guardarNotif(v) }
    }

    fun logout() {
        _usuario.value = null
        viewModelScope.launch { prefs.limpiarSesion() }
    }
}