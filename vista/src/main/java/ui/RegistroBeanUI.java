package ui;

import imf.cels.entity.Usuario;
import imf.cels.integration.ServiceFacadeLocator;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("registroUI")
@SessionScoped
public class RegistroBeanUI implements Serializable {

    private Usuario usuario = new Usuario();

    public void confirmarRegistro() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            ServiceFacadeLocator.getInstanceFacadeUsuario().registrarUsuario(usuario);

            String msg = String.format(
                    "Usuario registrado correctamente.%n" +  // new line after message
                            "ID: %d%n" +
                            "Nombre completo: %s %s %s%n" +
                            "RFC: %s%n" +
                            "Correo: %s",
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellidoPrim(),
                    (usuario.getApellidoSeg() != null ? usuario.getApellidoSeg() : ""),
                    usuario.getRfc(),
                    usuario.getEmail()
            );

            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro exitoso", msg));

        } catch (IllegalArgumentException ex) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validación", ex.getMessage()));
        } catch (Exception e) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error interno", "No se pudo registrar el usuario."));
        }
    }


    // Getter y Setter
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
