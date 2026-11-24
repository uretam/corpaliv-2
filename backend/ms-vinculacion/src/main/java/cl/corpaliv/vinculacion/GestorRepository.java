package cl.corpaliv.vinculacion;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GestorRepository extends JpaRepository<Gestor, Long> {
    Optional<Gestor> findByNombreUsuarioAndActivoTrue(String nombreUsuario);
}
