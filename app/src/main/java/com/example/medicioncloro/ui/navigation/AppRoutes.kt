package com.example.medicioncloro.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.medicioncloro.model.Rol

object AppRoutes {
    const val HOME = "home"
    const val MEDICIONES = "mediciones"
    const val REGISTRO = "registro"
    const val ALERTAS = "alertas"
    const val DESVIACIONES = "desviaciones"
    const val HISTORICO = "historico"
    const val REPORTES = "reportes"
    const val AUDITORIA = "auditoria" // NUEVA RUTA
    const val PERFIL = "perfil"
}

data class NavItem(val route: String, val label: String, val icon: ImageVector)

fun menuPorRol(rol: Rol): List<NavItem> = when (rol) {
    Rol.OPERARIO -> listOf(
        NavItem(AppRoutes.HOME, "Inicio", Icons.Default.Home),
        NavItem(AppRoutes.MEDICIONES, "Mediciones", Icons.Default.WaterDrop),
        NavItem(AppRoutes.REGISTRO, "Registrar", Icons.Default.Add),
        NavItem(AppRoutes.ALERTAS, "Alertas", Icons.Default.Warning),
        NavItem(AppRoutes.PERFIL, "Perfil", Icons.Default.Person)
    )
    Rol.SUPERVISOR -> listOf(
        NavItem(AppRoutes.HOME, "Inicio", Icons.Default.Home),
        NavItem(AppRoutes.MEDICIONES, "Mediciones", Icons.Default.WaterDrop),
        NavItem(AppRoutes.ALERTAS, "Alertas", Icons.Default.Warning),
        NavItem(AppRoutes.DESVIACIONES, "Desviaciones", Icons.Default.Build),
        NavItem(AppRoutes.HISTORICO, "Histórico", Icons.Default.History),
        NavItem(AppRoutes.PERFIL, "Perfil", Icons.Default.Person)
    )
    Rol.JEFATURA -> listOf(
        NavItem(AppRoutes.HOME, "Inicio", Icons.Default.Home),
        NavItem(AppRoutes.REPORTES, "Reportes", Icons.Default.Assessment),
        NavItem(AppRoutes.HISTORICO, "Histórico", Icons.Default.History),
        NavItem(AppRoutes.AUDITORIA, "Auditoría", Icons.Default.FactCheck), // NUEVO MENÚ
        NavItem(AppRoutes.PERFIL, "Perfil", Icons.Default.Person)
    )
}