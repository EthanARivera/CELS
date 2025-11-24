package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//Importa para el XML
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "material")
@XmlRootElement(name = "infoMaterial")
@XmlAccessorType(XmlAccessType.FIELD)
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material", nullable = false)
    @XmlElement
    private Integer id;

    @Size(max = 128)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 128)
    @XmlElement
    private String nombre;

    @Size(max = 32)
    @Column(name = "tipo_material", length = 32)
    @XmlElement
    private String tipoMaterial;

    @NotNull
    @Column(name = "costo", nullable = false, precision = 10, scale = 2)
    @XmlElement
    private BigDecimal costo;

    @NotNull //No puede ser nulo
    @Column(name = "tipo_unidad", nullable = false, columnDefinition = "ENUM('mts','kgs','lts','pzas','m²','otro')")
    @Convert(converter = TipoUnidadConverter.class)  // Usa el convertidor para mapear entre el enum y el texto almacenado en la base.
    @XmlElement
    private TipoUnidad tipoUnidad;


    @OneToMany(mappedBy = "idMaterial")
    @XmlTransient
    private Set<CotizacionMaterial> cotizacionMateriales = new LinkedHashSet<>();

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

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public TipoUnidad getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(TipoUnidad tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public Set<CotizacionMaterial> getCotizacionMaterials() {
        return cotizacionMateriales;
    }

    public void setCotizacionMaterials(Set<CotizacionMaterial> cotizacionMaterials) {
        this.cotizacionMateriales = cotizacionMaterials;
    }
}