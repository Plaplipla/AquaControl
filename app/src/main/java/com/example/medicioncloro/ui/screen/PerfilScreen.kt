package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicioncloro.model.Rol
import com.example.medicioncloro.model.Usuario
import com.example.medicioncloro.ui.screen.components.CorporateHeader
import com.example.medicioncloro.ui.screen.components.getRoleColor
import com.example.medicioncloro.ui.theme.*

@Composable
fun PerfilScreen(
    usuario: Usuario,
    notifActivas: Boolean,
    onNotifChange: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CorporateHeader(
            "Mi Perfil",
            "Configuración de la cuenta",
            lineColor = getRoleColor(usuario.rol)
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(CorporateLightBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (usuario.rol) {
                    Rol.OPERARIO -> Icons.Default.Engineering
                    Rol.SUPERVISOR -> Icons.Default.SupervisorAccount
                    Rol.JEFATURA -> Icons.Default.BusinessCenter
                },
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = CorporateDarkBlue
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(usuario.nombre, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = CorporateTextDark)
        Text(usuario.correo, fontSize = 14.sp, color = CorporateTextGray)

        Spacer(Modifier.height(8.dp))

        Surface(color = CorporateRed.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
            Text("Rol: ${usuario.rol.etiqueta}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                fontWeight = FontWeight.Bold, color = CorporateRed, fontSize = 12.sp)
        }

        Spacer(Modifier.height(32.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = CorporateDarkBlue)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text("Notificaciones", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Avisos de valores críticos", fontSize = 12.sp, color = CorporateTextGray)
                }
                Switch(
                    checked = notifActivas,
                    onCheckedChange = onNotifChange,
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = CorporateRed)
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CorporateRed)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Cerrar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}