package cl.corpaliv.app

data class Programa(val id: Int, val nombre: String, val resumen: String, val descripcion: String, val categoria: String)
data class SolicitudDemo(val codigo: String, val nombre: String, val correo: String, val telefono: String, val mensaje: String, val modalidad: String, val estado: String)

/** Datos temporales explícitos. Se reemplaza por Retrofit en Sprint 2. */
class DemoRepository {
    val programas = listOf(
        Programa(1, "Educación inclusiva", "Aprendizaje y autonomía para todas las personas.", "Propuesta demostrativa de actividades educativas inclusivas y apoyo al aprendizaje.", "Educación"),
        Programa(2, "Formación para el trabajo", "Habilidades para el desarrollo laboral.", "Propuesta demostrativa de talleres de formación y acompañamiento sociolaboral.", "Formación"),
        Programa(3, "Vinculación comunitaria", "Encuentros y redes de colaboración.", "Propuesta demostrativa para conectar a personas y organizaciones interesadas en colaborar.", "Comunidad")
    )
    private val solicitudes = mutableListOf(SolicitudDemo("DEMO-8F3K9D", "Persona ficticia", "demo@example.com", "", "Mensaje de ejemplo para la defensa.", "Voluntariado", "EN_REVISION"))
    fun listar(): List<SolicitudDemo> = solicitudes.toList()
    fun crear(nombre: String, correo: String, telefono: String, mensaje: String, modalidad: String): String {
        val codigo = "DEMO-%06X".format((100000..0xFFFFFF).random())
        solicitudes.add(0, SolicitudDemo(codigo, nombre, correo, telefono, mensaje, modalidad, "RECIBIDA"))
        return codigo
    }
    fun estado(codigo: String): String? = solicitudes.find { it.codigo.equals(codigo.trim(), true) }?.estado
    fun cambiarEstado(codigo: String, estado: String) {
        val i = solicitudes.indexOfFirst { it.codigo == codigo }
        if (i >= 0) solicitudes[i] = solicitudes[i].copy(estado = estado)
    }
    fun cambiarNombre(codigo: String, nombre: String) {
        val i = solicitudes.indexOfFirst { it.codigo == codigo }
        if (i >= 0) solicitudes[i] = solicitudes[i].copy(nombre = nombre)
    }
    fun eliminar(codigo: String) { solicitudes.removeAll { it.codigo == codigo } }
}
