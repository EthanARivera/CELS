package imf.cels.dao;

import imf.cels.entity.DatosEncuesta;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.EntityManager;

import java.util.List;

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

    // Estadistica ----------------------------------------------------------
      // Q1: Yes / No
    public List<Object[]> contarQ1() {
        return em.createQuery("""
        SELECT d.q1, COUNT(d)
        FROM DatosEncuesta d
        GROUP BY d.q1
    """, Object[].class).getResultList();
    }

    // Q2
    public List<Object[]> contarQ2() {
        return em.createQuery("""
        SELECT d.q2, COUNT(d)
        FROM DatosEncuesta d
        GROUP BY d.q2
        ORDER BY d.q2
    """, Object[].class).getResultList();
    }

    // Q3
    public List<Object[]> contarQ3() {
        return em.createQuery("""
        SELECT d.q3, COUNT(d)
        FROM DatosEncuesta d
        GROUP BY d.q3
        ORDER BY d.q3
    """, Object[].class).getResultList();
    }

    // Q4
    public List<Object[]> contarQ4() {
        return em.createQuery("""
        SELECT d.q4, COUNT(d)
        FROM DatosEncuesta d
        GROUP BY d.q4
        ORDER BY d.q4
    """, Object[].class).getResultList();
    }

    // Q5 (comentarios)
    public List<String> obtenerComentarios() {
        return em.createQuery("""
        SELECT d.q5
        FROM DatosEncuesta d
        WHERE d.q5 IS NOT NULL AND d.q5 <> ''
    """, String.class).getResultList();
    }
}