package cl.corpaliv.contenidos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ProgramaServiceTest {
    @Test void listaSoloActivosYDetalleValido() {
        ProgramaRepository repo = mock(ProgramaRepository.class);
        ProgramaService service = new ProgramaService(repo);
        Programa programa = new Programa("Educación", "Resumen", "Detalle", "Educación");
        when(repo.findByActivoTrueOrderByIdAsc()).thenReturn(List.of(programa));
        when(repo.findById(1L)).thenReturn(Optional.of(programa));
        assertEquals(1, service.listar().size());
        assertEquals("Educación", service.detalle(1L).nombre);
        programa.activo = false;
        assertEquals(HttpStatus.NOT_FOUND,
            assertThrows(ResponseStatusException.class, () -> service.detalle(1L)).getStatusCode());
    }
}
