package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medicioncloro.data.GranjaRepository
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.screen.components.EvidenciaFotoSection
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.EvidenciaFotoViewModel
import com.example.medicioncloro.ui.viewmodel.RegistroMedicionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroMedicionScreen(
    viewModel: RegistroMedicionViewModel,
    nombreOperario: String,
    onGuardado: () -> Unit,
    onCancelar: () -> Unit,
    fotoVM: EvidenciaFotoViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val foto by fotoVM.estado.collectAsStateWithLifecycle()
    var granjaExp by remember { mutableStateOf(false) }
    var puntoExp by remember { mutableStateOf(false) }
    val granjas = GranjaRepository.granjas
    val puntos = GranjaRepository.puntos.filter { it.granjaId == estado.granjaId }

    // Colores neutros para los campos de texto
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PrimaryWaterBlue,
        unfocusedBorderColor = Color.LightGray,
        focusedLabelColor = CorporateTextDark,
        unfocusedLabelColor = CorporateTextGray,
        focusedTextColor = CorporateTextDark,
        unfocusedTextColor = CorporateTextDark,
        cursorColor = PrimaryWaterBlue
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        CorporateHeader(
            title = "Registrar Medición",
            subtitle = "Selecciona la ubicación",
            lineColor = RoleOperarioTeal
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Granja Dropdown
                ExposedDropdownMenuBox(granjaExp, { granjaExp = it }) {
                    OutlinedTextField(
                        value = granjas.find { it.id == estado.granjaId }?.nombre ?: "Seleccionar",
                        onValueChange = {}, readOnly = true,
                        label = { Text("Granja", fontWeight = FontWeight.Bold) },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CorporateDarkBlue) },
                        leadingIcon = { Icon(Icons.Default.Agriculture, contentDescription = null, tint = CorporateDarkBlue) },
                        isError = estado.errores.granja != null,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors
                    )
                    ExposedDropdownMenu(
                        expanded = granjaExp,
                        onDismissRequest = { granjaExp = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        granjas.forEach { g ->
                            DropdownMenuItem(
                                text = { Text(g.nombre) },
                                onClick = { viewModel.onGranjaChange(g.id); granjaExp = false }
                            )
                        }
                    }
                }

                // Punto Dropdown
                ExposedDropdownMenuBox(puntoExp, { if (estado.granjaId != 0) puntoExp = it }) {
                    OutlinedTextField(
                        value = puntos.find { it.id == estado.puntoId }?.nombre ?: "Seleccionar",
                        onValueChange = {}, readOnly = true,
                        label = { Text("Línea / Punto", fontWeight = FontWeight.Bold) },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CorporateDarkBlue) },
                        leadingIcon = { Icon(Icons.Default.WaterDrop, contentDescription = null, tint = CorporateDarkBlue) },
                        isError = estado.errores.punto != null,
                        enabled = estado.granjaId != 0,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors
                    )
                    ExposedDropdownMenu(
                        expanded = puntoExp,
                        onDismissRequest = { puntoExp = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        puntos.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.nombre) },
                                onClick = { viewModel.onPuntoChange(p.id); puntoExp = false }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Parámetros del agua", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CorporateDarkBlue)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.cloroLibre, onValueChange = viewModel::onCloroChange,
            label = { Text("Cloro libre (ppm)", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.WaterDrop, contentDescription = null, tint = CorporateDarkBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = estado.errores.cloro != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.ph, onValueChange = viewModel::onPhChange,
            label = { Text("pH", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.Science, contentDescription = null, tint = CorporateDarkBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = estado.errores.ph != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.temperatura, onValueChange = viewModel::onTemperaturaChange,
            label = { Text("Temperatura (°C) - Opcional", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.Thermostat, contentDescription = null, tint = CorporateDarkBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = estado.errores.temperatura != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.turbidez, onValueChange = viewModel::onTurbidezChange,
            label = { Text("Turbidez (NTU) - Opcional", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.BlurOn, contentDescription = null, tint = CorporateDarkBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = estado.errores.turbidez != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = estado.observaciones,
            onValueChange = viewModel::onObservacionesChange,
            label = { Text("Acción / Control realizado", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.Build, contentDescription = null, tint = CorporateDarkBlue) },
            placeholder = { Text("Ej: Se limpió el dosificador", color = Color.LightGray) },
            minLines = 2,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors
        )

        Spacer(Modifier.height(16.dp))

        EvidenciaFotoSection(fotoVM)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { viewModel.guardarMedicion(nombreOperario, foto.uri?.toString(), onSuccess = onGuardado) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryWaterBlue)
        ) {
            Text("Continuar al registro", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        TextButton(
            onClick = onCancelar,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            // AQUÍ ESTÁ EL CAMBIO: Color rojo para el texto y el efecto al presionar
            colors = ButtonDefaults.textButtonColors(contentColor = CorporateRed)
        ) {
            Text("Cancelar", fontWeight = FontWeight.Bold)
        }
    }
}