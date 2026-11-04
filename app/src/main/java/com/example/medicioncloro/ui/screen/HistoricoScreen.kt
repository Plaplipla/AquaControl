package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicioncloro.data.EstadoMedicion
import com.example.medicioncloro.model.*
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.screen.components.getRoleColor
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.MedicionViewModel

@Composable
fun HistoricoScreen(viewModel: MedicionViewModel, rol: Rol) {
    val mediciones by viewModel.mediciones.collectAsStateWithLifecycle()
    var filtroTiempo by remember { mutableStateOf("Todos") }

    val medicionesFiltradas = when (filtroTiempo) {
        "24h" -> mediciones.take(3)
        "7d" -> mediciones.take(8)
        else -> mediciones
    }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        CorporateHeader(
            title = "Historial",
            subtitle = "Registro cronológico de mediciones",
            lineColor = getRoleColor(rol)
        )

        // Chips de filtro
        Row(Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("24h", "7d", "30d", "Todos").forEach { opcion ->
                FilterChip(
                    selected = filtroTiempo == opcion,
                    onClick = { filtroTiempo = opcion },
                    label = { Text(opcion) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getRoleColor(rol).copy(alpha = 0.1f),
                        selectedLabelColor = getRoleColor(rol)
                    )
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(medicionesFiltradas.sortedByDescending { it.fechaHora }) { m ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = when (m.estado) {
                                    EstadoMedicion.NORMAL -> StatusNormal
                                    EstadoMedicion.ADVERTENCIA -> StatusWarning
                                    EstadoMedicion.CRITICO -> StatusCritical
                                },
                                shape = CircleShape
                            )
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(viewModel.obtenerNombrePunto(m.puntoId), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Cloro: ${formatearCloro(m.cloroLibre)} | pH: ${formatearPh(m.ph)}", fontSize = 12.sp, color = CorporateTextGray)
                        }
                        Text(m.fechaHora.substringBefore(" "), fontSize = 12.sp, color = CorporateTextGray)
                    }
                }
            }
        }
    }
}