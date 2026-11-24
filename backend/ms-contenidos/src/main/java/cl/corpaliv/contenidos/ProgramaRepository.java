package cl.corpaliv.contenidos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {
    List<Programa> findByActivoTrueOrderByIdAsc();
}
