package cl.corpaliv.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

internal val Navy = Color(0xFF173C64)
internal val Teal = Color(0xFF168B9B)
internal val Green = Color(0xFF286650)
internal val Cream = Color(0xFFF8F3E9)
internal val CardShape = RoundedCornerShape(16.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CorpalivApp(vm: AppViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    LaunchedEffect(vm) { vm.cargarProgramas(); vm.cargarFeriado() }
    val public = state.pantalla !in setOf(Pantalla.LOGIN, Pantalla.GESTION, Pantalla.GESTION_DETALLE)
    var menuOpen by remember { mutableStateOf(false) }
    val scheme = lightColorScheme(primary = Teal, secondary = Green, background = Cream, surface = Color.White, onSurface = Navy, onSurfaceVariant = Navy, secondaryContainer = Teal.copy(alpha = .18f), onSecondaryContainer = Navy, surfaceContainer = Color.White, surfaceContainerHigh = Color.White)
    MaterialTheme(colorScheme = scheme) {
        Scaffold(
            containerColor = Cream,
            topBar = {
                TopAppBar(
                    title = { Text("CORPALIV", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        if (state.pantalla !in setOf(Pantalla.INICIO, Pantalla.PROGRAMAS, Pantalla.COLABORAR))
                            IconButton(onClick = { vm.ir(when (state.pantalla) { Pantalla.DETALLE -> Pantalla.PROGRAMAS; Pantalla.GESTION_DETALLE -> Pantalla.GESTION; else -> Pantalla.INICIO }) }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                            }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { menuOpen = true }) { Icon(Icons.Default.Menu, "Abrir menú") }
                            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                                listOf("Seguimiento" to Pantalla.SEGUIMIENTO, "Sobre CORPALIV" to Pantalla.SOBRE, "Acceso gestor" to Pantalla.LOGIN).forEach { (label, dest) ->
                                    DropdownMenuItem(text = { Text(label) }, onClick = { menuOpen = false; vm.ir(dest) })
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy, titleContentColor = Color.White, navigationIconContentColor = Color.White, actionIconContentColor = Color.White)
                )
            },
            bottomBar = {
                if (public) NavigationBar(containerColor = Color.White) {
                    listOf(Triple("Inicio", Icons.Default.Home, Pantalla.INICIO), Triple("Programas", Icons.Default.School, Pantalla.PROGRAMAS), Triple("Colaborar", Icons.Default.FavoriteBorder, Pantalla.COLABORAR)).forEach { (label, icon, dest) ->
                        NavigationBarItem(selected = state.pantalla == dest, onClick = { vm.ir(dest) }, icon = { Icon(icon, label) }, label = { Text(label) }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Navy, selectedTextColor = Navy, indicatorColor = Teal.copy(alpha = .18f)))
                    }
                }
            }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (state.cargando && state.pantalla != Pantalla.INICIO) LinearProgressIndicator(Modifier.fillMaxWidth())
                if (state.error != null) InfoCard { Text(state.error!!, color = MaterialTheme.colorScheme.error); TextButton(onClick = vm::limpiarError) { Text("Cerrar") } }
                when (state.pantalla) {
                    Pantalla.INICIO -> Inicio(vm)
                    Pantalla.PROGRAMAS -> Programas(vm)
                    Pantalla.DETALLE -> Detalle(vm, state)
                    Pantalla.COLABORAR -> Colaborar(vm)
                    Pantalla.CONFIRMACION -> Confirmacion(vm, state.codigo)
                    Pantalla.SEGUIMIENTO -> Seguimiento(vm, state)
                    Pantalla.SOBRE -> Sobre()
                    Pantalla.LOGIN -> Login(vm)
                    Pantalla.GESTION -> Gestion(vm, state)
                    Pantalla.GESTION_DETALLE -> GestionDetalle(vm, state)
                }
            }
        }
    }
}

@Composable internal fun Heading(title: String, subtitle: String? = null) {
    Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
    if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = Navy.copy(alpha = .85f))
}

@Composable internal fun PrimaryButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Teal)) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable internal fun OutlineButton(text: String, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, Navy)) { Text(text, color = Navy, fontWeight = FontWeight.Medium) }
}

@Composable internal fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(shape = CardShape, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

internal fun estadoLegible(estado: String) = when (estado) {
    "RECIBIDA" -> "Recibida"
    "EN_REVISION" -> "En revisión"
    "CONTACTADO" -> "Contactado"
    "CONCRETADO" -> "Concretado"
    "DESCARTADO" -> "Descartado"
    else -> estado
}
