package imf.cels.dao;

import imf.cels.entity.Material;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.EntityManager;
import java.util.List;

public class MaterialDAO extends AbstractDAO<Material> {

    private final EntityManager entityManager;

    public MaterialDAO(EntityManager entityManager) {
        super(Material.class);
        this.entityManager = entityManager;
    }


    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
