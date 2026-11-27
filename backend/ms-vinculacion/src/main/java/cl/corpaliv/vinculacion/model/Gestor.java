package cl.corpaliv.vinculacion.model;

import jakarta.persistence.*;

@Entity
public class Gestor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false, unique = true) public String nombreUsuario;
    @Column(nullable = false) public String passwordHash;
    public boolean activo = true;
    protected Gestor() {}
    public Gestor(String nombreUsuario, String passwordHash) {
        this.nombreUsuario = nombreUsuario; this.passwordHash = passwordHash;
    }
}
