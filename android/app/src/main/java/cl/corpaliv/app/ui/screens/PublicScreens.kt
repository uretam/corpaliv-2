package cl.corpaliv.app.ui.screens

import cl.corpaliv.app.data.model.*
import cl.corpaliv.app.viewmodel.*

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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

@Composable internal fun Inicio(vm: AppViewModel) {
    Heading("Conecta, conoce y colabora", "Descubre cómo apoyar la inclusión, la educación y la formación para el trabajo.")
    InfoCard {
        Text("CORPALIV / Alba Lab", color = Navy, fontWeight = FontWeight.Bold)
        Text("Un espacio para conocer iniciativas y expresar interés en colaborar.", color = Navy)
        PrimaryButton("Quiero colaborar") { vm.ir(Pantalla.COLABORAR) }
        OutlineButton("Conocer programas") { vm.ir(Pantalla.PROGRAMAS) }
    }
}

@Composable internal fun ProgramaCard(programa: Programa, onClick: () -> Unit) {
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

@Composable internal fun Programas(vm: AppViewModel) {
    val state by vm.state.collectAsState()
    Heading("Programas", "Explora iniciativas de ejemplo para conocer el recorrido de la aplicación.")
    state.programas.forEach { ProgramaCard(it) { vm.detalle(it.id) } }
    if (state.programas.isEmpty() && !state.cargando) OutlineButton("Reintentar carga") { vm.cargarProgramas() }
    InfoCard {
        Text("Próximo feriado en Chile", color = Navy, fontWeight = FontWeight.Bold)
        Text(state.feriado?.let { "${it.date} · ${it.title}" } ?: if (state.feriadoError) "No disponible ahora. Los programas siguen funcionando." else "Cargando feriados…", color = Navy)
        if (state.feriadoError) TextButton(onClick = vm::cargarFeriado) { Text("Reintentar") }
    }
}

@Composable internal fun Detalle(vm: AppViewModel, state: AppState) {
    val programa = state.programaDetalle ?: state.programas.firstOrNull { it.id == state.programaId } ?: return
    Heading(programa.nombre, programa.resumen)
    InfoCard {
        Text(programa.categoria, color = Green, fontWeight = FontWeight.Bold)
        Text(programa.descripcion, color = Navy)
    }
    PrimaryButton("Quiero colaborar") { vm.ir(Pantalla.COLABORAR) }
}

@Composable internal fun Colaborar(vm: AppViewModel) {
    var modalidad by remember { mutableStateOf("Voluntariado") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var acepta by remember { mutableStateOf(false) }
    var tried by remember { mutableStateOf(false) }
    val nombreError = !FormularioValidator.nombreValido(nombre)
    val correoError = !FormularioValidator.correoValido(correo)
    val telefonoError = !FormularioValidator.telefonoValido(telefono)
    val mensajeError = !FormularioValidator.mensajeValido(mensaje)
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
    PrimaryButton("Enviar solicitud", enabled = !vm.state.collectAsState().value.cargando) {
        tried = true
        if (!nombreError && !correoError && !telefonoError && !mensajeError && acepta) vm.enviar(nombre.trim(), correo.trim(), telefono.trim(), mensaje.trim(), modalidad)
    }
    Text("Tu solicitud se guardará en el servidor de CORPALIV para esta evaluación.", color = Navy, style = MaterialTheme.typography.bodySmall)
}

@Composable internal fun Confirmacion(vm: AppViewModel, codigo: String) {
    val context = LocalContext.current
    Text("✓", color = Green, style = MaterialTheme.typography.displayLarge, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    Heading("Solicitud enviada", "Tu solicitud fue guardada. Conserva el código para consultar su estado.")
    InfoCard {
        Text("Código de seguimiento", color = Navy)
        Text(codigo, color = Green, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlineButton("Copiar código") {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Código de seguimiento", codigo))
        }
    }
    PrimaryButton("Consultar estado") { vm.ir(Pantalla.SEGUIMIENTO) }
    OutlineButton("Volver al inicio") { vm.ir(Pantalla.INICIO) }
}

@Composable internal fun Seguimiento(vm: AppViewModel, state: AppState) {
    var codigo by remember(state.pantalla) { mutableStateOf(state.codigo) }
    Heading("Seguimiento", "Consulta el estado con tu código de seguimiento.")
    OutlinedTextField(codigo, { codigo = it }, label = { Text("Código de seguimiento") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    PrimaryButton("Consultar", enabled = codigo.isNotBlank()) { vm.consultar(codigo) }
    AnimatedVisibility(state.consulta != null) {
        InfoCard {
            Text("Estado de la solicitud", color = Navy, fontWeight = FontWeight.Bold)
            Text(if (state.consulta == "NO_ENCONTRADA") "Código no encontrado" else estadoLegible(state.consulta!!), color = Green, style = MaterialTheme.typography.titleLarge)
            Text("Esta consulta pública no muestra datos personales.", color = Navy)
        }
    }
}

@Composable internal fun Sobre() {
    Heading("Sobre CORPALIV", "Proyecto académico")
    InfoCard { Text("Esta aplicación permite conocer programas de demostración institucional y expresar interés en colaborar con CORPALIV / Alba Lab. Los programas de ejemplo no representan cifras ni casos institucionales reales.", color = Navy) }
}
