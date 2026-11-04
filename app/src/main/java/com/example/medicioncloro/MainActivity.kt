package com.example.medicioncloro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medicioncloro.data.AppDatabase
import com.example.medicioncloro.data.MedicionRepository
import com.example.medicioncloro.data.PreferenciasRepository
import com.example.medicioncloro.ui.navigation.AppNavigation
import com.example.medicioncloro.ui.screen.LoginScreen
import com.example.medicioncloro.ui.theme.MedicionCloroTheme
import com.example.medicioncloro.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val prefs by lazy { PreferenciasRepository(this) }
    private val medicionRepo by lazy {
        MedicionRepository(
            database.medicionDao(),
            database.alertaDao(),
            database.desviacionDao()
        )
    }
    private val factory by lazy { ViewModelFactory(medicionRepo, prefs) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicionCloroTheme {
                Surface {
                    val sessionVM: SessionViewModel = viewModel(factory = factory)
                    val medicionVM: MedicionViewModel = viewModel(factory = factory)
                    val registroVM: RegistroMedicionViewModel = viewModel(factory = factory)
                    val usuario by sessionVM.usuario.collectAsStateWithLifecycle()

                    AnimatedContent(
                        targetState = usuario,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(400)) togetherWith
                                    fadeOut(animationSpec = tween(400))
                        },
                        label = "login_transition"
                    ) { user ->
                        if (user == null) LoginScreen(sessionVM)
                        else AppNavigation(sessionVM, medicionVM, registroVM)
                    }
                }
            }
        }
    }
}