package imf.cels.dto;

import java.math.BigDecimal;

public class CotizacionManoObraDTO {

    private Integer numResponsable;
    private BigDecimal costoHora;
    private BigDecimal cantidadHoras;
    private BigDecimal subtotal;

    public Integer getNumResponsable() { return numResponsable; }
    public BigDecimal getCostoHora() { return costoHora; }
    public BigDecimal getCantidadHoras() { return cantidadHoras; }
    public BigDecimal getSubtotal() { return subtotal; }

    public void setNumResponsable(Integer numResponsable) { this.numResponsable = numResponsable; }
    public void setCostoHora(BigDecimal costoHora) { this.costoHora = costoHora; }
    public void setCantidadHoras(BigDecimal cantidadHoras) { this.cantidadHoras = cantidadHoras; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
