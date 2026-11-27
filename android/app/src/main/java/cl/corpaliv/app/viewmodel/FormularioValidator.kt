package cl.corpaliv.app.viewmodel

object FormularioValidator {
    private val correo = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private val telefono = Regex("^[+0-9 ()-]{7,20}$")

    fun nombreValido(valor: String) = valor.length <= 120 && valor.trim().length >= 2
    fun correoValido(valor: String) = valor.length <= 254 && correo.matches(valor.trim())
    fun telefonoValido(valor: String) = valor.isBlank() || telefono.matches(valor.trim())
    fun mensajeValido(valor: String) = valor.length <= 2000 && valor.trim().length >= 10
}
