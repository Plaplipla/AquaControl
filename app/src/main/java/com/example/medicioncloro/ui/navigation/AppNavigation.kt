package com.example.medicioncloro.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.medicioncloro.model.Rol
import com.example.medicioncloro.ui.screen.*
import com.example.medicioncloro.ui.theme.CorporateBackground
import com.example.medicioncloro.ui.theme.CorporateDarkBlue
import com.example.medicioncloro.ui.theme.PrimaryWaterBlue
import com.example.medicioncloro.ui.theme.StatusWarning
import com.example.medicioncloro.ui.viewmodel.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    sessionVM: SessionViewModel,
    medicionVM: MedicionViewModel,
    registroVM: RegistroMedicionViewModel
) {
    val navController = rememberNavController()
    val usuario by sessionVM.usuario.collectAsStateWithLifecycle()
    if (usuario == null) return

    val rol = usuario!!.rol
    val menuItems = menuPorRol(rol)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val rolColor = when (rol) {
        Rol.OPERARIO -> Color(0xFF00897B)
        Rol.SUPERVISOR -> StatusWarning
        Rol.JEFATURA -> Color(0xFF7B1FA2)
    }

    val titulo = when (currentRoute) {
        AppRoutes.HOME -> "Inicio"
        AppRoutes.MEDICIONES -> "Mediciones"
        AppRoutes.REGISTRO -> "Registrar Medición"
        AppRoutes.ALERTAS -> "Alertas"
        AppRoutes.DESVIACIONES -> "Desviaciones"
        AppRoutes.HISTORICO -> "Histórico"
        AppRoutes.REPORTES -> "Reportes"
        AppRoutes.AUDITORIA -> "Auditoría"
        AppRoutes.PERFIL -> "Perfil"
        else -> "AquaControl"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CorporateBackground
            ) {
                Text("AquaControl", style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = CorporateDarkBlue,
                    modifier = Modifier.padding(24.dp))
                Text("${usuario!!.nombre} · ${rol.etiqueta}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp))
                HorizontalDivider(Modifier.padding(vertical = 8.dp))

                menuItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label, fontWeight = FontWeight.Bold) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(AppRoutes.HOME) { saveState = true }
                                launchSingleTop = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = PrimaryWaterBlue.copy(alpha = 0.1f),
                            selectedTextColor = PrimaryWaterBlue,
                            selectedIconColor = PrimaryWaterBlue
                        )
                    )
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión", fontWeight = FontWeight.Bold) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        sessionVM.logout()
                    },
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        Scaffold(
            containerColor = CorporateBackground,
            topBar = {
                TopAppBar(
                    title = { Text(titulo, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CorporateBackground,
                        titleContentColor = CorporateDarkBlue
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú", tint = CorporateDarkBlue)
                        }
                    },
                    actions = {
                        AssistChip(
                            onClick = { },
                            label = { Text(rol.etiqueta, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.padding(end = 12.dp),
                            border = null,
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = rolColor,
                                containerColor = rolColor.copy(alpha = 0.1f)
                            )
                        )
                    }
                )
            },
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = AppRoutes.HOME,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(AppRoutes.HOME) {
                    when (rol) {
                        Rol.OPERARIO -> HomeOperarioScreen(
                            viewModel = medicionVM,
                            usuario = usuario!!,
                            onIrAMediciones = { navController.navigate(AppRoutes.MEDICIONES) },
                            onIrARegistro = { navController.navigate(AppRoutes.REGISTRO) },
                            onIrAAlertas = { navController.navigate(AppRoutes.ALERTAS) }
                        )
                        Rol.SUPERVISOR -> HomeSupervisorScreen(
                            viewModel = medicionVM,
                            usuario = usuario!!,
                            onIrAMediciones = { navController.navigate(AppRoutes.MEDICIONES) },
                            onIrAAlertas = { navController.navigate(AppRoutes.ALERTAS) },
                            onIrADesviaciones = { navController.navigate(AppRoutes.DESVIACIONES) },
                            onIrAHistorico = { navController.navigate(AppRoutes.HISTORICO) }
                        )
                        Rol.JEFATURA -> HomeJefaturaScreen(
                            viewModel = medicionVM,
                            usuario = usuario!!,
                            onIrAReportes = { navController.navigate(AppRoutes.REPORTES) },
                            onIrAHistorico = { navController.navigate(AppRoutes.HISTORICO) },
                            onIrAAuditoria = { navController.navigate(AppRoutes.AUDITORIA) }
                        )
                    }
                }
                composable(AppRoutes.MEDICIONES) {
                    MedicionesScreen(medicionVM, usuario!!)
                }
                composable(AppRoutes.REGISTRO) {
                    if (rol == Rol.OPERARIO) {
                        RegistroMedicionScreen(
                            viewModel = registroVM,
                            nombreOperario = usuario!!.nombre,
                            onGuardado = {
                                navController.navigate(AppRoutes.MEDICIONES) {
                                    popUpTo(AppRoutes.HOME)
                                }
                            },
                            onCancelar = { navController.popBackStack() }
                        )
                    } else {
                        AccesoDenegadoScreen(rol) { navController.popBackStack() }
                    }
                }
                composable(AppRoutes.ALERTAS) {
                    AlertasScreen(medicionVM, rol)
                }
                composable(AppRoutes.DESVIACIONES) {
                    if (rol == Rol.SUPERVISOR) {
                        GestionDesviacionesScreen(medicionVM, usuario!!.nombre)
                    } else {
                        AccesoDenegadoScreen(rol) { navController.popBackStack() }
                    }
                }
                composable(AppRoutes.HISTORICO) {
                    HistoricoScreen(medicionVM, usuario!!.rol)
                }
                composable(AppRoutes.REPORTES) {
                    if (rol == Rol.JEFATURA) {
                        ReportesScreen(medicionVM)
                    } else {
                        AccesoDenegadoScreen(rol) { navController.popBackStack() }
                    }
                }
                composable(AppRoutes.AUDITORIA) {
                    if (rol == Rol.JEFATURA) {
                        AuditoriaScreen(medicionVM)
                    } else {
                        AccesoDenegadoScreen(rol) { navController.popBackStack() }
                    }
                }
                composable(AppRoutes.PERFIL) {
                    val notif by sessionVM.notifActivas.collectAsStateWithLifecycle()
                    PerfilScreen(usuario!!, notif,
                        onNotifChange = { sessionVM.setNotifActivas(it) },
                        onLogout = { sessionVM.logout() })
                }
            }
        }
    }
}