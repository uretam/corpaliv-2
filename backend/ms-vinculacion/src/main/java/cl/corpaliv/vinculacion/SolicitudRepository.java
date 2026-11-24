package cl.corpaliv.vinculacion;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    Optional<Solicitud> findByCodigoSeguimiento(String codigo);
}
