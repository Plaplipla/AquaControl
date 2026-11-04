package com.example.medicioncloro.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicioncloro.model.Rol
import com.example.medicioncloro.ui.theme.*

@Composable
fun CorporateHeader(title: String, subtitle: String? = null, lineColor: Color = CorporateDarkBlue) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CorporateTextDark
        )
        Box(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 8.dp)
                .width(40.dp)
                .height(4.dp)
                .background(lineColor, RoundedCornerShape(2.dp))
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                fontSize = 16.sp,
                color = CorporateTextGray
            )
        }
    }
}

@Composable
fun CorporateActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    containerColor: Color = Color.White,
    contentColor: Color = CorporateTextDark,
    iconColor: Color = CorporateDarkBlue
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = contentColor
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = contentColor.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Ir",
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun getRoleColor(rol: Rol): Color = when (rol) {
    Rol.OPERARIO -> RoleOperarioTeal
    Rol.SUPERVISOR -> RoleSupervisorOrange
    Rol.JEFATURA -> RoleJefaturaPurple
}