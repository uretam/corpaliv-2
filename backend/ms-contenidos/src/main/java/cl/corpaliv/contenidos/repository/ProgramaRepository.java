package cl.corpaliv.contenidos.repository;

import cl.corpaliv.contenidos.model.Programa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {
    List<Programa> findByActivoTrueOrderByIdAsc();
}
