package cl.corpaliv.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun preparar() { Dispatchers.setMain(dispatcher) }
    @After fun limpiar() { Dispatchers.resetMain() }

    private class FalsoRepositorio : AppRepository {
        val programa = Programa(1, "Educación", "Resumen", "Detalle", "Educación")
        val solicitud = Solicitud(7, "codigo", "Ana", "ana@example.com", null, "Mensaje válido", "Voluntariado", "RECIBIDA")
        var lista = listOf(programa)
        var solicitudesGuardadas = listOf(solicitud)
        var error: Exception? = null
        var loginValido = true
        var ultimoEnvio: CrearSolicitud? = null
        var actualizacion: Map<String, String>? = null
        var eliminado = false
        private fun revisar() { error?.let { throw it } }
        override suspend fun programas(): List<Programa> { revisar(); return lista }
        override suspend fun detalle(id: Int): Programa { revisar(); return programa }
        override suspend fun crear(data: CrearSolicitud): String { revisar(); ultimoEnvio = data; return "codigo" }
        override suspend fun estado(codigo: String): String { revisar(); return "CONTACTADO" }
        override suspend fun login(auth: String): Boolean { revisar(); return loginValido }
        override suspend fun solicitudes(auth: String): List<Solicitud> { revisar(); return solicitudesGuardadas }
        override suspend fun actualizar(auth: String, id: Long, data: Map<String, String>): Solicitud { revisar(); actualizacion = data; return solicitud }
        override suspend fun eliminar(auth: String, id: Long) { revisar(); eliminado = true; solicitudesGuardadas = emptyList() }
        override suspend fun proximoFeriado(): Feriado? { revisar(); return Feriado("2026-12-08", "Feriado") }
        override fun basic(usuario: String, clave: String) = "Basic prueba"
    }

    private fun http(codigo: Int) = HttpException(Response.error<String>(codigo, "error".toResponseBody()))

    @Test fun cargaProgramasDetalleYFeriado() = runTest(dispatcher) {
        val repo = FalsoRepositorio(); val vm = AppViewModel(repo)
        vm.cargarProgramas(); vm.cargarFeriado(); advanceUntilIdle()
        assertEquals(1, vm.state.value.programas.size)
        assertEquals("Feriado", vm.state.value.feriado?.title)
        vm.detalle(1); advanceUntilIdle()
        assertEquals(Pantalla.DETALLE, vm.state.value.pantalla)
        assertEquals(1, vm.state.value.programaDetalle?.id)
    }

    @Test fun envioYSeguimientoReal() = runTest(dispatcher) {
        val repo = FalsoRepositorio(); val vm = AppViewModel(repo)
        vm.enviar("Ana", "ana@example.com", "", "Mensaje válido", "Voluntariado"); advanceUntilIdle()
        assertEquals(Pantalla.CONFIRMACION, vm.state.value.pantalla)
        assertEquals("codigo", vm.state.value.codigo)
        assertTrue(repo.ultimoEnvio!!.aceptaPrivacidad)
        vm.consultar("codigo"); advanceUntilIdle()
        assertEquals("CONTACTADO", vm.state.value.consulta)
    }

    @Test fun erroresDeSeguimientoSonClaros() = runTest(dispatcher) {
        val repo = FalsoRepositorio(); val vm = AppViewModel(repo)
        repo.error = http(404); vm.consultar("no-existe"); advanceUntilIdle()
        assertEquals("NO_ENCONTRADA", vm.state.value.consulta)
        repo.error = http(429); vm.consultar("codigo"); advanceUntilIdle()
        assertTrue(vm.state.value.error!!.contains("Demasiadas"))
        repo.error = http(500); vm.consultar("codigo"); advanceUntilIdle()
        assertTrue(vm.state.value.error!!.contains("No se pudo consultar"))
        repo.error = IllegalStateException("sin red"); vm.consultar("codigo"); advanceUntilIdle()
        assertNotNull(vm.state.value.error)
        vm.limpiarError(); assertNull(vm.state.value.error)
    }

    @Test fun loginCrudYLogout() = runTest(dispatcher) {
        val repo = FalsoRepositorio(); val vm = AppViewModel(repo)
        repo.loginValido = false; vm.entrar("gestor", "mala"); advanceUntilIdle()
        assertFalse(vm.state.value.gestorAutenticado)
        assertEquals("Credenciales incorrectas", vm.state.value.error)
        repo.loginValido = true; vm.entrar("gestor", "correcta"); advanceUntilIdle()
        assertTrue(vm.state.value.gestorAutenticado)
        assertEquals(1, vm.state.value.solicitudes.size)
        vm.detalleGestion("codigo"); assertEquals(Pantalla.GESTION_DETALLE, vm.state.value.pantalla)
        vm.estado("codigo", "CONTACTADO"); advanceUntilIdle()
        assertEquals("CONTACTADO", repo.actualizacion?.get("estado"))
        vm.nombre("codigo", "Nuevo nombre"); advanceUntilIdle()
        assertEquals("Nuevo nombre", repo.actualizacion?.get("nombre"))
        vm.eliminar("codigo"); advanceUntilIdle()
        assertTrue(repo.eliminado)
        assertTrue(vm.state.value.solicitudes.isEmpty())
        vm.salir(); assertFalse(vm.state.value.gestorAutenticado)
        assertEquals(Pantalla.INICIO, vm.state.value.pantalla)
    }

    @Test fun erroresDeCargaYLoginNoAutorizan() = runTest(dispatcher) {
        val repo = FalsoRepositorio(); val vm = AppViewModel(repo)
        repo.error = IllegalStateException("sin red")
        vm.cargarProgramas(); vm.cargarFeriado(); vm.entrar("gestor", "clave"); advanceUntilIdle()
        assertNotNull(vm.state.value.error)
        assertTrue(vm.state.value.feriadoError)
        assertFalse(vm.state.value.gestorAutenticado)
    }
}
