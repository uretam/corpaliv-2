package cl.corpaliv.app

import cl.corpaliv.app.data.model.*
import cl.corpaliv.app.data.repository.*
import cl.corpaliv.app.viewmodel.*

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntegracionRealTest {
    @Test fun androidServiciosMysqlYFeriados() = runBlocking {
        val repo = ApiRepository()
        val programas = repo.programas()
        assertTrue(programas.isNotEmpty())
        assertEquals(programas.first().id, repo.detalle(programas.first().id).id)

        val codigo = repo.crear(CrearSolicitud("Prueba Instrumental Sprint3", "instrumental@example.com", "",
            "Solicitud ficticia de verificación integral", "Voluntariado"))
        assertEquals(36, codigo.length)
        assertEquals("RECIBIDA", repo.estado(codigo))
        assertNotNull(repo.proximoFeriado())
    }
}
