package cl.corpaliv.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Pantalla { INICIO, PROGRAMAS, DETALLE, COLABORAR, CONFIRMACION, SEGUIMIENTO, SOBRE, LOGIN, GESTION, GESTION_DETALLE }
data class AppState(
    val pantalla: Pantalla = Pantalla.INICIO,
    val programaId: Int = 1,
    val codigo: String = "",
    val consulta: String? = null,
    val solicitudes: List<SolicitudDemo> = emptyList(),
    val solicitudCodigo: String = "",
    val gestorDemo: Boolean = false
)

class AppViewModel : ViewModel() {
    private val repo = DemoRepository()
    val programas = repo.programas
    private val _state = MutableStateFlow(AppState(solicitudes = repo.listar()))
    val state = _state.asStateFlow()
    fun ir(pantalla: Pantalla) { _state.value = _state.value.copy(pantalla = pantalla, consulta = null) }
    fun detalle(id: Int) { _state.value = _state.value.copy(pantalla = Pantalla.DETALLE, programaId = id) }
    fun enviar(nombre: String, correo: String, telefono: String, mensaje: String, modalidad: String) {
        _state.value = _state.value.copy(pantalla = Pantalla.CONFIRMACION, codigo = repo.crear(nombre, correo, telefono, mensaje, modalidad), solicitudes = repo.listar())
    }
    fun consultar(codigo: String) { _state.value = _state.value.copy(consulta = repo.estado(codigo) ?: "NO_ENCONTRADA") }
    fun entrarDemo() { _state.value = _state.value.copy(pantalla = Pantalla.GESTION, gestorDemo = true) }
    fun detalleGestion(codigo: String) { _state.value = _state.value.copy(pantalla = Pantalla.GESTION_DETALLE, solicitudCodigo = codigo) }
    fun salir() { _state.value = _state.value.copy(pantalla = Pantalla.INICIO, gestorDemo = false) }
    fun estado(codigo: String, estado: String) { repo.cambiarEstado(codigo, estado); _state.value = _state.value.copy(solicitudes = repo.listar()) }
    fun nombre(codigo: String, nombre: String) { repo.cambiarNombre(codigo, nombre); _state.value = _state.value.copy(solicitudes = repo.listar()) }
    fun eliminar(codigo: String) { repo.eliminar(codigo); _state.value = _state.value.copy(solicitudes = repo.listar()) }
}
