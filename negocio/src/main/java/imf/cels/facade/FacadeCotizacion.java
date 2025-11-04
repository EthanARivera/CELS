package imf.cels.facade;

import imf.cels.dao.CotizacionDAO;
import imf.cels.delegate.DelegateCotizacion;
import imf.cels.entity.Cotizacion;
import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.entity.CotizacionMaterial;
import imf.cels.integration.ServiceLocator;
import java.util.List;

public class FacadeCotizacion {

    private final DelegateCotizacion delegateCotizacion;

    public FacadeCotizacion(){
        this.delegateCotizacion = new DelegateCotizacion();
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return delegateCotizacion.obtenerTodosPorFecha();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return delegateCotizacion.obtenerPorFolio();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return delegateCotizacion.obtenerPorVendedor();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return delegateCotizacion.obtenerPorAnio(anio);
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return delegateCotizacion.obtenerPorMes(mes);
    }

    public List<Integer> obtenerAniosDisponibles(){
        return delegateCotizacion.obtenerAniosDisponibles();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return delegateCotizacion.obtenerMesesDisponibles();
    }

    public void saveCotizacion(Cotizacion cotizacion){ delegateCotizacion.saveCotizacion(cotizacion); }

    public void saveCotizacionMaterial(CotizacionMaterial cotizacionMaterial) { delegateCotizacion.saveCotizacionMaterial(cotizacionMaterial); }

    public void saveCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) { delegateCotizacion.saveCotizacionManoDeObra(cotizacionManoDeObra); }

    public void enviarEmail() { delegateCotizacion.enviarEmail(); }
}