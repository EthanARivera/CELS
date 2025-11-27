package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "datos_encuesta")
public class DatosEncuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encuesta", nullable = false)
    private Integer id;

    @Column(name = "q1")
    private Boolean q1;

    @Size(max = 50)
    @Column(name = "q2", length = 50)
    private String q2;

    @Column(name = "q3")
    private Integer q3;

}