package imf.cels.facade;

import imf.cels.delegate.DelegateUsuario;
import imf.cels.entity.Usuario;

public class FacadeUsuario {

    private final DelegateUsuario delegateUsuario;

    public FacadeUsuario() {
        this.delegateUsuario = new DelegateUsuario();
    }

    public Usuario login(String password, String correo){
        return delegateUsuario.login(password, correo);
    }

    public void saveUsario(Usuario usuario){
        delegateUsuario.saveUsario(usuario);
    }

    //Registro de Usuario
    public void registrarUsuario(Usuario usuario){ delegateUsuario.registrarUsuario(usuario);
    }

}