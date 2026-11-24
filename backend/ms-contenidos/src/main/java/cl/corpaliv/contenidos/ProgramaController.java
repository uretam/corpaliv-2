package cl.corpaliv.contenidos;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/programas")
public class ProgramaController {
    private final ProgramaService service;
    public ProgramaController(ProgramaService service) { this.service = service; }
    @GetMapping public List<Programa> listar() { return service.listar(); }
    @GetMapping("/{id}") public Programa detalle(@PathVariable Long id) { return service.detalle(id); }
}
