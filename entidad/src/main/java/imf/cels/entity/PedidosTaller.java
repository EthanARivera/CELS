package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.Instant;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "pedidos_taller")
public class PedidosTaller {
    @Id
    @Column(name = "id_folio", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_folio", nullable = false)
    private Cotizacion cotizacion;

    @NotNull
    @ColumnDefault("'Por iniciar'")
    @Lob
    @Column(name = "estado_en_taller", nullable = false)
    private String estadoEnTaller;

    @Column(name = "prioridad")
    private String prioridad;
    public String getPrioridad() {return prioridad;}
    public void setPrioridad(String prioridad) {this.prioridad = prioridad;}

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_actualizacion")
    private Instant fechaActualizacion;

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEstadoEnTaller(String estadoEnTaller) {
        this.estadoEnTaller = estadoEnTaller;
    }

    public Instant getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Instant fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}