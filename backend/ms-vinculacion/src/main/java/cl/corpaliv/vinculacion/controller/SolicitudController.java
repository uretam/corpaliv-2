package cl.corpaliv.vinculacion.controller;

import cl.corpaliv.vinculacion.service.SolicitudService;
import cl.corpaliv.vinculacion.dto.SolicitudDTO;
import cl.corpaliv.vinculacion.model.Solicitud;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolicitudController {
    private final SolicitudService service;
    public SolicitudController(SolicitudService service) { this.service = service; }
    @PostMapping("/solicitudes") @ResponseStatus(HttpStatus.CREATED)
    public SolicitudDTO.Codigo crear(@RequestBody SolicitudDTO.Crear data) { return service.crear(data); }
    @GetMapping("/seguimiento/{codigo}") public SolicitudDTO.Estado seguimiento(@PathVariable String codigo) { return service.estado(codigo); }
    @GetMapping("/admin/sesion") public String sesion() { return "OK"; }
    @GetMapping("/admin/solicitudes") public List<SolicitudDTO.Admin> listar() { return service.listar().stream().map(SolicitudDTO.Admin::desde).toList(); }
    @GetMapping("/admin/solicitudes/{id}") public SolicitudDTO.Admin detalle(@PathVariable Long id) { return SolicitudDTO.Admin.desde(service.detalle(id)); }
    @PutMapping("/admin/solicitudes/{id}") public SolicitudDTO.Admin actualizar(@PathVariable Long id, @RequestBody SolicitudDTO.Cambiar data) { return SolicitudDTO.Admin.desde(service.actualizar(id, data)); }
    @DeleteMapping("/admin/solicitudes/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
