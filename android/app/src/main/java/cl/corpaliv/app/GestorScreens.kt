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

@Composable internal fun Login(vm: AppViewModel) {
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var tried by remember { mutableStateOf(false) }
    Heading("Acceso gestor", "Ingresa tus credenciales para administrar solicitudes.")
    OutlinedTextField(usuario, { usuario = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = tried && usuario.isBlank())
    OutlinedTextField(clave, { clave = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(), isError = tried && clave.isBlank())
    AnimatedVisibility(tried && (usuario.isBlank() || clave.isBlank())) { Text("Completa ambos campos", color = MaterialTheme.colorScheme.error) }
    PrimaryButton("Ingresar") { tried = true; if (usuario.isNotBlank() && clave.isNotBlank()) vm.entrar(usuario, clave) }
}

@Composable internal fun Gestion(vm: AppViewModel, state: AppState) {
    var selected by remember { mutableStateOf("Todas") }
    var deleteCode by remember { mutableStateOf<String?>(null) }
    Heading("Solicitudes", "Gestión de solicitudes registradas")
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
            TextButton(onClick = { deleteCode = solicitud.codigo }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
        }
    }
    OutlineButton("Cerrar sesión") { vm.salir() }
    if (deleteCode != null) AlertDialog(onDismissRequest = { deleteCode = null }, title = { Text("Eliminar solicitud") }, text = { Text("La solicitud se eliminará del servidor.") }, confirmButton = { TextButton(onClick = { vm.eliminar(deleteCode!!); deleteCode = null }) { Text("Eliminar") } }, dismissButton = { TextButton(onClick = { deleteCode = null }) { Text("Cancelar") } })
}

@Composable internal fun GestionDetalle(vm: AppViewModel, state: AppState) {
    val solicitud = state.solicitudes.find { it.codigo == state.solicitudCodigo }
    if (solicitud == null) { Heading("Solicitud no disponible"); return }
    var nombre by remember(solicitud.codigo) { mutableStateOf(solicitud.nombre) }
    Heading("Detalle de solicitud", "Registro guardado en MySQL")
    InfoCard {
        Text("Código: ${solicitud.codigo}", color = Navy, fontWeight = FontWeight.Bold)
        Text("Modalidad: ${solicitud.modalidad}", color = Navy)
        Text("Estado: ${estadoLegible(solicitud.estado)}", color = Green)
        Text("Correo: ${solicitud.correo}", color = Navy)
        if (!solicitud.telefono.isNullOrBlank()) Text("Teléfono: ${solicitud.telefono}", color = Navy)
        Text("Mensaje: ${solicitud.mensaje}", color = Navy)
    }
    OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
    PrimaryButton("Guardar nombre", enabled = nombre.trim().length >= 2) { vm.nombre(solicitud.codigo, nombre.trim()) }
    Text("Actualizar estado", color = Navy, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    listOf("RECIBIDA", "EN_REVISION", "CONTACTADO", "CONCRETADO", "DESCARTADO").forEach { estado ->
        OutlinedButton(onClick = { vm.estado(solicitud.codigo, estado) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), border = BorderStroke(1.dp, if (solicitud.estado == estado) Teal else Navy.copy(alpha = .4f))) { Text(estadoLegible(estado), color = Navy) }
    }
    OutlineButton("Volver a solicitudes") { vm.ir(Pantalla.GESTION) }
}
