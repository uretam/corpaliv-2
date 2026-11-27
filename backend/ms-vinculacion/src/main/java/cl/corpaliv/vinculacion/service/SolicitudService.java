package cl.corpaliv.vinculacion.service;

import cl.corpaliv.vinculacion.repository.SolicitudRepository;
import cl.corpaliv.vinculacion.dto.SolicitudDTO.Crear;
import cl.corpaliv.vinculacion.dto.SolicitudDTO.Cambiar;
import cl.corpaliv.vinculacion.dto.SolicitudDTO.Codigo;
import cl.corpaliv.vinculacion.dto.SolicitudDTO.Estado;
import cl.corpaliv.vinculacion.model.Solicitud;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SolicitudService {
    private final SolicitudRepository repository;
    public SolicitudService(SolicitudRepository repository) { this.repository = repository; }

    public Codigo crear(Crear data) {
        if (data == null || data.nombre() == null || data.nombre().trim().length() < 2 || data.nombre().length() > 120
            || data.correo() == null || !data.correo().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || data.correo().length() > 254
            || data.mensaje() == null || data.mensaje().trim().length() < 10 || data.mensaje().length() > 2000
            || data.modalidad() == null || !List.of("Donación", "Voluntariado", "Alianza", "Capacitación").contains(data.modalidad())
            || !data.aceptaPrivacidad() || (data.telefono() != null && !data.telefono().isBlank() && !data.telefono().trim().matches("^[+0-9 ()-]{7,20}$")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos inválidos");
        Solicitud s = new Solicitud();
        s.codigoSeguimiento = UUID.randomUUID().toString();
        s.nombre = data.nombre().trim(); s.correo = data.correo().trim(); s.telefono = data.telefono();
        s.modalidad = data.modalidad(); s.mensaje = data.mensaje().trim(); s.aceptaPrivacidad = true;
        return new Codigo(repository.save(s).codigoSeguimiento);
    }
    public Estado estado(String codigo) {
        if (codigo == null || !codigo.matches("[0-9a-fA-F-]{36}")) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return repository.findByCodigoSeguimiento(codigo).map(s -> new Estado(s.estado))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public List<Solicitud> listar() { return repository.findAll(); }
    public Solicitud detalle(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); }
    public Solicitud actualizar(Long id, Cambiar data) {
        Solicitud s = detalle(id);
        if (data.nombre() != null) {
            if (data.nombre().trim().length() < 2 || data.nombre().length() > 120) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            s.nombre = data.nombre().trim();
        }
        if (data.estado() != null) {
            if (!List.of("RECIBIDA", "EN_REVISION", "CONTACTADO", "CONCRETADO", "DESCARTADO").contains(data.estado())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            s.estado = data.estado();
        }
        return repository.save(s);
    }
    public void eliminar(Long id) { if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND); repository.deleteById(id); }
}
