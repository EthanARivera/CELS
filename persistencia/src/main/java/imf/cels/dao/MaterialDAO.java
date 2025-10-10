package imf.cels.dao;

import imf.cels.entity.Material;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.EntityManager;
import java.util.List;

public class MaterialDAO extends AbstractDAO<Material> {

    private final EntityManager entityManager;

    public MaterialDAO(EntityManager em) {
        super(Material.class);
        this.entityManager = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Material> obtenerTodos() {
        return entityManager
                .createQuery("SELECT m FROM Material m", Material.class)
                .getResultList();
    }

    public Material buscarPorNombre(String nombre) {
        try {
            return entityManager
                    .createQuery("SELECT m FROM Material m WHERE m.nombre = :nombre", Material.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
