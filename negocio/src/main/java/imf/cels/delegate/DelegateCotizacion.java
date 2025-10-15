package imf.cels.delegate;

import imf.cels.entity.Cotizacion;
import imf.cels.facade.FacadeCotizacion;
import imf.cels.integration.ServiceLocator;

import java.util.List;

public class DelegateCotizacion {
    private final FacadeCotizacion facadeCotizacion;

    public DelegateCotizacion(){
        this.facadeCotizacion = new FacadeCotizacion();
    }

    public List<Cotizacion> buscarPorId(int id){
        return ServiceLocator.getInstanceCotizacionDAO().buscarPorId(id);
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return facadeCotizacion.obtenerTodosPorFecha();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return facadeCotizacion.obtenerPorFolio();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return facadeCotizacion.obtenerPorVendedor();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return facadeCotizacion.obtenerPorAnio(anio);
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return facadeCotizacion.obtenerPorMes(mes);
    }

    public List<Integer> obtenerAniosDisponibles(){
        return facadeCotizacion.obtenerAniosDisponibles();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return  facadeCotizacion.obtenerMesesDisponibles();
    }
}