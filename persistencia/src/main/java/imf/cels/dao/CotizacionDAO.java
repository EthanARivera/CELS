package imf.cels.dao;

import imf.cels.entity.Cotizacion;
import imf.cels.integration.ServiceLocator;
import imf.cels.persistence.AbstractDAO;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Properties;

public class CotizacionDAO extends AbstractDAO<Cotizacion>{
    private final EntityManager entityManager;

    public CotizacionDAO(EntityManager em) {
        super(Cotizacion.class);
        this.entityManager = em;
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c ORDER BY c.fecha DESC", Cotizacion.class)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c ORDER BY c.id ASC", Cotizacion.class)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c ORDER BY c.idUsuario.nombre ASC", Cotizacion.class)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE YEAR(c.fecha) = :anio ORDER BY c.fecha DESC", Cotizacion.class)
                .setParameter("anio", anio).getResultList();
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE MONTH(c.fecha) = :mes ORDER BY c.fecha DESC", Cotizacion.class)
                .setParameter("mes", mes).getResultList();
    }

    public List<Cotizacion> buscarPorId(int id){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE c.id = :id",  Cotizacion.class)
                .setParameter("id", id).getResultList();
    }

    public List<Integer> obtenerAniosDisponibles(){
        return entityManager
                .createQuery("SELECT DISTINCT YEAR(c.fecha) FROM Cotizacion c ORDER BY YEAR(c.fecha) DESC",  Integer.class)
                .getResultList();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return entityManager
                .createQuery("SELECT DISTINCT MONTH(c.fecha) FROM Cotizacion c ORDER BY MONTH(c.fecha)", Integer.class)
                .getResultList();
    }


    //Registro de cotizaciones

    //Guardado de entidad Cotizacion
    public void registrarCotizacion(Cotizacion cotizacion) {

        //Validacion
       if(cotizacion.getIdUsuario() == null) {
            throw new IllegalArgumentException("No se encuentra ningún usuario vendedor activo. ");
        }

        if(cotizacion.getFecha() == null) {
            throw new IllegalArgumentException("La fecha no se puede recuperar de manera correcta. ");
        }

        if(cotizacion.getCliente() == null) {
            throw new IllegalArgumentException("Es necesario especificar el nombre del cliente. ");
        }

        if(cotizacion.getPrecioFinal() == null) {
            throw new IllegalArgumentException("El precio final no puede ser nulo.");
        }

        //Guardado
        ServiceLocator.getInstanceCotizacionDAO().save(cotizacion);
    }

    //función con query para obtener el último folio existente
    public Integer ultimoFolio() {
        Integer ultimoIdFolio = entityManager
                .createQuery("SELECT MAX(c.id) FROM Cotizacion c", Integer.class)
                .getSingleResult();

        //retorna 0 si no se encuentra ningun folio
        return (ultimoIdFolio != null) ? ultimoIdFolio : 0;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }


    // Aprobación de Cotización
    public void aprobarCotizacion(Integer idFolio) {
        Cotizacion cotizacion = entityManager.find(Cotizacion.class, idFolio);

        if (cotizacion == null) {
            throw new IllegalArgumentException("No se encontró la cotización con el folio especificado.");
        }

        if (Boolean.TRUE.equals(cotizacion.getisCotizacionAprobado())) {
            throw new IllegalStateException("La cotización ya fue aprobada y no puede desaprobarse.");
        }

        /*executeInsideTransaction(em -> {
            cotizacion.setAprobado(true);
            em.merge(cotizacion);
        });*/
        try {
            entityManager.getTransaction().begin();
            cotizacion.setisCotizacionAprobado(true);
            entityManager.merge(cotizacion);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }


    // Aprobación de Contrato
    public void aprobarContrato(Integer idFolio) {
        Cotizacion cotizacion = entityManager.find(Cotizacion.class, idFolio);

        if (cotizacion == null) {
            throw new IllegalArgumentException("No se encontró el contrato con el folio especificado.");
        }

        if (Boolean.TRUE.equals(cotizacion.getisContratoAprobado())) {
            throw new IllegalStateException("El contrato ya fue aprobada y no puede desaprobarse.");
        }

        /*executeInsideTransaction(em -> {
            cotizacion.setAprobado(true);
            em.merge(cotizacion);
        });*/
        try {
            entityManager.getTransaction().begin();
            cotizacion.setisContratoAprobado(true);
            entityManager.merge(cotizacion);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }
}