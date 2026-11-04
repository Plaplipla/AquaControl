package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicioncloro.model.Rol
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.screen.components.getRoleColor
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.MedicionViewModel

@Composable
fun AlertasScreen(viewModel: MedicionViewModel, rol: Rol) {
    val alertas by viewModel.alertas.collectAsStateWithLifecycle()
    val puedeAtender = rol == Rol.SUPERVISOR

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        CorporateHeader(
            title = "Alertas",
            subtitle = if (puedeAtender) "Puede marcar alertas como atendidas"
                       else "Vista informativa — solo supervisores gestionan alertas",
            lineColor = getRoleColor(rol)
        )

        if (alertas.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(Modifier.padding(24.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Sin mediciones disponibles.", color = CorporateTextDark, fontSize = 16.sp)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(alertas, key = { it.id }) { alerta ->
                    // Determinamos si es crítica buscando palabras clave en el mensaje
                    val esCritica = alerta.mensaje.contains("excesivo") || alerta.mensaje.contains("insuficiente")

                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (alerta.atendida)
                                MaterialTheme.colorScheme.surfaceVariant
                            else if (esCritica)
                                CorporateRed.copy(alpha = 0.1f) // Fondo rojo suave para críticas
                            else
                                StatusWarning.copy(alpha = 0.1f) // Fondo naranja suave para advertencias
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (alerta.atendida) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (alerta.atendida) StatusNormal else if (esCritica) CorporateRed else StatusWarning
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(alerta.puntoNombre, fontWeight = FontWeight.Bold)
                                Text(alerta.mensaje, fontSize = 14.sp, fontWeight = if (esCritica) FontWeight.Bold else FontWeight.Normal)
                                Text(alerta.fechaHora, fontSize = 12.sp, color = CorporateTextGray)
                            }
                            if (!alerta.atendida && puedeAtender) {
                                TextButton(onClick = { viewModel.marcarAlertaAtendida(alerta) }) {
                                    Text("Atender", color = CorporateRed, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}