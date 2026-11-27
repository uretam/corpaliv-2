package cl.corpaliv.contenidos.config;

import cl.corpaliv.contenidos.repository.ProgramaRepository;
import cl.corpaliv.contenidos.model.Programa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProgramaSeed implements CommandLineRunner {
    private final ProgramaRepository repository;
    public ProgramaSeed(ProgramaRepository repository) { this.repository = repository; }
    public void run(String... args) {
        if (repository.count() != 0) return;
        repository.save(new Programa("Educación inclusiva", "Aprendizaje y autonomía para todas las personas.", "Propuesta académica demostrativa de actividades educativas inclusivas y apoyo al aprendizaje.", "Educación"));
        repository.save(new Programa("Formación para el trabajo", "Habilidades para el desarrollo laboral.", "Propuesta académica demostrativa de talleres de formación y acompañamiento sociolaboral.", "Formación"));
        repository.save(new Programa("Vinculación comunitaria", "Encuentros y redes de colaboración.", "Propuesta académica demostrativa para conectar a personas y organizaciones interesadas en colaborar.", "Comunidad"));
    }
}
