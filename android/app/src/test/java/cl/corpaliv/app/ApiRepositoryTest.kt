package cl.corpaliv.app

import cl.corpaliv.app.data.model.*
import cl.corpaliv.app.data.repository.*
import cl.corpaliv.app.viewmodel.*

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.time.LocalDate

class ApiRepositoryTest {
    private val contenidos = MockWebServer()
    private val vinculacion = MockWebServer()
    private val feriados = MockWebServer()
    private lateinit var repo: ApiRepository

    @Before fun preparar() {
        contenidos.start(); vinculacion.start(); feriados.start()
        repo = ApiRepository(contenidos.url("/").toString(), vinculacion.url("/").toString(),
            feriados.url("/").toString()) { LocalDate.of(2026, 11, 24) }
    }
    @After fun cerrar() { contenidos.shutdown(); vinculacion.shutdown(); feriados.shutdown() }

    @Test fun listaYDetalleUsanServicioDeContenidos() = runBlocking<Unit> {
        val json = """{"id":1,"nombre":"Educación","resumen":"Resumen","descripcion":"Detalle","categoria":"Educación"}"""
        contenidos.enqueue(MockResponse().setBody("[$json]"))
        contenidos.enqueue(MockResponse().setBody(json))
        assertEquals(1, repo.programas().size)
        assertEquals("Educación", repo.detalle(1).nombre)
        assertEquals("/programas", contenidos.takeRequest().path)
        assertEquals("/programas/1", contenidos.takeRequest().path)
    }

    @Test fun creaConsultaYAutenticaConRespuestasReales() = runBlocking<Unit> {
        vinculacion.enqueue(MockResponse().setResponseCode(201).setBody("""{"codigoSeguimiento":"uuid"}"""))
        vinculacion.enqueue(MockResponse().setBody("""{"estado":"RECIBIDA"}"""))
        vinculacion.enqueue(MockResponse().setBody("OK"))
        vinculacion.enqueue(MockResponse().setResponseCode(401))
        val codigo = repo.crear(CrearSolicitud("Ana", "ana@example.com", "", "Mensaje válido", "Voluntariado"))
        assertEquals("uuid", codigo)
        val creacion = vinculacion.takeRequest()
        assertEquals("POST", creacion.method)
        assertTrue(creacion.body.readUtf8().contains("\"aceptaPrivacidad\":true"))
        assertEquals("RECIBIDA", repo.estado(codigo))
        assertEquals("/seguimiento/uuid", vinculacion.takeRequest().path)
        val auth = repo.basic("gestor", "clave")
        assertEquals("Basic Z2VzdG9yOmNsYXZl", auth)
        assertTrue(repo.login(auth))
        assertEquals(auth, vinculacion.takeRequest().getHeader("Authorization"))
        assertFalse(repo.login(auth))
        vinculacion.takeRequest()
    }

    @Test fun operacionesGestorUsanRutaYAutorizacion() = runBlocking<Unit> {
        val json = """{"id":7,"codigoSeguimiento":"uuid","nombre":"Ana","correo":"ana@example.com","telefono":null,"mensaje":"Mensaje válido","modalidad":"Voluntariado","estado":"CONTACTADO"}"""
        vinculacion.enqueue(MockResponse().setBody("[$json]"))
        vinculacion.enqueue(MockResponse().setBody(json))
        vinculacion.enqueue(MockResponse().setResponseCode(204))
        assertEquals(1, repo.solicitudes("Basic prueba").size)
        assertEquals("/admin/solicitudes", vinculacion.takeRequest().path)
        assertEquals("CONTACTADO", repo.actualizar("Basic prueba", 7, mapOf("estado" to "CONTACTADO")).estado)
        val cambio = vinculacion.takeRequest()
        assertEquals("PUT", cambio.method)
        assertEquals("Basic prueba", cambio.getHeader("Authorization"))
        repo.eliminar("Basic prueba", 7)
        assertEquals("DELETE", vinculacion.takeRequest().method)
    }

    @Test fun feriadoIndependienteYEfectoDeErrores() = runBlocking<Unit> {
        feriados.enqueue(MockResponse().setBody("""{"data":[{"date":"2026-09-18","title":"Pasado"},{"date":"2026-12-08","title":"Próximo"}]}"""))
        assertEquals("Próximo", repo.proximoFeriado()?.title)
        assertEquals("/feriados.json", feriados.takeRequest().path)
        feriados.enqueue(MockResponse().setBody("""{"data":[]}"""))
        assertNull(repo.proximoFeriado())
        feriados.takeRequest()
        vinculacion.enqueue(MockResponse().setResponseCode(503))
        try { repo.login("Basic prueba"); fail("Debe propagar el error 503") }
        catch (e: HttpException) { assertEquals(503, e.code()) }
        vinculacion.takeRequest()
    }
}
