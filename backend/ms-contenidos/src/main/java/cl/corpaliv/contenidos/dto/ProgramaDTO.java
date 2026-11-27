package cl.corpaliv.contenidos.dto;

import cl.corpaliv.contenidos.model.Programa;

public record ProgramaDTO(Long id, String nombre, String resumen, String descripcion, String categoria) {
    public static ProgramaDTO desde(Programa programa) {
        return new ProgramaDTO(programa.id, programa.nombre, programa.resumen, programa.descripcion, programa.categoria);
    }
}
