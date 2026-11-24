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

private val Navy = Color(0xFF173C64)
private val Teal = Color(0xFF168B9B)
private val Green = Color(0xFF286650)
private val Cream = Color(0xFFF8F3E9)
private val CardShape = RoundedCornerShape(16.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CorpalivApp(vm: AppViewModel = viewModel()) {
    val state by vm.state.collectAsState()
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
                DemoNotice()
                when (state.pantalla) {
                    Pantalla.INICIO -> Inicio(vm)
                    Pantalla.PROGRAMAS -> Programas(vm)
                    Pantalla.DETALLE -> Detalle(vm, state.programaId)
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

@Composable private fun DemoNotice() {
    Surface(color = Teal.copy(alpha = .12f), shape = RoundedCornerShape(10.dp)) {
        Text("DEMOSTRACIÓN LOCAL · Datos ficticios, aún sin conexión al backend", Modifier.fillMaxWidth().padding(10.dp), color = Navy, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable private fun Heading(title: String, subtitle: String? = null) {
    Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
    if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = Navy.copy(alpha = .85f))
}

@Composable private fun PrimaryButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Teal)) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable private fun OutlineButton(text: String, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, Navy)) { Text(text, color = Navy, fontWeight = FontWeight.Medium) }
}

@Composable private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(shape = CardShape, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

@Composable private fun Inicio(vm: AppViewModel) {
    Heading("Conecta, conoce y colabora", "Descubre cómo apoyar la inclusión, la educación y la formación para el trabajo.")
    InfoCard {
        Text("CORPALIV / Alba Lab", color = Navy, fontWeight = FontWeight.Bold)
        Text("Un espacio para conocer iniciativas y expresar interés en colaborar.", color = Navy)
        PrimaryButton("Quiero colaborar") { vm.ir(Pantalla.COLABORAR) }
        OutlineButton("Conocer programas") { vm.ir(Pantalla.PROGRAMAS) }
    }
    Text("Programas destacados", color = Navy, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    vm.programas.take(2).forEach { ProgramaCard(it) { vm.detalle(it.id) } }
}

@Composable private fun ProgramaCard(programa: Programa, onClick: () -> Unit) {
    Card(onClick = onClick, shape = CardShape, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            val icon = when (programa.categoria) {
                "Educación" -> Icons.Default.School
                "Formación" -> Icons.Default.Work
                else -> Icons.Default.Groups
            }
            Surface(color = if (programa.categoria == "Formación") Green.copy(alpha = .12f) else Teal.copy(alpha = .12f), shape = RoundedCornerShape(14.dp)) {
                Icon(icon, null, tint = if (programa.categoria == "Formación") Green else Teal, modifier = Modifier.size(52.dp).padding(12.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(programa.categoria.uppercase(), color = Teal, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(programa.nombre, color = Navy, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(programa.resumen, color = Navy, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Ver detalle", tint = Navy)
        }
    }
}

@Composable private fun Programas(vm: AppViewModel) {
    Heading("Programas", "Explora iniciativas de ejemplo para conocer el recorrido de la aplicación.")
    vm.programas.forEach { ProgramaCard(it) { vm.detalle(it.id) } }
}

@Composable private fun Detalle(vm: AppViewModel, id: Int) {
    val programa = vm.programas.firstOrNull { it.id == id } ?: return
    Heading(programa.nombre, programa.resumen)
    InfoCard {
        Text(programa.categoria, color = Green, fontWeight = FontWeight.Bold)
        Text(programa.descripcion, color = Navy)
    }
    PrimaryButton("Quiero colaborar") { vm.ir(Pantalla.COLABORAR) }
}

@Composable private fun Colaborar(vm: AppViewModel) {
    var modalidad by remember { mutableStateOf("Voluntariado") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var acepta by remember { mutableStateOf(false) }
    var tried by remember { mutableStateOf(false) }
    val nombreError = nombre.trim().length < 2
    val correoError = !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
    val telefonoError = telefono.isNotBlank() && !Regex("^[+0-9 ()-]{7,20}$").matches(telefono.trim())
    val mensajeError = mensaje.trim().length < 10
    Heading("Quiero colaborar", "Elige una modalidad y cuéntanos cómo te gustaría participar.")
    Text("Modalidad de colaboración", color = Navy, fontWeight = FontWeight.Bold)
    listOf(listOf("Donación", "Voluntariado"), listOf("Alianza", "Capacitación")).forEach { row ->
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            row.forEach { option ->
                FilterChip(selected = modalidad == option, onClick = { modalidad = option }, label = { Text(option) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp), colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Teal.copy(alpha = .17f), selectedLabelColor = Navy), border = FilterChipDefaults.filterChipBorder(enabled = true, selected = modalidad == option, borderColor = Navy.copy(alpha = .6f), selectedBorderColor = Teal))
            }
        }
    }
    OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre *") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = tried && nombreError, supportingText = { if (tried && nombreError) Text("Ingresa al menos 2 caracteres") })
    OutlinedTextField(correo, { correo = it }, label = { Text("Correo electrónico *") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = tried && correoError, supportingText = { if (tried && correoError) Text("Ingresa un correo válido") })
    OutlinedTextField(telefono, { telefono = it }, label = { Text("Teléfono (opcional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = tried && telefonoError, supportingText = { if (tried && telefonoError) Text("Revisa el número ingresado") })
    OutlinedTextField(mensaje, { mensaje = it }, label = { Text("Mensaje *") }, modifier = Modifier.fillMaxWidth().heightIn(min = 130.dp), minLines = 3, isError = tried && mensajeError, supportingText = { if (tried && mensajeError) Text("Escribe al menos 10 caracteres") })
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = acepta, onCheckedChange = { acepta = it })
        Text("Acepto el uso de mis datos para responder esta solicitud *", color = Navy, modifier = Modifier.clickable { acepta = !acepta })
    }
    AnimatedVisibility(tried && !acepta) { Text("Debes aceptar antes de continuar", color = MaterialTheme.colorScheme.error) }
    PrimaryButton("Continuar con demostración") {
        tried = true
        if (!nombreError && !correoError && !telefonoError && !mensajeError && acepta) vm.enviar(nombre.trim(), correo.trim(), telefono.trim(), mensaje.trim(), modalidad)
    }
    Text("Este formulario no envía datos al servidor en Sprint 1.", color = Navy, style = MaterialTheme.typography.bodySmall)
}

@Composable private fun Confirmacion(vm: AppViewModel, codigo: String) {
    val context = LocalContext.current
    Text("✓", color = Green, style = MaterialTheme.typography.displayLarge, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    Heading("Solicitud de demostración creada", "El registro se guardó solo en memoria de esta app.")
    InfoCard {
        Text("Código temporal de demostración", color = Navy)
        Text(codigo, color = Green, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlineButton("Copiar código") {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Código demo", codigo))
        }
    }
    PrimaryButton("Consultar estado") { vm.ir(Pantalla.SEGUIMIENTO) }
    OutlineButton("Volver al inicio") { vm.ir(Pantalla.INICIO) }
}

@Composable private fun Seguimiento(vm: AppViewModel, state: AppState) {
    var codigo by remember(state.pantalla) { mutableStateOf(state.codigo) }
    Heading("Seguimiento", "Consulta solo el estado con tu código de demostración.")
    OutlinedTextField(codigo, { codigo = it }, label = { Text("Código de seguimiento") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    PrimaryButton("Consultar", enabled = codigo.isNotBlank()) { vm.consultar(codigo) }
    AnimatedVisibility(state.consulta != null) {
        InfoCard {
            Text("Estado de la solicitud", color = Navy, fontWeight = FontWeight.Bold)
            Text(if (state.consulta == "NO_ENCONTRADA") "Código no encontrado" else estadoLegible(state.consulta!!), color = Green, style = MaterialTheme.typography.titleLarge)
            Text("Información de demostración. No muestra datos personales.", color = Navy)
        }
    }
    Text("Prueba también con DEMO-8F3K9D", color = Navy, style = MaterialTheme.typography.bodySmall)
}

@Composable private fun Sobre() {
    Heading("Sobre CORPALIV", "Proyecto académico de demostración")
    InfoCard { Text("Esta aplicación explora una forma simple de conocer programas y expresar interés en colaborar con CORPALIV / Alba Lab. El contenido visible en Sprint 1 es ilustrativo y no representa cifras ni casos institucionales reales.", color = Navy) }
}

@Composable private fun Login(vm: AppViewModel) {
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var tried by remember { mutableStateOf(false) }
    Heading("Acceso gestor", "Vista de demostración; la autenticación real se integrará en Sprint 2.")
    OutlinedTextField(usuario, { usuario = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = tried && usuario.isBlank())
    OutlinedTextField(clave, { clave = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(), isError = tried && clave.isBlank())
    AnimatedVisibility(tried && (usuario.isBlank() || clave.isBlank())) { Text("Completa ambos campos para abrir la vista de demostración", color = MaterialTheme.colorScheme.error) }
    PrimaryButton("Abrir panel de demostración") { tried = true; if (usuario.isNotBlank() && clave.isNotBlank()) vm.entrarDemo() }
    Text("No ingreses una contraseña real: esta pantalla no autentica todavía.", color = Navy, style = MaterialTheme.typography.bodySmall)
}

@Composable private fun Gestion(vm: AppViewModel, state: AppState) {
    var selected by remember { mutableStateOf("Todas") }
    var deleteCode by remember { mutableStateOf<String?>(null) }
    Heading("Solicitudes", "Panel temporal de gestión en memoria")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Todas", "Recibidas", "En revisión").forEach { f -> FilterChip(selected = selected == f, onClick = { selected = f }, label = { Text(f) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Teal.copy(alpha = .17f), selectedLabelColor = Navy), border = FilterChipDefaults.filterChipBorder(enabled = true, selected = selected == f, borderColor = Navy.copy(alpha = .6f), selectedBorderColor = Teal)) }
    }
    val shown = state.solicitudes.filter { selected == "Todas" || (selected == "Recibidas" && it.estado == "RECIBIDA") || (selected == "En revisión" && it.estado == "EN_REVISION") }
    if (shown.isEmpty()) InfoCard { Text("No hay solicitudes en este filtro.", color = Navy) }
    shown.forEach { solicitud ->
        InfoCard {
            Text(solicitud.nombre, color = Navy, fontWeight = FontWeight.Bold)
            Text("${solicitud.modalidad} · ${solicitud.codigo}", color = Navy)
            Text(estadoLegible(solicitud.estado), color = Green)
            TextButton(onClick = { vm.detalleGestion(solicitud.codigo) }) { Text("Ver detalle y editar") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { vm.estado(solicitud.codigo, "EN_REVISION") }) { Text("En revisión") }
                TextButton(onClick = { vm.estado(solicitud.codigo, "CONTACTADO") }) { Text("Contactado") }
            }
            TextButton(onClick = { deleteCode = solicitud.codigo }) { Text("Eliminar demo", color = MaterialTheme.colorScheme.error) }
        }
    }
    OutlineButton("Cerrar sesión de demostración") { vm.salir() }
    if (deleteCode != null) AlertDialog(onDismissRequest = { deleteCode = null }, title = { Text("Eliminar solicitud de demostración") }, text = { Text("Esta acción solo afecta los datos en memoria.") }, confirmButton = { TextButton(onClick = { vm.eliminar(deleteCode!!); deleteCode = null }) { Text("Eliminar") } }, dismissButton = { TextButton(onClick = { deleteCode = null }) { Text("Cancelar") } })
}

@Composable private fun GestionDetalle(vm: AppViewModel, state: AppState) {
    val solicitud = state.solicitudes.find { it.codigo == state.solicitudCodigo }
    if (solicitud == null) { Heading("Solicitud no disponible"); return }
    var nombre by remember(solicitud.codigo) { mutableStateOf(solicitud.nombre) }
    Heading("Detalle de solicitud", "Registro ficticio guardado solo en memoria")
    InfoCard {
        Text("Código: ${solicitud.codigo}", color = Navy, fontWeight = FontWeight.Bold)
        Text("Modalidad: ${solicitud.modalidad}", color = Navy)
        Text("Estado: ${estadoLegible(solicitud.estado)}", color = Green)
        Text("Correo: ${solicitud.correo}", color = Navy)
        if (solicitud.telefono.isNotBlank()) Text("Teléfono: ${solicitud.telefono}", color = Navy)
        Text("Mensaje: ${solicitud.mensaje}", color = Navy)
    }
    OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre de demostración") }, modifier = Modifier.fillMaxWidth())
    PrimaryButton("Guardar nombre", enabled = nombre.trim().length >= 2) { vm.nombre(solicitud.codigo, nombre.trim()) }
    Text("Actualizar estado", color = Navy, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    listOf("RECIBIDA", "EN_REVISION", "CONTACTADO", "CONCRETADO", "DESCARTADO").forEach { estado ->
        OutlinedButton(onClick = { vm.estado(solicitud.codigo, estado) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), border = BorderStroke(1.dp, if (solicitud.estado == estado) Teal else Navy.copy(alpha = .4f))) { Text(estadoLegible(estado), color = Navy) }
    }
    OutlineButton("Volver a solicitudes") { vm.ir(Pantalla.GESTION) }
}

private fun estadoLegible(estado: String) = when (estado) {
    "RECIBIDA" -> "Recibida"
    "EN_REVISION" -> "En revisión"
    "CONTACTADO" -> "Contactado"
    "CONCRETADO" -> "Concretado"
    "DESCARTADO" -> "Descartado"
    else -> estado
}
