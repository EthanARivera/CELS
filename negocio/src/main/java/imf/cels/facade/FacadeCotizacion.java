package imf.cels.facade;

import imf.cels.dao.CotizacionDAO;
import imf.cels.delegate.DelegateCotizacion;
import imf.cels.entity.Cotizacion;
import imf.cels.entity.Usuario;
import imf.cels.integration.ServiceLocator;
import java.util.List;

public class FacadeCotizacion {
    private final CotizacionDAO cotizacionDAO;

    public FacadeCotizacion(){
        this.cotizacionDAO = ServiceLocator.getInstanceCotizacionDAO();
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return cotizacionDAO.obtenerTodosPorFecha();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return cotizacionDAO.obtenerPorFolio();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return cotizacionDAO.obtenerPorVendedor();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return cotizacionDAO.obtenerPorAnio(anio);
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return cotizacionDAO.obtenerPorMes(mes);
    }

    public List<Integer> obtenerAniosDisponibles(){
        return cotizacionDAO.obtenerAniosDisponibles();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return cotizacionDAO.obtenerMesesDisponibles();
    }
}