package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "cotizacion_material")
public class CotizacionMaterial {
    @EmbeddedId
    private CotizacionMaterialId id = new CotizacionMaterialId();

    @MapsId("idFolio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_folio", nullable = false)
    private Cotizacion idFolio;

    @MapsId("idMaterial")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_material", nullable = false)
    private Material idMaterial;

    @NotNull
    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @NotNull
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
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