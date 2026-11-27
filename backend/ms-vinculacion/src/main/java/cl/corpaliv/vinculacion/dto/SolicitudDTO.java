package cl.corpaliv.vinculacion.dto;

import cl.corpaliv.vinculacion.model.Solicitud;
import java.time.Instant;

public class SolicitudDTO {
    public record Crear(String nombre, String correo, String telefono, String modalidad, String mensaje, boolean aceptaPrivacidad) {}
    public record Cambiar(String nombre, String estado) {}
    public record Codigo(String codigoSeguimiento) {}
    public record Estado(String estado) {}
    public record Admin(Long id, String codigoSeguimiento, String nombre, String correo, String telefono,
                        String modalidad, String mensaje, String estado, Instant fechaCreacion) {
        public static Admin desde(Solicitud solicitud) {
            return new Admin(solicitud.id, solicitud.codigoSeguimiento, solicitud.nombre, solicitud.correo,
                solicitud.telefono, solicitud.modalidad, solicitud.mensaje, solicitud.estado, solicitud.fechaCreacion);
        }
    }
}
