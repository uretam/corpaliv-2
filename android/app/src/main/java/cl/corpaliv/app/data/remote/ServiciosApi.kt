package cl.corpaliv.app.data.remote

import cl.corpaliv.app.data.model.*
import retrofit2.http.*

internal interface ContenidosApi {
    @GET("programas") suspend fun programas(): List<Programa>
    @GET("programas/{id}") suspend fun detalle(@Path("id") id: Int): Programa
}
internal interface VinculacionApi {
    @POST("solicitudes") suspend fun crear(@Body data: CrearSolicitud): CodigoRespuesta
    @GET("seguimiento/{codigo}") suspend fun estado(@Path("codigo") codigo: String): EstadoRespuesta
    @GET("admin/sesion") suspend fun login(@Header("Authorization") auth: String): retrofit2.Response<Void>
    @GET("admin/solicitudes") suspend fun listar(@Header("Authorization") auth: String): List<Solicitud>
    @GET("admin/solicitudes/{id}") suspend fun detalle(@Header("Authorization") auth: String, @Path("id") id: Long): Solicitud
    @PUT("admin/solicitudes/{id}") suspend fun actualizar(@Header("Authorization") auth: String, @Path("id") id: Long, @Body data: Map<String, String>): Solicitud
    @DELETE("admin/solicitudes/{id}") suspend fun eliminar(@Header("Authorization") auth: String, @Path("id") id: Long)
}
internal interface FeriadosApi { @GET("feriados.json") suspend fun listar(): FeriadosRespuesta }
