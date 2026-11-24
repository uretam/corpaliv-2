package cl.corpaliv.vinculacion;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolicitudController {
    private final SolicitudService service;
    public SolicitudController(SolicitudService service) { this.service = service; }
    @PostMapping("/solicitudes") @ResponseStatus(HttpStatus.CREATED)
    public SolicitudService.Codigo crear(@RequestBody SolicitudService.Crear data) { return service.crear(data); }
    @GetMapping("/seguimiento/{codigo}") public SolicitudService.Estado seguimiento(@PathVariable String codigo) { return service.estado(codigo); }
    @GetMapping("/admin/sesion") public String sesion() { return "OK"; }
    @GetMapping("/admin/solicitudes") public List<Solicitud> listar() { return service.listar(); }
    @GetMapping("/admin/solicitudes/{id}") public Solicitud detalle(@PathVariable Long id) { return service.detalle(id); }
    @PutMapping("/admin/solicitudes/{id}") public Solicitud actualizar(@PathVariable Long id, @RequestBody SolicitudService.Cambiar data) { return service.actualizar(id, data); }
    @DeleteMapping("/admin/solicitudes/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
