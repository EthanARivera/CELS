package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "cotizacion")
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_folio", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proyecto", nullable = false)
    private TipoProyecto tipoProyecto;

    @NotNull
    @Column(name = "precio_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioFinal;

    @OneToMany(mappedBy = "idFolio")
    private Set<CotizacionManoDeObra> cotizacionManoDeObras = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idFolio")
    private Set<CotizacionMaterial> cotizacionMaterials = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoProyecto getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(TipoProyecto tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(BigDecimal precioFinal) {
        this.precioFinal = precioFinal;
    }

    public Set<CotizacionManoDeObra> getCotizacionManoDeObras() {
        return cotizacionManoDeObras;
    }

    public void setCotizacionManoDeObras(Set<CotizacionManoDeObra> cotizacionManoDeObras) {
        this.cotizacionManoDeObras = cotizacionManoDeObras;
    }

    public Set<CotizacionMaterial> getCotizacionMaterials() {
        return cotizacionMaterials;
    }

    public void setCotizacionMaterials(Set<CotizacionMaterial> cotizacionMaterials) {
        this.cotizacionMaterials = cotizacionMaterials;
    }

}