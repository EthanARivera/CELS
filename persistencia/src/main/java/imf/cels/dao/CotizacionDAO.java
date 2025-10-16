package imf.cels.dao;

import imf.cels.entity.Cotizacion;
import imf.cels.integration.ServiceLocator;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.EntityManager;
import java.util.List;

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
        if(existeFolio(cotizacion.getId()))
            throw new IllegalArgumentException("El Folio ya se encuentra en uso. Favor de recargarlo para utilizar uno disponible.");

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

    public boolean existeFolio(Integer id) {
        List<Cotizacion> result = entityManager.createQuery(
                "SELECT c FROM Cotizacion c WHERE c.id = :id", Cotizacion.class)
                .setParameter("id", id)
                .getResultList();
        return !result.isEmpty();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}