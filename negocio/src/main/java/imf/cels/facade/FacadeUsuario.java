package imf.cels.facade;

import imf.cels.delegate.DelegateUsuario;
import imf.cels.entity.Usuario;
import imf.cels.integration.ServiceLocator;

import java.util.List;

public class FacadeUsuario {

    private final DelegateUsuario delegateUsuario;

    public FacadeUsuario() {
        this.delegateUsuario = new DelegateUsuario();
    }

    public Usuario login(String password, String correo){
        return delegateUsuario.login(password, correo);
    }

    public void saveUsario(Usuario usuario){
        if (usuario.getPsswd() == null || usuario.getPsswd().isEmpty() || usuario.getPsswd().equals("1")) {
            throw new IllegalArgumentException("Contraseña inválida: no se puede guardar valor vacío o '1'");
        }
        ServiceLocator.getInstanceUsuarioDAO().save(usuario);
    }

    //Registro de Usuario -
    public void registrarUsuario(Usuario usuario){ delegateUsuario.registrarUsuario(usuario);
    }

    public List<Usuario> obtenerUsuarios() {
        return delegateUsuario.obtenerUsuarios();
    }

    public List<Usuario> obtenerPorNombre(String nombre){
        return delegateUsuario.obtenerPorNombre(nombre);
    }

    public Usuario obtenerPorId(Integer id){
        return delegateUsuario.obtenerPorId(id);
    }

}