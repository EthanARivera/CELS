package imf.cels.dao;

import jakarta.persistence.EntityManager;
import imf.cels.persistence.AbstractDAO;
import imf.cels.entity.Usuario;

import java.util.List;


public class UsuarioDAO extends AbstractDAO<Usuario> {
    private final EntityManager entityManager;

    public UsuarioDAO(EntityManager em) {
        super(Usuario.class);
        this.entityManager = em;
    }

    public List<Usuario> obtenerTodos(){
        return entityManager
                .createQuery("SELECT a FROM Usuario a", Usuario.class)
                .getResultList();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public boolean existeCorreo(String correo) {
        List<Usuario> result = entityManager.createQuery(
                        "SELECT u FROM Usuario u WHERE u.email = :correo", Usuario.class)
                .setParameter("correo", correo)
                .getResultList();
        return !result.isEmpty();
    }


}
