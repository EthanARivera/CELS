package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

//imports para los archivos XML
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@Entity
@Table(name = "cotizacion")
@XmlRootElement(name = "cotizacion")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_folio", nullable = false)
    @XmlElement
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @XmlTransient
    private Usuario idUsuario;

    @NotNull
    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "fecha", nullable = false)
    @XmlElement
    private LocalDate fecha;

    @NotNull
    @Column(name = "cliente", nullable = false)
    @XmlElement
    private String cliente;

    @Lob
    @Column(name = "descripcion", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String descripcion;

    @NotNull
    @Convert(converter = TipoProyectoConverter.class)
    @Column(name = "tipo_proyecto", nullable = false)
    private TipoProyecto tipoProyecto;

    @NotNull
    @Column(name = "precio_final", nullable = false, precision = 10, scale = 2)
    @XmlElement
    private BigDecimal precioFinal;

    @OneToMany(mappedBy = "idFolio", cascade = CascadeType.ALL, orphanRemoval = true)
    @XmlElementWrapper(name = "manosDeObra")
    @XmlElement(name = "manoDeObra")
    private Set<CotizacionManoDeObra> cotizacionManoDeObras = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idFolio", cascade = CascadeType.ALL, orphanRemoval = true)
    @XmlElementWrapper(name = "materiales")
    @XmlElement(name = "material")
    private Set<CotizacionMaterial> cotizacionMateriales = new LinkedHashSet<>();

    @NotNull
    @Column(name = "aprobado", nullable = false)
    private Boolean aprobado = false;

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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion =  descripcion; }

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

    public Set<CotizacionMaterial> getCotizacionMateriales() {
        return cotizacionMateriales;
    }

    public void setCotizacionMateriales(Set<CotizacionMaterial> cotizacionMateriales) {
        this.cotizacionMateriales = cotizacionMateriales;
    }

    // PBI-CO-US18 Aprobación de Cotización
    public Boolean getisCotizacionAprobado() { return aprobado; }
    public void setisCotizacionAprobado(Boolean aprobado) { this.aprobado = aprobado; }

}