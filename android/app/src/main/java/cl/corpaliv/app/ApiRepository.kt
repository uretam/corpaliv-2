package cl.corpaliv.app

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

data class Programa(val id: Int, val nombre: String, val resumen: String, val descripcion: String, val categoria: String)
data class Solicitud(val id: Long, val codigoSeguimiento: String, val nombre: String, val correo: String,
    val telefono: String?, val mensaje: String, val modalidad: String, val estado: String) {
    val codigo: String get() = codigoSeguimiento
}
data class CrearSolicitud(val nombre: String, val correo: String, val telefono: String, val mensaje: String,
    val modalidad: String, val aceptaPrivacidad: Boolean = true)
data class CodigoRespuesta(val codigoSeguimiento: String)
data class EstadoRespuesta(val estado: String)
data class Feriado(val date: String, val title: String)
data class FeriadosRespuesta(val data: List<Feriado>)

interface AppRepository {
    suspend fun programas(): List<Programa>
    suspend fun detalle(id: Int): Programa
    suspend fun crear(data: CrearSolicitud): String
    suspend fun estado(codigo: String): String
    suspend fun login(auth: String): Boolean
    suspend fun solicitudes(auth: String): List<Solicitud>
    suspend fun actualizar(auth: String, id: Long, data: Map<String, String>): Solicitud
    suspend fun eliminar(auth: String, id: Long)
    suspend fun proximoFeriado(): Feriado?
    fun basic(usuario: String, clave: String): String
}

private interface ContenidosApi {
    @GET("programas") suspend fun programas(): List<Programa>
    @GET("programas/{id}") suspend fun detalle(@Path("id") id: Int): Programa
}
private interface VinculacionApi {
    @POST("solicitudes") suspend fun crear(@Body data: CrearSolicitud): CodigoRespuesta
    @GET("seguimiento/{codigo}") suspend fun estado(@Path("codigo") codigo: String): EstadoRespuesta
    @GET("admin/sesion") suspend fun login(@Header("Authorization") auth: String): retrofit2.Response<Void>
    @GET("admin/solicitudes") suspend fun listar(@Header("Authorization") auth: String): List<Solicitud>
    @GET("admin/solicitudes/{id}") suspend fun detalle(@Header("Authorization") auth: String, @Path("id") id: Long): Solicitud
    @PUT("admin/solicitudes/{id}") suspend fun actualizar(@Header("Authorization") auth: String, @Path("id") id: Long, @Body data: Map<String, String>): Solicitud
    @DELETE("admin/solicitudes/{id}") suspend fun eliminar(@Header("Authorization") auth: String, @Path("id") id: Long)
}
private interface FeriadosApi { @GET("feriados.json") suspend fun listar(): FeriadosRespuesta }

class ApiRepository(
    contenidosUrl: String = "http://10.0.2.2:8081/",
    vinculacionUrl: String = "http://10.0.2.2:8082/",
    feriadosUrl: String = "https://api.roison.cl/",
    private val hoy: () -> java.time.LocalDate = { java.time.LocalDate.now() }
) : AppRepository {
    private val client = OkHttpClient.Builder().connectTimeout(8, TimeUnit.SECONDS).readTimeout(8, TimeUnit.SECONDS).callTimeout(12, TimeUnit.SECONDS).build()
    private fun retrofit(url: String) = Retrofit.Builder().baseUrl(url).client(client).addConverterFactory(GsonConverterFactory.create()).build()
    private val contenidos = retrofit(contenidosUrl).create(ContenidosApi::class.java)
    private val vinculacion = retrofit(vinculacionUrl).create(VinculacionApi::class.java)
    private val feriados = retrofit(feriadosUrl).create(FeriadosApi::class.java)
    override suspend fun programas() = contenidos.programas()
    override suspend fun detalle(id: Int) = contenidos.detalle(id)
    override suspend fun crear(data: CrearSolicitud) = vinculacion.crear(data).codigoSeguimiento
    override suspend fun estado(codigo: String) = vinculacion.estado(codigo).estado
    override suspend fun login(auth: String): Boolean {
        val respuesta = vinculacion.login(auth)
        if (respuesta.code() == 401) return false
        if (!respuesta.isSuccessful) throw retrofit2.HttpException(respuesta)
        return true
    }
    override suspend fun solicitudes(auth: String) = vinculacion.listar(auth)
    override suspend fun actualizar(auth: String, id: Long, data: Map<String, String>) = vinculacion.actualizar(auth, id, data)
    override suspend fun eliminar(auth: String, id: Long) { vinculacion.eliminar(auth, id) }
    override suspend fun proximoFeriado(): Feriado? = feriados.listar().data.filter { it.date >= hoy().toString() }.minByOrNull { it.date }
    override fun basic(usuario: String, clave: String): String = "Basic " + java.util.Base64.getEncoder().encodeToString("$usuario:$clave".toByteArray())
}
