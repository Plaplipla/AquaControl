package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medicioncloro.data.Desviacion
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.screen.components.EvidenciaFotoSection
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.EvidenciaFotoViewModel
import com.example.medicioncloro.ui.viewmodel.MedicionViewModel

@Composable
fun GestionDesviacionesScreen(viewModel: MedicionViewModel, supervisorNombre: String) {
    val desviaciones by viewModel.desviaciones.collectAsStateWithLifecycle()
    var seleccionada by remember { mutableStateOf<Desviacion?>(null) }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        CorporateHeader(
            title = "Desviaciones",
            subtitle = "Acciones correctivas requeridas",
            lineColor = RoleSupervisorOrange
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(desviaciones) { d ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    onClick = { seleccionada = d },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(d.puntoNombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            EstadoChip(d.estado)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = if (d.accionRegistrada.isNotBlank()) d.accionRegistrada else d.descripcion,
                            color = CorporateTextGray, fontSize = 14.sp
                        )
                        Text(d.fechaDeteccion, fontSize = 12.sp, color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }

    seleccionada?.let { d ->
        GestionDesviacionDialog(
            desviacion = d,
            supervisorNombre = supervisorNombre,
            onDismiss = { seleccionada = null },
            onGuardar = { estadoFinal, accion, cloro, ph, temp, turb, evidencia ->
                if (estadoFinal == "RESUELTA") {
                    viewModel.resolverDesviacion(
                        desviacion = d.copy(
                            accionRegistrada = accion,
                            evidenciaUri = evidencia,
                            supervisorAsignado = supervisorNombre
                        ),
                        cloro = cloro!!,
                        ph = ph!!,
                        temperatura = temp,
                        turbidez = turb,
                        supervisor = supervisorNombre
                    )
                } else {
                    viewModel.actualizarDesviacion(d.copy(
                        estado = estadoFinal,
                        accionRegistrada = accion,
                        supervisorAsignado = supervisorNombre,
                        evidenciaUri = evidencia
                    ))
                }
                seleccionada = null
            }
        )
    }
}

@Composable
fun EstadoChip(estado: String) {
    val (color, texto) = when (estado) {
        "PENDIENTE" -> CorporateRed to "Pendiente"
        "EN_PROCESO" -> StatusWarning to "En proceso"
        else -> StatusNormal to "Resuelta"
    }
    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
        Text(texto, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class) // IMPORTANTE PARA EL MENÚ DESPLEGABLE
@Composable
fun GestionDesviacionDialog(
    desviacion: Desviacion,
    supervisorNombre: String,
    onDismiss: () -> Unit,
    onGuardar: (estado: String, accion: String, cloro: Double?, ph: Double?, temp: Double, turb: Double, evidencia: String?) -> Unit
) {
    var accion by remember { mutableStateOf(desviacion.accionRegistrada) }
    var nuevoEstado by remember { mutableStateOf(desviacion.estado) }
    var nuevoCloro by remember { mutableStateOf("") }
    var nuevoPh by remember { mutableStateOf("") }
    var nuevaTemp by remember { mutableStateOf("") }
    var nuevaTurb by remember { mutableStateOf("") }
    var errorValidacion by remember { mutableStateOf<String?>(null) }

    // Variable para controlar si el menú está abierto o cerrado
    var estadoExpandido by remember { mutableStateOf(false) }

    val fotoVM: EvidenciaFotoViewModel = viewModel()
    val foto by fotoVM.estado.collectAsStateWithLifecycle()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Gestionar desviación", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = CorporateTextDark)
                Spacer(Modifier.height(8.dp))
                Text(desviacion.puntoNombre, fontWeight = FontWeight.Bold, color = CorporateDarkBlue)
                Text(desviacion.descripcion, fontSize = 14.sp, color = CorporateTextGray)

                Spacer(Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = estadoExpandido,
                    onExpandedChange = { estadoExpandido = it }
                ) {
                    OutlinedTextField(
                        value = nuevoEstado.replace("_", " "),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado", fontWeight = FontWeight.Bold) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estadoExpandido) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryWaterBlue,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = estadoExpandido,
                        onDismissRequest = { estadoExpandido = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        listOf("PENDIENTE", "EN_PROCESO", "RESUELTA").forEach { e ->
                            DropdownMenuItem(
                                text = { Text(e.replace("_", " ")) },
                                onClick = {
                                    nuevoEstado = e
                                    estadoExpandido = false
                                    errorValidacion = null
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = accion, onValueChange = { accion = it },
                    label = { Text("Acción correctiva") }, minLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (nuevoEstado == "RESUELTA") {
                    Spacer(Modifier.height(16.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = CorporateLightBlue.copy(alpha = 0.5f))) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Validación de resolución", fontWeight = FontWeight.Bold, color = CorporateDarkBlue)
                            Text("Cloro y pH son obligatorios. Temperatura y Turbidez son opcionales.", fontSize = 12.sp, color = CorporateTextGray)
                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nuevoCloro, onValueChange = { nuevoCloro = it; errorValidacion = null },
                                label = { Text("Nuevo Cloro (ppm) *") },
                                placeholder = { Text("Ej: 4.5") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                isError = errorValidacion != null && errorValidacion!!.contains("cloro", ignoreCase = true)
                            )
                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nuevoPh, onValueChange = { nuevoPh = it; errorValidacion = null },
                                label = { Text("Nuevo pH *") },
                                placeholder = { Text("Ej: 7.2") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                isError = errorValidacion != null && errorValidacion!!.contains("pH", ignoreCase = true)
                            )

                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nuevaTemp, onValueChange = { nuevaTemp = it },
                                label = { Text("Nueva Temperatura (°C) - Opcional") },
                                placeholder = { Text("Ej: 24.5") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nuevaTurb, onValueChange = { nuevaTurb = it },
                                label = { Text("Nueva Turbidez (NTU) - Opcional") },
                                placeholder = { Text("Ej: 2.5") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (errorValidacion != null) {
                                Spacer(Modifier.height(8.dp))
                                Text(errorValidacion!!, color = CorporateRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                EvidenciaFotoSection(fotoVM)

                Spacer(Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (nuevoEstado == "RESUELTA") {
                                val c = nuevoCloro.toDoubleOrNull()
                                val p = nuevoPh.toDoubleOrNull()
                                val t = nuevaTemp.toDoubleOrNull() ?: 0.0
                                val tb = nuevaTurb.toDoubleOrNull() ?: 0.0

                                if (c == null || p == null) {
                                    errorValidacion = "Debe ingresar ambos valores obligatorios (Cloro y pH)."
                                } else if (c < 3.00 || c > 5.00) {
                                    errorValidacion = "El nuevo nivel de cloro debe estar entre 3.00 y 5.00 ppm."
                                } else if (p < 6.5 || p > 8.0) {
                                    errorValidacion = "El nuevo nivel de pH debe estar entre 6.5 y 8.0."
                                } else {
                                    onGuardar(nuevoEstado, accion, c, p, t, tb, foto.uri?.toString())
                                }
                            } else {
                                onGuardar(nuevoEstado, accion, null, null, 0.0, 0.0, foto.uri?.toString())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryWaterBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Guardar") }
                }
            }
        }
    }
}