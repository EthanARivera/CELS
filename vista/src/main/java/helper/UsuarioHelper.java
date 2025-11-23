package helper;

import imf.cels.integration.ServiceFacadeLocator;
import imf.cels.entity.Usuario;
import java.util.List;

public class UsuarioHelper {

    public List<Usuario> obtenerUsuarios() {
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerUsuarios();
    }

    public List<Usuario> obtenerPorNombre(String nombre){
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerPorNombre(nombre);
    }

    public Usuario obtenerPorId(Integer id){
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerPorId(id);
    }


    // Activacion/Desactivacion
    public boolean cambiarEstadoUsuario(Integer idUsuario, boolean nuevoEstado) {
        return ServiceFacadeLocator.getInstanceFacadeUsuario().cambiarEstadoUsuario(idUsuario, nuevoEstado);
    }
}