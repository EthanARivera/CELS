package ui;

import helper.LoginHelper;
import imf.cels.respaldos.ServicioRespaldo;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import imf.cels.entity.Usuario;
import java.io.IOException;
import java.io.Serializable;

@Named("loginUI")
@SessionScoped
public class LoginBeanUI implements Serializable{
    private LoginHelper loginHelper;
    private Usuario usuario;
    private Usuario datosFormulario;
    
    public LoginBeanUI() {
        loginHelper = new LoginHelper();
    }
    
    /**
     * Metodo postconstructor todo lo que este dentro de este metodo
     * sera la primero que haga cuando cargue la pagina
     */
    @PostConstruct
    public void init(){
        usuario= null;
        datosFormulario= new Usuario();
    }

    public String login() {
        try {
            String email = datosFormulario.getEmail() != null ? datosFormulario.getEmail().trim() : "";
            String password = datosFormulario.getPsswd() != null ? datosFormulario.getPsswd().trim() : "";

            if (email.isEmpty() || password.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Debe ingresar correo y contraseña", "Intente de nuevo"));
                return null;
            }

            Usuario us = loginHelper.Login(email, password);

            // En caso de que usuario o el id sea nulo, no redirige
            if (us == null || us.getId() == null) {
                usuario = new Usuario(); // limpia datos previos
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Usuario o contraseña incorrecta", "Intente de nuevo"));
                datosFormulario = new Usuario();
                return null;
            }

            // En caso de que pase, guarda usuario en sesión y redirige
            usuario = us;
            setUsuario(usuario);
            datosFormulario = new Usuario();
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        usuario=null;
        datosFormulario=new Usuario();
        return "/login.xhtml?faces-redirect=true";
    }

    /* Corroborar que la sesión esté abierta */
    public void verificarSesion() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(usuario==null || usuario.getEmail()==null || usuario.getId()==null){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()+"/login.xhtml");
        }
    }

    public void redirigirSiLogueado() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        String currentPage = context.getViewRoot().getViewId();
        if (currentPage != null && currentPage.contains("login.xhtml")) {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            usuario=null;
            datosFormulario=new Usuario();
            return;
        }

        if (usuario != null && usuario.getEmail() != null && usuario.getId() != null) {
            context.getExternalContext()
                    .redirect(context.getExternalContext().getRequestContextPath() + "/index.xhtml");
        }
    }

    public Integer obtenerIdUsuarioEnSesion() {
        return usuario.getId();
    }

    public String obtenerNombreUsuarioEnSesion() {
        if(usuario != null && usuario.getNombre() != null) {
            return usuario.getNombre();
        } else {
            return "Invitado";
        }
    }

    
    /* getters y setters */

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getDatosFormulario() {
        return datosFormulario;
    }

    public void setDatosFormulario(Usuario datosFormulario) {
        this.datosFormulario = datosFormulario;
    }

    //prueba de respaldo

    public void probarRespaldoManual() {
        try {
            ServicioRespaldo servicio = new ServicioRespaldo();
            servicio.ejecutarRespaldo();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Respaldo generado en C:/Respaldos"));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Fallo: " + e.getMessage()));
        }
    }
}