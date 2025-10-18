package imf.cels.facade;

import imf.cels.delegate.DelegateUsuario;
import imf.cels.entity.Usuario;

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
        delegateUsuario.saveUsario(usuario);
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


    // Activacion/Desactivacion
    public boolean cambiarEstadoUsuario(Integer idUsuario, boolean nuevoEstado) {
        try {
            delegateUsuario.cambiarEstadoUsuario(idUsuario, nuevoEstado);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}