package imf.cels.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CotizacionManoDeObraId implements Serializable {
    private static final long serialVersionUID = -2091168135919047692L;
    @NotNull
    @Column(name = "id_folio", nullable = false)
    private Integer idFolio;

    @NotNull
    @Column(name = "num_responsable", nullable = false)
    private Integer numResponsable;

    public Integer getIdFolio() {
        return idFolio;
    }

    public void setIdFolio(Integer idFolio) {
        this.idFolio = idFolio;
    }

    public Integer getNumResponsable() {
        return numResponsable;
    }

    public void setNumResponsable(Integer numResponsable) {
        this.numResponsable = numResponsable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CotizacionManoDeObraId entity = (CotizacionManoDeObraId) o;
        return Objects.equals(this.numResponsable, entity.numResponsable) &&
                Objects.equals(this.idFolio, entity.idFolio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numResponsable, idFolio);
    }

}