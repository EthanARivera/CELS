package ui;

import imf.cels.entity.UsDatosSensible;
import imf.cels.entity.UsPsswd;
import imf.cels.entity.Usuario;
import imf.cels.integration.ServiceFacadeLocator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("registroUI")
@ViewScoped
public class RegistroBeanUI implements Serializable {

    private Usuario usuario;


    @PostConstruct
    public void init() {
        usuario = new Usuario();
        usuario.setUsDatosSensible(new UsDatosSensible());
        usuario.setUsPsswd(new UsPsswd());
    }

    //feedback
    public void confirmarRegistro() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            ServiceFacadeLocator.getInstanceFacadeUsuario().registrarUsuario(usuario);

            /*String msg = String.format(
                    "Usuario registrado correctamente.%n" +  // new line after message
                            "ID: %03d%n" +
                            "Nombre completo: %s %s %s%n" +
                            "RFC: %s%n" +
                            "Correo: %s",
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellidoPrim(),
                    (usuario.getApellidoSeg() != null ? usuario.getApellidoSeg() : ""),
                    usuario.getUsDatosSensible().getRfc(),
                    usuario.getUsDatosSensible().getEmail()
            );*/

            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro exitoso",
                            "El usuario ha sido registrado correctamente."));

            // Clean form after saving
            usuario = new Usuario();
            usuario.setUsDatosSensible(new UsDatosSensible());
            usuario.setUsPsswd(new UsPsswd());

        } catch (IllegalArgumentException ex) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validación", ex.getMessage()));
        } catch (Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error interno", e.getMessage()));
        }
    }

    // Getter y Setter
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
