package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.medicioncloro.model.Usuario
import com.example.medicioncloro.ui.screen.components.CorporateActionCard
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.MedicionViewModel

@Composable
fun HomeOperarioScreen(
    viewModel: MedicionViewModel,
    usuario: Usuario,
    onIrAMediciones: () -> Unit,
    onIrARegistro: () -> Unit,
    onIrAAlertas: () -> Unit
) {
    val mediciones by viewModel.mediciones.collectAsStateWithLifecycle()
    val alertas by viewModel.alertas.collectAsStateWithLifecycle()

    val misMediciones = mediciones.filter { it.registradoPor == usuario.nombre }
    val pendientesDeSync = misMediciones.count { !it.sincronizado }
    val alertasActivas = alertas.count { !it.atendida }

    // Colores pastel para las tarjetas
    val colorMedicionesBg = Color(0xFFE3F2FD) // Azul muy claro
    val colorMedicionesText = Color(0xFF1565C0) // Azul vivo

    val colorRegistroBg = RoleOperarioTeal.copy(alpha = 0.1f) // Verde aguado muy claro
    val colorRegistroText = RoleOperarioTeal // Verde aguado

    val colorAlertasBg = CorporateRed.copy(alpha = 0.1f) // Rojo muy claro
    val colorAlertasText = CorporateRed // Rojo

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "PANEL OPERARIO",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CorporateDarkBlue,
            letterSpacing = 1.sp
        )
        CorporateHeader(
            title = "Control en terreno",
            subtitle = "Medición de cloro y pH en las líneas de agua.",
            lineColor = RoleOperarioTeal
        )

        // Tarjeta azul oscura con información
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CorporateDarkBlue)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 50.dp, y = 50.dp)
                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(100.dp))
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("CONTROL DE LA OPERACIÓN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))
                    Spacer(Modifier.height(8.dp))
                    Text("Revisa, registra y da seguimiento", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White, lineHeight = 28.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Consulta las alertas para identificar las líneas que requieren atención.", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f), lineHeight = 18.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // TARJETA DE SINCRONIZACIÓN
        if (pendientesDeSync > 0) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StatusWarning.copy(alpha = 0.1f))
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudOff, contentDescription = null, tint = StatusWarning)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Trabajando sin conexión", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StatusWarning)
                        Text("Tienes $pendientesDeSync registros pendientes.", fontSize = 12.sp, color = CorporateTextDark)
                    }
                    Button(
                        onClick = { viewModel.sincronizarMediciones() },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusWarning),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Sincronizar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Tarjetas de acción con los NUEVOS COLORES
        CorporateActionCard(
            icon = Icons.Default.WaterDrop,
            title = "Mediciones",
            subtitle = "Consulta el estado actual de las líneas",
            onClick = onIrAMediciones,
            containerColor = colorMedicionesBg,
            contentColor = colorMedicionesText,
            iconColor = colorMedicionesText
        )
        Spacer(Modifier.height(16.dp))
        CorporateActionCard(
            icon = Icons.Default.Add,
            title = "Registrar medición de cloro",
            subtitle = "Registro y evidencia fotográfica",
            onClick = onIrARegistro,
            containerColor = colorRegistroBg,
            contentColor = colorRegistroText,
            iconColor = colorRegistroText
        )
        Spacer(Modifier.height(16.dp))
        CorporateActionCard(
            icon = Icons.Default.Notifications,
            title = "Ver alertas",
            subtitle = "$alertasActivas líneas que requieren atención",
            onClick = onIrAAlertas,
            containerColor = colorAlertasBg,
            contentColor = colorAlertasText,
            iconColor = colorAlertasText
        )
    }
}