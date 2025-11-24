package imf.cels.dto;

import java.math.BigDecimal;

public class MaterialDTO {

    private Integer id;
    private String nombre;
    private BigDecimal costo;

    public MaterialDTO(Integer id, String nombre, BigDecimal precio) {
        this.id = id;
        this.nombre = nombre;
        this.costo = precio;
    }

    public Integer getid() {
        return id;
    }

    public void setid(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getcosto() {
        return costo;
    }

    public void setcosto(BigDecimal costo) {
        this.costo = costo;
    }
}

