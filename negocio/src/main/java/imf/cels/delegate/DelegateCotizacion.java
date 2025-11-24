package imf.cels.delegate;

import imf.cels.entity.Cotizacion;
import imf.cels.entity.CotizacionMaterial;
import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.integration.ServiceLocator;

import java.util.List;

public class DelegateCotizacion {

    public List<Cotizacion> buscarPorId(int id){
        return ServiceLocator.getInstanceCotizacionDAO().buscarPorId(id);
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerTodosPorFecha();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorFolio();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorVendedor();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorAnio(anio);
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorMes(mes);
    }

    public List<Integer> obtenerAniosDisponibles(){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerAniosDisponibles();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return  ServiceLocator.getInstanceCotizacionDAO().obtenerMesesDisponibles();
    }

    //Guardado de entidad Cotizacion
    //Llamando a CotizacionDAO desde ServiceLocator
    public void saveCotizacion(Cotizacion cotizacion){
        ServiceLocator.getInstanceCotizacionDAO().registrarCotizacion(cotizacion);
    }


    //Guardado de entidad CotizacionMaterial
    //Llamando a CotizacionMaterialDAO desde ServiceLocator
    public void saveCotizacionMaterial(CotizacionMaterial cotizacionMaterial) {
        ServiceLocator.getCotizacionMaterialDAO().registrarCotizacionMaterial(cotizacionMaterial);

    }


    //Guardado de entidad CotizacionManoDeObra
    //Llamando a CotizacionManoDeObraDAO desde ServiceLocator
    public void saveCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) {
        ServiceLocator.getCotizacionManoDeObraDAO().registrarCotizacionManoDeObra(cotizacionManoDeObra);
    }


    // Aprobación de Cotización
    public void aprobarCotizacion(Integer idFolio) {
        ServiceLocator.getInstanceCotizacionDAO().aprobarCotizacion(idFolio);
    }

    public Integer ultimoFolio() { return ServiceLocator.getInstanceCotizacionDAO().ultimoFolio(); }

    //actualización de cotización
    public Cotizacion buscarPorIdUnico(int id) {
        return ServiceLocator.getInstanceCotizacionDAO().buscarPorIdUnico(id);
    }

}