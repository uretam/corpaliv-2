package cl.corpaliv.app

import android.util.Base64
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

class ApiRepository {
    private val client = OkHttpClient.Builder().connectTimeout(8, TimeUnit.SECONDS).readTimeout(8, TimeUnit.SECONDS).callTimeout(12, TimeUnit.SECONDS).build()
    private fun retrofit(url: String) = Retrofit.Builder().baseUrl(url).client(client).addConverterFactory(GsonConverterFactory.create()).build()
    private val contenidos = retrofit("http://10.0.2.2:8081/").create(ContenidosApi::class.java)
    private val vinculacion = retrofit("http://10.0.2.2:8082/").create(VinculacionApi::class.java)
    private val feriados = retrofit("https://api.roison.cl/").create(FeriadosApi::class.java)
    suspend fun programas() = contenidos.programas()
    suspend fun detalle(id: Int) = contenidos.detalle(id)
    suspend fun crear(data: CrearSolicitud) = vinculacion.crear(data).codigoSeguimiento
    suspend fun estado(codigo: String) = vinculacion.estado(codigo).estado
    suspend fun login(auth: String) = vinculacion.login(auth).isSuccessful
    suspend fun solicitudes(auth: String) = vinculacion.listar(auth)
    suspend fun actualizar(auth: String, id: Long, data: Map<String, String>) = vinculacion.actualizar(auth, id, data)
    suspend fun eliminar(auth: String, id: Long) = vinculacion.eliminar(auth, id)
    suspend fun proximoFeriado(): Feriado? = feriados.listar().data.filter { it.date >= java.time.LocalDate.now().toString() }.minByOrNull { it.date }
    fun basic(usuario: String, clave: String): String = "Basic " + Base64.encodeToString("$usuario:$clave".toByteArray(), Base64.NO_WRAP)
}
