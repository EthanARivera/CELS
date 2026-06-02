package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "datos_encuesta")
public class DatosEncuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encuesta", nullable = false)
    private Integer idEncuesta;

    @Column(name = "q1", nullable = false)
    private Boolean q1;// ¿El tiempo de entrega fue adecuado?

    @Column(name = "q2", nullable = false)
    private Integer q2; // Atención al vendedor (1-5)

    @Column(name = "q3", nullable = false)
    private Integer q3; // Calidad del producto (1-5)

    @Column(name = "q4", nullable = false)
    private Integer q4; // Probabilidad de recomendar (1-5)

    @Column(name = "q5", length = 254)
    private String q5; // Retroalimentación libre

    public DatosEncuesta() {}

    // Getters y Setters
    public Integer getIdEncuesta() { return idEncuesta; }
    public void setIdEncuesta(Integer idEncuesta) { this.idEncuesta = idEncuesta; }

    public Boolean getQ1() { return q1; }
    public void setQ1(Boolean q1) { this.q1 = q1; }

    public Integer getQ2() { return q2; }
    public void setQ2(Integer q2) { this.q2 = q2; }

    public Integer getQ3() { return q3; }
    public void setQ3(Integer q3) { this.q3 = q3; }

    public Integer getQ4() { return q4; }
    public void setQ4(Integer q4) { this.q4 = q4; }

    public String getQ5() { return q5; }
    public void setQ5(String q5) { this.q5 = q5; }

}