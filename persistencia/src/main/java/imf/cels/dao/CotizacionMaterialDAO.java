package imf.cels.dao;

import imf.cels.entity.CotizacionMaterial;
import imf.cels.integration.ServiceLocator;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.EntityManager;

public class CotizacionMaterialDAO extends AbstractDAO<CotizacionMaterial> {
    private final EntityManager entityManager;

    public CotizacionMaterialDAO(EntityManager em) {
        super(CotizacionMaterial.class);
        this.entityManager = em;
    }

    //Guardado de entidad CotizacionMaterial
    public void registrarCotizacionMaterial(CotizacionMaterial cotizacionMaterial){
        //Guardado
        ServiceLocator.getCotizacionMaterialDAO().save(cotizacionMaterial);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
