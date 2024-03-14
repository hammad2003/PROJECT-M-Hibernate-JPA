package PROJECTM.model;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@Table(name = "Juego")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JuegoID")
    private Long juegoID;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Descripcion")
    private String descripcion;

    public Juego() {
    }

    public Juego(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getJuegoID() {
        return juegoID;
    }

    public void setJuegoID(Long juegoID) {
        this.juegoID = juegoID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Juego{" +
                "juegoID=" + juegoID +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
