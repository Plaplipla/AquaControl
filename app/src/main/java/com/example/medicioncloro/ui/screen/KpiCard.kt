package com.example.medicioncloro.ui.screen

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun KpiCard(titulo: String, valor: String, color: Color, modifier: Modifier = Modifier) {
    val valorInt = valor.toIntOrNull()
    val texto = if (valorInt != null) {
        val animated by animateIntAsState(targetValue = valorInt, animationSpec = tween(800))
        animated.toString()
    } else valor
    Card(modifier = modifier) {
        Column(Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(texto, style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold, color = color)
            Text(titulo, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}