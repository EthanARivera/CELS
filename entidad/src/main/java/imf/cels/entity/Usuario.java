package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
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

    @NotNull
    @Column(name = "codigo_tipo_usuario", nullable = false)
    private Integer codigoTipoUsuario;

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

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion_usuario")
    private Instant fechaCreacionUsuario;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @OneToMany
    @JoinColumn(name = "id_usuario")
    private Set<Cotizacion> cotizacions = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private UsDatosSensible usDatosSensible;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private UsPsswd usPsswd;


    // GETTERS / SETTERS

    public Integer getId() { return id; }
    public Integer getCodigoTipoUsuario() { return codigoTipoUsuario; }
    public String getNombre() { return nombre; }
    public String getApellidoPrim() { return apellidoPrim; }
    public String getApellidoSeg() { return apellidoSeg; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public Instant getFechaCreacionUsuario() { return fechaCreacionUsuario; }
    public Boolean getEstado() { return estado; }
    public Set<Cotizacion> getCotizacions() { return cotizacions; }
    public UsDatosSensible getUsDatosSensible() { return usDatosSensible; }
    public UsPsswd getUsPsswd() { return usPsswd; }

    public void setId(Integer id) { this.id = id; }
    public void setCodigoTipoUsuario(Integer codigoTipoUsuario) { this.codigoTipoUsuario = codigoTipoUsuario; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidoPrim(String apellidoPrim) { this.apellidoPrim = apellidoPrim; }
    public void setApellidoSeg(String apellidoSeg) { this.apellidoSeg = apellidoSeg; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento;  }
    public void setFechaCreacionUsuario(Instant fechaCreacionUsuario) { this.fechaCreacionUsuario = fechaCreacionUsuario; }
    public void setCotizacions(Set<Cotizacion> cotizacions) { this.cotizacions = cotizacions; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public void setUsDatosSensible(UsDatosSensible usDatosSensible) { this.usDatosSensible = usDatosSensible; }
    public void setUsPsswd(UsPsswd usPsswd) { this.usPsswd = usPsswd; }
}