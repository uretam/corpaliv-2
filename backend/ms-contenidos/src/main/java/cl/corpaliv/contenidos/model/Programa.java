package cl.corpaliv.contenidos.model;

import jakarta.persistence.*;

@Entity
public class Programa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String nombre;
    public String resumen;
    @Column(length = 2000) public String descripcion;
    public String categoria;
    public boolean activo = true;

    protected Programa() {}
    public Programa(String nombre, String resumen, String descripcion, String categoria) {
        this.nombre = nombre; this.resumen = resumen; this.descripcion = descripcion; this.categoria = categoria;
    }
}
