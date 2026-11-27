package cl.corpaliv.app.data.model

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
