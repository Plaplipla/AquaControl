package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicioncloro.data.EstadoMedicion
import com.example.medicioncloro.model.Granja
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.MedicionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(viewModel: MedicionViewModel) {
    val mediciones by viewModel.mediciones.collectAsStateWithLifecycle()
    var granjaSeleccionada by remember { mutableStateOf<Granja?>(null) }
    var expandido by remember { mutableStateOf(false) }

    val medicionesFiltradas = if (granjaSeleccionada == null) {
        mediciones
    } else {
        val puntosIds = viewModel.puntos.filter { it.granjaId == granjaSeleccionada!!.id }.map { it.id }
        mediciones.filter { it.puntoId in puntosIds }
    }

    val reporte = viewModel.granjas.map { g ->
        val puntosIds = viewModel.puntos.filter { it.granjaId == g.id }.map { it.id }
        val meds = medicionesFiltradas.filter { it.puntoId in puntosIds }
        val normales = meds.count { it.estado == EstadoMedicion.NORMAL }
        val criticos = meds.count { it.estado == EstadoMedicion.CRITICO }
        val promCloro = if (meds.isNotEmpty()) meds.map { it.cloroLibre }.average() else 0.0
        Triple(g.nombre, Triple(meds.size, normales, criticos), promCloro)
    }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        CorporateHeader(
            title = "Reportes",
            subtitle = "Resumen y comparación por granja",
            lineColor = RoleJefaturaPurple
        )

        // Filtro por granja
        ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = it }) {
            OutlinedTextField(
                value = granjaSeleccionada?.nombre ?: "Todas las granjas",
                onValueChange = {},
                readOnly = true,
                label = { Text("Filtrar por Granja") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoleJefaturaPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }, modifier = Modifier.background(Color.White)) {
                DropdownMenuItem(
                    text = { Text("Todas las granjas") },
                    onClick = { granjaSeleccionada = null; expandido = false }
                )
                viewModel.granjas.forEach { g ->
                    DropdownMenuItem(
                        text = { Text(g.nombre) },
                        onClick = { granjaSeleccionada = g; expandido = false }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(reporte.filter { it.second.first > 0 }) { (nombre, conteos, promCloro) ->
                val (total, normales, criticos) = conteos
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CorporateTextDark)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total: $total", fontSize = 12.sp, color = CorporateTextGray)
                            Text("Normales: $normales", fontSize = 12.sp, color = StatusNormal, fontWeight = FontWeight.Bold)
                            Text("Críticos: $criticos", fontSize = 12.sp, color = StatusCritical, fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.height(12.dp))

                        Text("Promedio Cloro: ${"%.2f".format(promCloro)} ppm", fontSize = 14.sp, color = CorporateTextDark)
                        Spacer(Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { (promCloro / 10.0).toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth().height(8.dp),
                            color = if (promCloro in 3.0..5.0) StatusNormal else StatusCritical,
                            trackColor = Color.LightGray.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}