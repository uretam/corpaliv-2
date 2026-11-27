package cl.corpaliv.app

import cl.corpaliv.app.data.model.*
import cl.corpaliv.app.data.repository.*
import cl.corpaliv.app.viewmodel.*

import org.junit.Assert.*
import org.junit.Test

class FormularioValidatorTest {
    @Test fun aceptaFormularioValido() {
        assertTrue(FormularioValidator.nombreValido("Ana Pérez"))
        assertTrue(FormularioValidator.correoValido("ana@example.com"))
        assertTrue(FormularioValidator.telefonoValido("+56 9 1234 5678"))
        assertTrue(FormularioValidator.telefonoValido(""))
        assertTrue(FormularioValidator.mensajeValido("Quiero colaborar"))
    }

    @Test fun rechazaCamposInvalidosYLimites() {
        assertFalse(FormularioValidator.nombreValido(" "))
        assertFalse(FormularioValidator.nombreValido("x".repeat(121)))
        assertFalse(FormularioValidator.correoValido("sin-arroba"))
        assertFalse(FormularioValidator.correoValido("a".repeat(255) + "@x.cl"))
        assertFalse(FormularioValidator.telefonoValido("abc12345"))
        assertFalse(FormularioValidator.telefonoValido("123456"))
        assertFalse(FormularioValidator.mensajeValido("corto"))
        assertFalse(FormularioValidator.mensajeValido("x".repeat(2001)))
    }
}
