package cl.corpaliv.vinculacion.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Solicitud {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false, unique = true, length = 36) public String codigoSeguimiento;
    @Column(nullable = false) public String nombre;
    @Column(nullable = false) public String correo;
    public String telefono;
    @Column(nullable = false) public String modalidad;
    @Column(nullable = false, length = 2000) public String mensaje;
    public boolean aceptaPrivacidad;
    @Column(nullable = false) public String estado = "RECIBIDA";
    @Column(nullable = false) public Instant fechaCreacion = Instant.now();
    public Solicitud() {}
}
