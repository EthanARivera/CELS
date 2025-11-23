package imf.cels.dao;

import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.integration.ServiceLocator;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.EntityManager;

public class CotizacionManoDeObraDAO extends AbstractDAO<CotizacionManoDeObra> {
    private final EntityManager entityManager;

    public CotizacionManoDeObraDAO(EntityManager em) {
        super(CotizacionManoDeObra.class);
        this.entityManager = em;
    }

    //Guardado de entidad CotizacionManoDeObra
    public void registrarCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) {
        //Guardado
        ServiceLocator.getCotizacionManoDeObraDAO().update(cotizacionManoDeObra);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}