package PROJECTM.model;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@Table(name = "Mod")
public class Mod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ModID")
    private Long modID;

    @ManyToOne
    @JoinColumn(name = "JuegoID")
    private Juego juego;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Autor", nullable = false)
    private String autor;

    @Column(name = "Descripcion")
    private String descripcion;

    public Mod() {
    }

    public Mod(Juego juego, String nombre, String autor, String descripcion) {
        this.juego = juego;
        this.nombre = nombre;
        this.autor = autor;
        this.descripcion = descripcion;
    }

    public Long getModID() {
        return modID;
    }

    public void setModID(Long modID) {
        this.modID = modID;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Mod{" +
                "modID=" + modID +
                ", juego=" + juego +
                ", nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
