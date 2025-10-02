package imf.cels.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CotizacionMaterialId implements Serializable {
    private static final long serialVersionUID = 7249116442828466512L;
    @NotNull
    @Column(name = "id_folio", nullable = false)
    private Integer idFolio;

    @NotNull
    @Column(name = "id_material", nullable = false)
    private Integer idMaterial;

    public Integer getIdFolio() {
        return idFolio;
    }

    public void setIdFolio(Integer idFolio) {
        this.idFolio = idFolio;
    }

    public Integer getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Integer idMaterial) {
        this.idMaterial = idMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CotizacionMaterialId entity = (CotizacionMaterialId) o;
        return Objects.equals(this.idMaterial, entity.idMaterial) &&
                Objects.equals(this.idFolio, entity.idFolio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMaterial, idFolio);
    }

}