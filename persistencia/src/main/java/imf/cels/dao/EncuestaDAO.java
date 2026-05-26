package imf.cels.dao;

import imf.cels.entity.DatosEncuesta;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.EntityManager;

public class EncuestaDAO extends AbstractDAO<DatosEncuesta> {

    private final EntityManager em;

    public EncuestaDAO(EntityManager em) {
        super(DatosEncuesta.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void registrarEncuesta(DatosEncuesta encuesta) {
        save(encuesta);
    }
}