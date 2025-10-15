/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import helper.LoginHelper;
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
    
    public LoginBeanUI() {
        loginHelper = new LoginHelper();
    }
    
    /**
     * Metodo postconstructor todo lo que este dentro de este metodo
     * sera la primero que haga cuando cargue la pagina
     */
    @PostConstruct
    public void init(){
        usuario= new Usuario();
    }

    public String login() {
        String appURL = "/index.xhtml";

        Usuario us = loginHelper.Login(usuario.getEmail(), usuario.getPsswd());
        if (us != null && us.getId() != null) {
            usuario = us;
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + appURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null; // ya hiciste redirect manual
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecta:", "Intente de nuevo"));
            return null; // quédate en la misma página para mostrar mensaje
        }
    }

    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        usuario=null;
        return "/login.xhtml?faces-redirect=true";
    }

    /* Corroborar que la sesión esté abierta */
    public void verificarSesion() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(usuario==null || usuario.getEmail()==null){
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()+"/login.xhtml");
        }
    }

    public void redirigirSiLogueado() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (usuario != null && usuario.getEmail() != null) {
            context.getExternalContext()
                    .redirect(context.getExternalContext().getRequestContextPath() + "/index.xhtml");
        }
    }
    
    /* getters y setters */

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}