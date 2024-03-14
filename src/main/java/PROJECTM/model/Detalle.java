package PROJECTM.model;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@Table(name = "Detalle")
public class Detalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DetalleID")
    private Long detalleID;

    @ManyToOne
    @JoinColumn(name = "ModID")
    private Mod mod;

    @Column(name = "Descripcion")
    private String descripcion;

    public Detalle() {
    }

    public Detalle(Mod mod, String descripcion) {
        this.mod = mod;
        this.descripcion = descripcion;
    }

    public Long getDetalleID() {
        return detalleID;
    }

    public void setDetalleID(Long detalleID) {
        this.detalleID = detalleID;
    }

    public Mod getMod() {
        return mod;
    }

    public void setMod(Mod mod) {
        this.mod = mod;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Detalle{" +
                "detalleID=" + detalleID +
                ", mod=" + mod +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}

