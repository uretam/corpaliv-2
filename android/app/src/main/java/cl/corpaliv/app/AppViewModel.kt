package cl.corpaliv.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Pantalla { INICIO, PROGRAMAS, DETALLE, COLABORAR, CONFIRMACION, SEGUIMIENTO, SOBRE, LOGIN, GESTION, GESTION_DETALLE }
data class AppState(val pantalla: Pantalla = Pantalla.INICIO, val programaId: Int = 1,
    val programas: List<Programa> = emptyList(), val programaDetalle: Programa? = null, val codigo: String = "", val consulta: String? = null,
    val solicitudes: List<Solicitud> = emptyList(), val solicitudCodigo: String = "", val gestorAutenticado: Boolean = false,
    val cargando: Boolean = false, val error: String? = null, val feriado: Feriado? = null, val feriadoError: Boolean = false)

class AppViewModel(private val repo: AppRepository) : ViewModel() {
    constructor() : this(ApiRepository())
    private var autorizacion: String? = null // Solo memoria.
    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()
    val programas: List<Programa> get() = _state.value.programas
    private fun fallo() { _state.value = _state.value.copy(cargando = false, error = "No se pudo completar la operación. Comprueba la conexión.") }
    fun limpiarError() { _state.value = _state.value.copy(error = null) }
    fun cargarProgramas() = viewModelScope.launch {
        _state.value = _state.value.copy(cargando = true, error = null)
        try { _state.value = _state.value.copy(programas = repo.programas(), cargando = false) }
        catch (e: Exception) { fallo() }
    }
    fun cargarFeriado() = viewModelScope.launch {
        try { _state.value = _state.value.copy(feriado = repo.proximoFeriado(), feriadoError = false) }
        catch (e: Exception) { _state.value = _state.value.copy(feriadoError = true) }
    }
    fun ir(pantalla: Pantalla) {
        _state.value = _state.value.copy(pantalla = pantalla, consulta = null, error = null)
        if (pantalla == Pantalla.PROGRAMAS && programas.isEmpty()) cargarProgramas()
        if (pantalla == Pantalla.GESTION) cargarSolicitudes()
    }
    fun detalle(id: Int) = viewModelScope.launch {
        _state.value = _state.value.copy(pantalla = Pantalla.DETALLE, programaId = id, programaDetalle = null, cargando = true, error = null)
        try { _state.value = _state.value.copy(programaDetalle = repo.detalle(id), cargando = false) }
        catch (e: Exception) { fallo() }
    }
    fun enviar(nombre: String, correo: String, telefono: String, mensaje: String, modalidad: String) = viewModelScope.launch {
        _state.value = _state.value.copy(cargando = true, error = null)
        try { _state.value = _state.value.copy(pantalla = Pantalla.CONFIRMACION,
            codigo = repo.crear(CrearSolicitud(nombre, correo, telefono, mensaje, modalidad)), cargando = false) }
        catch (e: Exception) { fallo() }
    }
    fun consultar(codigo: String) = viewModelScope.launch {
        _state.value = _state.value.copy(cargando = true, consulta = null, error = null)
        try { _state.value = _state.value.copy(consulta = repo.estado(codigo), cargando = false) }
        catch (e: retrofit2.HttpException) { _state.value = _state.value.copy(consulta = if (e.code() == 404) "NO_ENCONTRADA" else null,
            cargando = false, error = when (e.code()) { 404 -> null; 429 -> "Demasiadas consultas. Espera un minuto."; else -> "No se pudo consultar el estado. Inténtalo nuevamente." }) }
        catch (e: Exception) { fallo() }
    }
    fun entrar(usuario: String, clave: String) = viewModelScope.launch {
        _state.value = _state.value.copy(cargando = true, error = null)
        val auth = repo.basic(usuario, clave)
        try {
            if (!repo.login(auth)) { _state.value = _state.value.copy(cargando = false, error = "Credenciales incorrectas"); return@launch }
            autorizacion = auth
            _state.value = _state.value.copy(pantalla = Pantalla.GESTION, gestorAutenticado = true, cargando = false)
            cargarSolicitudes()
        } catch (e: Exception) { fallo() }
    }
    fun cargarSolicitudes() = viewModelScope.launch {
        val auth = autorizacion ?: return@launch
        _state.value = _state.value.copy(cargando = true, error = null)
        try { _state.value = _state.value.copy(solicitudes = repo.solicitudes(auth), cargando = false) } catch (e: Exception) { fallo() }
    }
    fun detalleGestion(codigo: String) { _state.value = _state.value.copy(pantalla = Pantalla.GESTION_DETALLE, solicitudCodigo = codigo) }
    fun salir() { autorizacion = null; _state.value = _state.value.copy(pantalla = Pantalla.INICIO, gestorAutenticado = false, solicitudes = emptyList()) }
    fun estado(codigo: String, estado: String) = actualizar(codigo, mapOf("estado" to estado))
    fun nombre(codigo: String, nombre: String) = actualizar(codigo, mapOf("nombre" to nombre))
    private fun actualizar(codigo: String, data: Map<String, String>) = viewModelScope.launch {
        val auth = autorizacion ?: return@launch
        val id = _state.value.solicitudes.find { it.codigo == codigo }?.id ?: return@launch
        _state.value = _state.value.copy(cargando = true, error = null)
        try { repo.actualizar(auth, id, data); _state.value = _state.value.copy(solicitudes = repo.solicitudes(auth), cargando = false) }
        catch (e: Exception) { fallo() }
    }
    fun eliminar(codigo: String) = viewModelScope.launch {
        val auth = autorizacion ?: return@launch
        val id = _state.value.solicitudes.find { it.codigo == codigo }?.id ?: return@launch
        _state.value = _state.value.copy(cargando = true, error = null)
        try { repo.eliminar(auth, id); _state.value = _state.value.copy(solicitudes = repo.solicitudes(auth), cargando = false) }
        catch (e: Exception) { fallo() }
    }
}
