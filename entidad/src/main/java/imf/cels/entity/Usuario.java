package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Size(max = 50)
    @NotNull
    @Column(name = "apellido_prim", nullable = false, length = 50)
    private String apellidoPrim;

    @Size(max = 50)
    @Column(name = "apellido_seg", length = 50)
    private String apellidoSeg;

    @Size(max = 13)
    @NotNull
    @Column(name = "rfc", nullable = false, length = 13)
    private String rfc;

    @Size(max = 254)
    @NotNull
    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Size(max = 64)
    @NotNull
    @Column(name = "psswd", nullable = false, length = 64)
    private String psswd;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "estado", nullable = false)
    //private Boolean estado = false;
    private Boolean estado = true;

    @OneToMany(mappedBy = "idUsuario")
    private Set<Cotizacion> cotizacions = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPrim() {
        return apellidoPrim;
    }

    public void setApellidoPrim(String apellidoPrim) {
        this.apellidoPrim = apellidoPrim;
    }

    public String getApellidoSeg() {
        return apellidoSeg;
    }

    public void setApellidoSeg(String apellidoSeg) {
        this.apellidoSeg = apellidoSeg;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPsswd() {
        return psswd;
    }

    public void setPsswd(String psswd) {
        this.psswd = psswd;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Set<Cotizacion> getCotizacions() {
        return cotizacions;
    }

    public void setCotizacions(Set<Cotizacion> cotizacions) {
        this.cotizacions = cotizacions;
    }
}