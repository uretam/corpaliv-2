package cl.corpaliv.vinculacion.repository;

import cl.corpaliv.vinculacion.model.Gestor;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GestorRepository extends JpaRepository<Gestor, Long> {
    Optional<Gestor> findByNombreUsuarioAndActivoTrue(String nombreUsuario);
}
