package imf.cels.dao;

import imf.cels.entity.Usuario;
import imf.cels.entity.UsPsswd;
import imf.cels.entity.UsDatosSensible;
import imf.cels.integration.ServiceLocator;
import jakarta.persistence.EntityManager;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.NoResultException;

import java.util.List;

public class UsuarioDAO extends AbstractDAO<Usuario> {
    private final EntityManager entityManager;


    public UsuarioDAO(EntityManager entityManager) {
        super(Usuario.class);
        this.entityManager = entityManager;
    }

    public List<Usuario> obtenerTodos(){
        return entityManager
                .createQuery("SELECT a FROM Usuario a", Usuario.class)
                .getResultList();
    }

    public Usuario findById(int id) {
        return entityManager.find(Usuario.class, id);
    }

    public List<Usuario> findByName(String name) {
        try {
            return entityManager.createQuery(
                            "SELECT u FROM Usuario u WHERE u.nombre = :nombre", Usuario.class)
                    .setParameter("nombre", name)
                    .getResultList();
        } catch (jakarta.persistence.NoResultException e) {
            return null; // No se encontró el usuario
        }
    }


    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void saveDatosSensible(UsDatosSensible uds) {
        entityManager.persist(uds);
    }

    public void savePsswd(UsPsswd up) {
        entityManager.persist(up);
    }

    // Modificacion
    public void actualizarCorreoYContrasena(Integer id, String email, String psswd) {
        System.out.println("Entrada al DAO actualizarCorreYContrasena con los datos: \nid: "+id+"\nemail: "+email+"\npsswd: "+psswd);
        Usuario usuario = entityManager.find(Usuario.class, id);

        if (usuario == null) return;

        usuario.getUsDatosSensible().setEmail(email);
        usuario.getUsPsswd().setPsswd(psswd);

        ServiceLocator.getInstanceUsuarioDAO().saveOrUpdate(usuario);
    }



    // Vrificar si email ya existe
    public boolean existeCorreo(String correo) {
        List<UsDatosSensible> result = entityManager.createQuery(
                        "SELECT u FROM UsDatosSensible u WHERE u.email = :correo", UsDatosSensible.class)
                .setParameter("correo", correo)
                .getResultList();
        return !result.isEmpty();
    }



    // Activacion/Desactivacion
    public void cambiarEstado(Integer idUsuario, boolean nuevoEstado) {
        execute ( em -> {
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                usuario.setEstado(nuevoEstado);
                em.merge(usuario);
            }
            return null;
        });
    }

    //Obtención del correo del usuario
    public String obtenerCorreo(Integer idUsuario) {
        return execute(em -> {
            UsDatosSensible usuario = em.find(UsDatosSensible.class, idUsuario);
            return usuario != null ? usuario.getEmail() : null;
        });
    }

    public String obtenerPsswd(Integer idUsuario) {
        return execute( em -> {
            UsPsswd usPsswd = em.find(UsPsswd.class, idUsuario);
            return usPsswd != null ? usPsswd.getPsswd() : null;
        });
    }

    public Integer obtenerIdUsuario(Integer idUsuario) {
        return execute(em -> {
            Usuario usuario = em.find(Usuario.class, idUsuario);
            return usuario.getId();
        });
    }

    public String obtenerNombre(Integer idUsuario) {
        return execute(em -> {
            Usuario usuario = em.find(Usuario.class, idUsuario);
            return usuario.getNombre();
        });
    }
}
