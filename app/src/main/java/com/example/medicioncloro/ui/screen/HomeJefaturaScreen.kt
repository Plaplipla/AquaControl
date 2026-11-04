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
fun HomeJefaturaScreen(
    viewModel: MedicionViewModel,
    usuario: Usuario,
    onIrAReportes: () -> Unit,
    onIrAHistorico: () -> Unit,
    onIrAAuditoria: () -> Unit
) {
    val mediciones by viewModel.mediciones.collectAsStateWithLifecycle()
    val alertas by viewModel.alertas.collectAsStateWithLifecycle()

    // Calculamos datos para la tarjeta superior
    val normales = mediciones.count { it.estado == EstadoMedicion.NORMAL }
    val porcentaje = if (mediciones.isNotEmpty()) (normales * 100) / mediciones.size else 0
    val alertasActivas = alertas.count { !it.atendida }

    // Colores Pastel para las tarjetas
    val colorReportesBg = Color(0xFFE3F2FD) // Azul claro
    val colorReportesText = Color(0xFF1565C0)

    val colorHistoricoBg = Color(0xFFFDF4E3) // Crema/Arena
    val colorHistoricoText = Color(0xFFB07D3E)

    val colorAuditoriaBg = RoleJefaturaPurple.copy(alpha = 0.1f) // Púrpura claro
    val colorAuditoriaText = RoleJefaturaPurple

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "PANEL JEFATURA",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CorporateDarkBlue,
            letterSpacing = 1.sp
        )
        CorporateHeader(
            title = "Visión Global",
            subtitle = "Supervisión y análisis de todas las granjas.",
            lineColor = RoleJefaturaPurple
        )

        // Tarjeta oscura con resumen ejecutivo
        Card(
            modifier = Modifier.fillMaxWidth().height(150.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CorporateDarkBlue)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 40.dp, y = 40.dp)
                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(100.dp))
                )
                Row(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("$porcentaje%", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Cumplimiento global", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Alertas activas", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                        Text("$alertasActivas", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Tarjetas de acción
        CorporateActionCard(
            icon = Icons.Default.Assessment,
            title = "Reportes",
            subtitle = "Analiza promedios y compara granjas",
            onClick = onIrAReportes,
            containerColor = colorReportesBg,
            contentColor = colorReportesText,
            iconColor = colorReportesText
        )
        Spacer(Modifier.height(12.dp))

        CorporateActionCard(
            icon = Icons.Default.History,
            title = "Histórico Global",
            subtitle = "Filtra registros por 24h, 7d o 30d",
            onClick = onIrAHistorico,
            containerColor = colorHistoricoBg,
            contentColor = colorHistoricoText,
            iconColor = colorHistoricoText
        )
        Spacer(Modifier.height(12.dp))

        CorporateActionCard(
            icon = Icons.Default.FactCheck,
            title = "Auditoría de Desviaciones",
            subtitle = "Revisa acciones, responsables y fechas",
            onClick = onIrAAuditoria,
            containerColor = colorAuditoriaBg,
            contentColor = colorAuditoriaText,
            iconColor = colorAuditoriaText
        )
    }
}