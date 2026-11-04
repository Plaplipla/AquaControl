package com.example.medicioncloro.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = PrimaryWaterBlue,
    onPrimary = Color.White,
    secondary = CorporateDarkBlue,
    onSecondary = Color.White,
    background = CorporateBackground,
    onBackground = CorporateTextDark,
    surface = Color.White,
    onSurface = CorporateTextDark,
    error = CorporateRed
)

@Composable
fun MedicionCloroTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}