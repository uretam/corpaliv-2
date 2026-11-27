package cl.corpaliv.vinculacion.repository;

import cl.corpaliv.vinculacion.model.Solicitud;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    Optional<Solicitud> findByCodigoSeguimiento(String codigo);
}
