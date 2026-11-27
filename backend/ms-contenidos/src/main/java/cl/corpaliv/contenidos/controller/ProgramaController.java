package cl.corpaliv.contenidos.controller;

import cl.corpaliv.contenidos.model.Programa;
import cl.corpaliv.contenidos.service.ProgramaService;
import cl.corpaliv.contenidos.dto.ProgramaDTO;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/programas")
public class ProgramaController {
    private final ProgramaService service;
    public ProgramaController(ProgramaService service) { this.service = service; }
    @GetMapping public List<ProgramaDTO> listar() { return service.listar().stream().map(ProgramaDTO::desde).toList(); }
    @GetMapping("/{id}") public ProgramaDTO detalle(@PathVariable Long id) { return ProgramaDTO.desde(service.detalle(id)); }
}
