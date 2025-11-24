package imf.cels.dto;

import java.math.BigDecimal;

public class CotizacionMaterialDTO {

    private Integer idMaterial;
    private BigDecimal cantidad;
    private BigDecimal subtotal;

    public Integer getIdMaterial() { return idMaterial; }
    public BigDecimal getCantidad() { return cantidad; }
    public BigDecimal getSubtotal() { return subtotal; }

    public void setIdMaterial(Integer idMaterial) { this.idMaterial = idMaterial; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
