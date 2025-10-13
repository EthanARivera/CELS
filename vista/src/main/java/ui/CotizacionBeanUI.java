package ui;

import imf.cels.delegate.DelegateCotizacion;
import imf.cels.entity.Cotizacion;
import imf.cels.integration.ServiceFacadeLocator;
import imf.cels.integration.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class CotizacionBeanUI implements Serializable {
    private final DelegateCotizacion delegateCotizacion = new DelegateCotizacion();
    private List<Cotizacion> cotizaciones;
    private int anioFiltro;
    private int mesFiltro;
    private int idBusqueda;
    private List<Integer> aniosDisponibles;
    private List<Integer> mesesDisponibles;

    @PostConstruct
    public void init(){
        cotizaciones = delegateCotizacion.obtenerTodosPorFecha();
        aniosDisponibles = delegateCotizacion.obtenerAniosDisponibles();
        mesesDisponibles = delegateCotizacion.obtenerMesesDisponibles();
    }

    public void buscarPorId(){
        cotizaciones = delegateCotizacion.buscarPorId(idBusqueda);
    }

    public int getIdBusqueda() {
        return idBusqueda;
    }

    public void setIdBusqueda(int idBusqueda) {
        this.idBusqueda = idBusqueda;
    }

    public void consultarPorFecha(){
        cotizaciones = delegateCotizacion.obtenerTodosPorFecha();
    }

    public void consultarPorFolio(){
        cotizaciones = delegateCotizacion.obtenerPorFolio();
    }

    public void consultarPorVendedor(){
        cotizaciones = delegateCotizacion.obtenerPorVendedor();
    }

    public void obtenerPorAnio(){
        cotizaciones = delegateCotizacion.obtenerPorAnio(anioFiltro);
    }

    public void obtenerPorMes(){
        cotizaciones = delegateCotizacion.obtenerPorMes(mesFiltro);
    }

    public List<Cotizacion> getCotizaciones() {
        return cotizaciones;
    }

    public int getAnioFiltro() {
        return anioFiltro;
    }

    public void setAnioFiltro(int anioFiltro) {
        this.anioFiltro = anioFiltro;
    }

    public List<Integer> getAniosDisponibles() {
        return aniosDisponibles;
    }

    public int getMesFiltro() {
        return mesFiltro;
    }

    public void  setMesFiltro(int mesFiltro) {
        this.mesFiltro = mesFiltro;
    }

    public List<Integer> getMesesDisponibles() {
        return mesesDisponibles;
    }

    public String obtenerNombreMes(int mes){
        String[] nombres = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        if (mes >= 1 && mes <= 12) return nombres[mes - 1];
        return "Mes " + mes;
    }
}