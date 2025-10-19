package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "cotizacion_mano_de_obra")
public class CotizacionManoDeObra {
    @EmbeddedId
    private CotizacionManoDeObraId id = new CotizacionManoDeObraId();

    @MapsId("idFolio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_folio", nullable = false)
    private Cotizacion idFolio;

    @NotNull
    @Column(name = "costo_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoHora;

    @NotNull
    @Column(name = "cantidad_horas", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadHoras;

    @NotNull
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public CotizacionManoDeObraId getId() {
        return id;
    }

    public void setId(CotizacionManoDeObraId id) {
        this.id = id;
    }

    public Cotizacion getIdFolio() {
        return idFolio;
    }

    public void setIdFolio(Cotizacion idFolio) {
        this.idFolio = idFolio;
    }

    public BigDecimal getCostoHora() {
        return costoHora;
    }

    public void setCostoHora(BigDecimal costoHora) {
        this.costoHora = costoHora;
    }

    public BigDecimal getCantidadHoras() {
        return cantidadHoras;
    }

    public void setCantidadHoras(BigDecimal cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

}