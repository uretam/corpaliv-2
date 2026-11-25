package cl.corpaliv.vinculacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class SolicitudServiceTest {
    private SolicitudRepository repository;
    private SolicitudService service;
    @BeforeEach void preparar() {
        repository = mock(SolicitudRepository.class);
        service = new SolicitudService(repository);
        when(repository.save(any(Solicitud.class))).thenAnswer(a -> a.getArgument(0));
    }

    private SolicitudService.Crear valida() {
        return new SolicitudService.Crear(" Ana ", "ana@example.com", "+56 9 1234 5678", "Voluntariado", "Quiero colaborar", true);
    }

    @Test void creaCodigoAleatorioYEstadoInicial() {
        var codigo1 = service.crear(valida()).codigoSeguimiento();
        var codigo2 = service.crear(valida()).codigoSeguimiento();
        assertNotEquals(codigo1, codigo2);
        assertEquals(36, codigo1.length());
        var captor = org.mockito.ArgumentCaptor.forClass(Solicitud.class);
        verify(repository, times(2)).save(captor.capture());
        assertEquals("RECIBIDA", captor.getValue().estado);
        assertEquals("Ana", captor.getValue().nombre);
    }

    @Test void rechazaFormularioInvalidoSinGuardar() {
        var casos = java.util.List.of(
            new SolicitudService.Crear("A", "ana@example.com", "", "Voluntariado", "Quiero colaborar", true),
            new SolicitudService.Crear("Ana", "sin-correo", "", "Voluntariado", "Quiero colaborar", true),
            new SolicitudService.Crear("Ana", "ana@example.com", "abc12345", "Voluntariado", "Quiero colaborar", true),
            new SolicitudService.Crear("Ana", "ana@example.com", "", "Otra", "Quiero colaborar", true),
            new SolicitudService.Crear("Ana", "ana@example.com", "", "Voluntariado", "corto", true),
            new SolicitudService.Crear("Ana", "ana@example.com", "", "Voluntariado", "Quiero colaborar", false));
        for (var caso : casos) assertEquals(HttpStatus.BAD_REQUEST,
            assertThrows(ResponseStatusException.class, () -> service.crear(caso)).getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test void seguimientoSoloDevuelveEstado() {
        Solicitud s = new Solicitud(); s.estado = "CONTACTADO";
        when(repository.findByCodigoSeguimiento("00000000-0000-0000-0000-000000000000")).thenReturn(Optional.of(s));
        assertEquals("CONTACTADO", service.estado("00000000-0000-0000-0000-000000000000").estado());
        assertEquals(HttpStatus.NOT_FOUND,
            assertThrows(ResponseStatusException.class, () -> service.estado("invalido")).getStatusCode());
    }

    @Test void actualizaEstadoPermitidoYRechazaDesconocido() {
        Solicitud s = new Solicitud(); s.nombre = "Ana";
        when(repository.findById(7L)).thenReturn(Optional.of(s));
        assertEquals("CONTACTADO", service.actualizar(7L, new SolicitudService.Cambiar(null, "CONTACTADO")).estado);
        assertEquals("Berta", service.actualizar(7L, new SolicitudService.Cambiar(" Berta ", null)).nombre);
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
            () -> service.actualizar(7L, new SolicitudService.Cambiar(null, "DESCONOCIDO"))).getStatusCode());
    }

    @Test void eliminaSoloSiExiste() {
        when(repository.existsById(7L)).thenReturn(true);
        service.eliminar(7L);
        verify(repository).deleteById(7L);
        assertEquals(HttpStatus.NOT_FOUND,
            assertThrows(ResponseStatusException.class, () -> service.eliminar(8L)).getStatusCode());
    }
}
