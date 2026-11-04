package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicioncloro.data.EstadoMedicion
import com.example.medicioncloro.model.Usuario
import com.example.medicioncloro.ui.screen.components.CorporateActionCard
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.MedicionViewModel

@Composable
fun HomeSupervisorScreen(
    viewModel: MedicionViewModel,
    usuario: Usuario,
    onIrAMediciones: () -> Unit,
    onIrAAlertas: () -> Unit,
    onIrADesviaciones: () -> Unit,
    onIrAHistorico: () -> Unit
) {
    val todasLasMediciones by viewModel.mediciones.collectAsStateWithLifecycle()
    val alertas by viewModel.alertas.collectAsStateWithLifecycle()
    val desviaciones by viewModel.desviaciones.collectAsStateWithLifecycle()

    val medicionesActuales = remember(todasLasMediciones) { viewModel.obtenerMedicionesActuales() }

    val normales = medicionesActuales.count { it.estado == EstadoMedicion.NORMAL }
    val criticos = medicionesActuales.count { it.estado == EstadoMedicion.CRITICO }
    val advertencias = medicionesActuales.count { it.estado == EstadoMedicion.ADVERTENCIA }

    val pendientes = desviaciones.count { it.estado != "RESUELTA" }
    val alertasActivas = alertas.count { !it.atendida }

    // Definición de colores para las tarjetas
    val colorMedicionesBg = Color(0xFFE3F2FD) // Azul claro
    val colorMedicionesText = Color(0xFF1565C0) // Azul oscuro

    val colorAlertasBg = CorporateRed.copy(alpha = 0.1f) // Rojo claro
    val colorAlertasText = CorporateRed // Rojo

    val colorDesviacionesBg = RoleOperarioTeal.copy(alpha = 0.1f) // Verde Agua claro
    val colorDesviacionesText = RoleOperarioTeal // Verde Agua

    val colorHistoricoBg = Color(0xFFFDF4E3) // Un tono crema/arena muy suave
    val colorHistoricoText = Color(0xFFB07D3E) // Un tono mostaza/dorado opaco
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "PANEL SUPERVISOR",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CorporateDarkBlue,
            letterSpacing = 1.sp
        )
        CorporateHeader(
            title = "Visión de operación",
            subtitle = "Estado de las líneas bajo tu supervisión.",
            lineColor = RoleSupervisorOrange
        )

        // Tarjeta oscura con resumen
        Card(
            modifier = Modifier.fillMaxWidth().height(170.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CorporateDarkBlue)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 60.dp, y = 60.dp)
                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(100.dp))
                )
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp)
                ) {
                    Text("ESTADO ACTUAL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))
                    Spacer(Modifier.height(8.dp))
                    Text("$normales de ${medicionesActuales.size} líneas en rango normal", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White, lineHeight = 28.sp)
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Advertencia: $advertencias", fontSize = 14.sp, color = Color.White)
                        Text("Crítico: $criticos", fontSize = 14.sp, color = Color.White)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Lista de tarjetas de acción
        CorporateActionCard(
            icon = Icons.Default.Search,
            title = "Consultar Mediciones",
            subtitle = "Revisa las mediciones registradas",
            onClick = onIrAMediciones,
            containerColor = colorMedicionesBg,
            contentColor = colorMedicionesText,
            iconColor = colorMedicionesText
        )
        Spacer(Modifier.height(12.dp))

        CorporateActionCard(
            icon = Icons.Default.Notifications,
            title = "Revisar Alertas",
            subtitle = "Consulta y gestiona alertas del sistema",
            onClick = onIrAAlertas,
            containerColor = colorAlertasBg,
            contentColor = colorAlertasText,
            iconColor = colorAlertasText
        )
        Spacer(Modifier.height(12.dp))

        CorporateActionCard(
            icon = Icons.Default.Assignment,
            title = "Gestionar Desviaciones",
            subtitle = "Valida y cierra desviaciones registradas",
            onClick = onIrADesviaciones,
            containerColor = colorDesviacionesBg,
            contentColor = colorDesviacionesText,
            iconColor = colorDesviacionesText
        )
        Spacer(Modifier.height(12.dp))

        CorporateActionCard(
            icon = Icons.Default.History,
            title = "Histórico de Mediciones",
            subtitle = "Consulta el historial de mediciones y eventos",
            onClick = onIrAHistorico,
            containerColor = colorHistoricoBg,
            contentColor = colorHistoricoText,
            iconColor = colorHistoricoText
        )
    }
}