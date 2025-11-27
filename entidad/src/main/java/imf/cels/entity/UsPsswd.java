package imf.cels.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "us_psswds")
public class UsPsswd {
    @Id
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Size(max = 64)
    @NotNull
    @Column(name = "psswd", nullable = false, length = 64)
    private String psswd;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ultima_modificacion")
    private Instant ultimaModificacion;

    // GETTERS / SETTERS

    public Integer getId() { return id; }
    public String getPsswd() { return psswd; }
    public Usuario getUsuario() { return usuario; }
    public Instant getUltimaModificacion() { return ultimaModificacion; }

    public void setId(Integer id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setPsswd(String psswd) { this.psswd = psswd; }
    public void setUltimaModificacion(Instant ultimaModificacion) { this.ultimaModificacion = ultimaModificacion; }
}