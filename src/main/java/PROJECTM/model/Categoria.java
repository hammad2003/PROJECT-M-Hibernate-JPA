package PROJECTM.model;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@Table(name = "Categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoriaID")
    private Long categoriaID;

    @ManyToOne
    @JoinColumn(name = "ModID")
    private Mod mod;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    public Categoria() {
    }

    public Categoria(Mod mod, String nombre) {
        this.mod = mod;
        this.nombre = nombre;
    }

    public Long getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Long categoriaID) {
        this.categoriaID = categoriaID;
    }

    public Mod getMod() {
        return mod;
    }

    public void setMod(Mod mod) {
        this.mod = mod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "categoriaID=" + categoriaID +
                ", mod=" + mod +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
