package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
// imports para XML
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;

@Entity
@Table(name = "cotizacion_material")
@XmlRootElement(name = "material")
@XmlAccessorType(XmlAccessType.FIELD)
public class CotizacionMaterial {
    @EmbeddedId
    @XmlTransient
    private CotizacionMaterialId id = new CotizacionMaterialId();

    @MapsId("idFolio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_folio", nullable = false)
    @XmlTransient
    private Cotizacion idFolio;

    @MapsId("idMaterial")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_material", nullable = false)
    @XmlElement
    private Material idMaterial;

    @NotNull
    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    @XmlElement
    private BigDecimal cantidad;

    @NotNull
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    @XmlElement
    private BigDecimal subtotal;

    public CotizacionMaterial() {
        this.id = new CotizacionMaterialId();
    }

    public CotizacionMaterialId getId() {
        return id;
    }

    public void setId(CotizacionMaterialId id) {
        this.id = id;
    }

    public Cotizacion getIdFolio() {
        return idFolio;
    }

    public void setIdFolio(Cotizacion idFolio) {
        this.idFolio = idFolio;
    }

    public Material getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Material idMaterial) {
        this.idMaterial = idMaterial;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

}