package com.example.medicioncloro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicioncloro.data.MedicionRepository
import com.example.medicioncloro.data.PreferenciasRepository

class ViewModelFactory(
    private val medicionRepository: MedicionRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SessionViewModel::class.java) ->
                SessionViewModel(preferenciasRepository) as T
            modelClass.isAssignableFrom(MedicionViewModel::class.java) ->
                MedicionViewModel(medicionRepository) as T
            modelClass.isAssignableFrom(RegistroMedicionViewModel::class.java) ->
                RegistroMedicionViewModel(medicionRepository) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}