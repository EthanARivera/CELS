package ui;

import helper.LoginHelper;
import imf.cels.entity.UsDatosSensible;
import imf.cels.entity.UsPsswd;
import imf.cels.entity.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;

@Named("loginUI")
@SessionScoped
public class LoginBeanUI implements Serializable{
    private LoginHelper loginHelper;
    private Usuario usuario;
    private Usuario datosFormulario;
    private UsDatosSensible usDatosSensible;
    private UsPsswd usPsswd;

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
        datosFormulario = new Usuario();
        datosFormulario.setUsDatosSensible(new UsDatosSensible());
        datosFormulario.setUsPsswd(new UsPsswd());

    }

    public String login() {
        try {
            String email = datosFormulario.getUsDatosSensible().getEmail() != null ? datosFormulario.getUsDatosSensible().getEmail().trim() : "";
            String password = datosFormulario.getUsPsswd().getPsswd() != null ? datosFormulario.getUsPsswd().getPsswd().trim() : "";

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
            datosFormulario.setUsDatosSensible(new UsDatosSensible());
            datosFormulario.setUsPsswd(new UsPsswd());
            System.out.println("Se inició sesión correctamente con el usuairo: " + usuario.getNombre());
            Integer tipoUsuario = usuario.getCodigoTipoUsuario();
            switch (tipoUsuario) {
                case 0:
                    FacesContext.getCurrentInstance().getExternalContext().redirect(
                            FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/indexGerente.xhtml");
                    break;
                case 1:
                    FacesContext.getCurrentInstance().getExternalContext().redirect(
                            FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/indexVendedor.xhtml");
                    break;
                case 2:
                    FacesContext.getCurrentInstance().getExternalContext().redirect(
                            FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/indexProductor.xhtml");
                    break;
                default:
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Tipo de usuario no asignado.", "Intente de con otro usuario, o asigne el tipo de usuario"));
            }

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
        if(usuario==null || usuario.getUsDatosSensible().getEmail()==null || usuario.getId()==null){
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

        if (usuario != null && usuario.getUsDatosSensible().getEmail() != null && usuario.getId() != null) {
            context.getExternalContext()
                    .redirect(context.getExternalContext().getRequestContextPath() + "/indexGerente.xhtml");
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

    public UsDatosSensible getUsDatosSensible() { return usDatosSensible; }

    public void setUsDatosSensible(UsDatosSensible usDatosSensible) { this.usDatosSensible = usDatosSensible; }

    public UsPsswd getUsPsswd() { return usPsswd; }

    public void setUsPsswd(UsPsswd usPsswd) { this.usPsswd = usPsswd; }
}