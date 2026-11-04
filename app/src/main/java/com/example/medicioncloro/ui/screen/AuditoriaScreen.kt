package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
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
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.MedicionViewModel

@Composable
fun AuditoriaScreen(viewModel: MedicionViewModel) {
    val desviaciones by viewModel.desviaciones.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        CorporateHeader(
            title = "Auditoría",
            subtitle = "Trazabilidad de desviaciones y responsables",
            lineColor = RoleJefaturaPurple
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(desviaciones) { d ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        // CABECERA: Punto y Estado
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(d.puntoNombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CorporateTextDark)

                            val (colorEstado, textEstado) = when (d.estado) {
                                "PENDIENTE" -> CorporateRed to "PENDIENTE"
                                "EN_PROCESO" -> StatusWarning to "EN PROCESO"
                                else -> StatusNormal to "RESUELTA"
                            }

                            Surface(color = colorEstado.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = textEstado,
                                    color = colorEstado,
                                    fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // PROBLEMA ORIGINAL
                        Text("Problema: ${d.descripcion}", fontSize = 14.sp, color = CorporateTextGray)

                        // ACCIÓN REALIZADA
                        if (d.accionRegistrada.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Text("Acción: ${d.accionRegistrada}", fontSize = 14.sp, color = CorporateTextDark, fontWeight = FontWeight.Medium)
                        }

                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                        Spacer(Modifier.height(8.dp))

                        // PIE DE TARJETA: Fecha y Responsable (Apilados verticalmente)
                        Column(Modifier.fillMaxWidth()) {

                            // Fecha (Color más oscuro y legible)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = CorporateTextGray, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "Detectado: ${d.fechaDeteccion}",
                                    fontSize = 12.sp,
                                    color = CorporateTextGray
                                )
                            }

                            Spacer(Modifier.height(4.dp))

                            val nombreResponsable = if (d.supervisorAsignado.isNotBlank()) d.supervisorAsignado else "Sistema (Dato antiguo)"
                            val colorResponsable = if (d.supervisorAsignado.isNotBlank()) CorporateDarkBlue else CorporateRed

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = colorResponsable, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "Responsable: $nombreResponsable",
                                    fontSize = 13.sp,
                                    color = colorResponsable,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}