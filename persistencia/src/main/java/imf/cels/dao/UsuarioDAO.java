package imf.cels.dao;

import jakarta.persistence.EntityManager;
import imf.cels.persistence.AbstractDAO;
import imf.cels.entity.Usuario;

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





    // Modificacion
    // Update/actualizar email and password
    public void actualizarCorreoYContrasena(Integer id, String correo, String password) {
        execute(em -> {
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                usuario.setEmail(correo);
                usuario.setPsswd(password);
                em.merge(usuario);
            }
            return null;
        });
    }

    // Vrificar si email ya existe
    public boolean existeCorreo(String correo) {
        List<Usuario> result = entityManager.createQuery(
                        "SELECT u FROM Usuario u WHERE u.email = :correo", Usuario.class)
                .setParameter("correo", correo)
                .getResultList();
        return !result.isEmpty();
    }
}
