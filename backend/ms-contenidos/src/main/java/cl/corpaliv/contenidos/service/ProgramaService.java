package cl.corpaliv.contenidos.service;

import cl.corpaliv.contenidos.repository.ProgramaRepository;
import cl.corpaliv.contenidos.model.Programa;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProgramaService {
    private final ProgramaRepository repository;
    public ProgramaService(ProgramaRepository repository) { this.repository = repository; }
    public List<Programa> listar() { return repository.findByActivoTrueOrderByIdAsc(); }
    public Programa detalle(Long id) { return repository.findById(id).filter(p -> p.activo)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); }
}
