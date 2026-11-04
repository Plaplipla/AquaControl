package com.example.medicioncloro.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicioncloro.data.EstadoMedicion
import com.example.medicioncloro.data.Medicion
import com.example.medicioncloro.model.*
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.screen.components.getRoleColor
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.MedicionViewModel

@Composable
fun MedicionesScreen(viewModel: MedicionViewModel, usuario: Usuario) {
    val todasLasMediciones by viewModel.mediciones.collectAsStateWithLifecycle()
    val medicionesActuales = remember(todasLasMediciones) { viewModel.obtenerMedicionesActuales() }

    // Filtro P14
    val granjasVisibles = if (usuario.rol == Rol.OPERARIO || usuario.rol == Rol.SUPERVISOR) {
        viewModel.granjas.filter { it.id in usuario.granjasAsignadas }
    } else {
        viewModel.granjas
    }

    var granjaSeleccionadaId by remember { mutableStateOf<Int?>(null) }

    if (granjaSeleccionadaId == null) {
        ListaGranjas(
            granjas = granjasVisibles,
            rol = usuario.rol, // <-- PASAMOS EL ROL
            onGranjaClick = { granjaSeleccionadaId = it.id }
        )
    } else {
        val granja = granjasVisibles.find { it.id == granjaSeleccionadaId }
        val puntosDeGranja = viewModel.puntos.filter { it.granjaId == granjaSeleccionadaId }.map { it.id }
        val medicionesDeGranja = medicionesActuales.filter { it.puntoId in puntosDeGranja }

        DetalleMedicionesGranja(
            granja = granja!!,
            mediciones = medicionesDeGranja,
            viewModel = viewModel,
            rol = usuario.rol, // <-- PASAMOS EL ROL
            onVolver = { granjaSeleccionadaId = null }
        )
    }
}

@Composable
fun ListaGranjas(granjas: List<Granja>, rol: Rol, onGranjaClick: (Granja) -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        CorporateHeader(
            title = "Granjas",
            subtitle = "Selecciona una granja para ver sus mediciones",
            lineColor = getRoleColor(rol) // <-- AQUÍ YA FUNCIONA
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(granjas) { granja ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onGranjaClick(granja) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(CorporateLightBlue, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Agriculture, contentDescription = null, tint = CorporateDarkBlue, modifier = Modifier.size(32.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(granja.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CorporateTextDark)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = CorporateTextGray, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(granja.ubicacion, fontSize = 14.sp, color = CorporateTextGray)
                            }
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = CorporateTextGray)
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleMedicionesGranja(
    granja: Granja,
    mediciones: List<Medicion>,
    viewModel: MedicionViewModel,
    rol: Rol, // <-- RECIBE EL ROL
    onVolver: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        TextButton(
            onClick = onVolver,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = CorporateDarkBlue)
            Spacer(Modifier.width(4.dp))
            Text("Volver a granjas", color = CorporateDarkBlue, fontWeight = FontWeight.Bold)
        }

        CorporateHeader(
            title = granja.nombre,
            subtitle = "Estado actual de las líneas de agua",
            lineColor = getRoleColor(rol) // <-- AQUÍ YA FUNCIONA
        )

        if (mediciones.isEmpty()) {
            Box(Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                Text("No hay mediciones registradas en esta granja.", color = CorporateTextGray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(mediciones) { m ->
                    MedicionCard(m, viewModel.obtenerNombrePunto(m.puntoId))
                }
            }
        }
    }
}

@Composable
fun MedicionCard(medicion: Medicion, nombrePunto: String) {
    val escala by animateFloatAsState(
        targetValue = if (medicion.estado == EstadoMedicion.CRITICO) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.4f), label = "escala"
    )

    val borderColor = when(medicion.estado) {
        EstadoMedicion.NORMAL -> StatusNormal
        EstadoMedicion.ADVERTENCIA -> StatusWarning
        EstadoMedicion.CRITICO -> StatusCritical
    }

    Card(
        modifier = Modifier.fillMaxWidth().graphicsLayer { scaleX = escala; scaleY = escala },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

            Box(modifier = Modifier.width(8.dp).fillMaxHeight().background(borderColor))

            Column(Modifier.padding(16.dp).weight(1f)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(nombrePunto, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CorporateTextDark)
                    Surface(color = borderColor.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = when (medicion.estado) {
                                EstadoMedicion.NORMAL -> "Normal"
                                EstadoMedicion.ADVERTENCIA -> "Advertencia"
                                EstadoMedicion.CRITICO -> "Crítico"
                            },
                            color = borderColor, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))

                // Fila con los 4 parámetros
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Cloro", fontSize = 11.sp, color = CorporateTextGray)
                        Text(formatearCloro(medicion.cloroLibre), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Column {
                        Text("pH", fontSize = 11.sp, color = CorporateTextGray)
                        Text(formatearPh(medicion.ph), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Column {
                        Text("Temp.", fontSize = 11.sp, color = CorporateTextGray)
                        Text(
                            text = if (medicion.temperatura > 0.0) formatearTemp(medicion.temperatura) else "-",
                            fontWeight = FontWeight.Bold, fontSize = 14.sp
                        )
                    }
                    Column {
                        Text("Turbidez", fontSize = 11.sp, color = CorporateTextGray)
                        Text(
                            text = if (medicion.turbidez > 0.0) formatearTurbidez(medicion.turbidez) else "-",
                            fontWeight = FontWeight.Bold, fontSize = 14.sp
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Última act: ${medicion.fechaHora}", fontSize = 12.sp, color = CorporateTextGray)

                    if (!medicion.sincronizado) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CloudOff, contentDescription = null, tint = CorporateRed, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Pendiente sync", color = CorporateRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}