package ui;

import imf.cels.delegate.DelegateCotizacion;
import imf.cels.entity.*;
import helper.CotizacionHelper;
import imf.cels.integration.ServiceFacadeLocator;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Named
@ViewScoped
public class CotizacionBeanUI implements Serializable {
    private final CotizacionHelper cotizacionHelper = new CotizacionHelper();
    private final DelegateCotizacion delegateCotizacion = new DelegateCotizacion();

    private Cotizacion cotizacion = new Cotizacion();
    private CotizacionMaterial  nuevoMaterial = new CotizacionMaterial();
    private CotizacionManoDeObra nuevaManoDeObra = new CotizacionManoDeObra();

    private List<Cotizacion> cotizaciones;
    private List<CotizacionMaterial> listaMateriales = new ArrayList<>();
    private List<CotizacionManoDeObra> listaManoDeObra = new ArrayList<>();

    private Integer idMaterialSeleccionado;
    private Integer numResponsable;
    private BigDecimal costoMaterialSeleccionado;
    private BigDecimal cantidadSeleccionada;
    private BigDecimal cantidadHorasMDO;
    private BigDecimal costoHorasMDO;
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

    //Para mostrar Lista de materiales en tabla
    public Set<CotizacionMaterial> getMateriales() {
        return cotizacion.getCotizacionMateriales();
    }

    /*Funcion para agregar materiales a la lista de materiales
    * que se guardará junto con la cotización*/
    public void agregarMaterial() {
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            Material material = ServiceFacadeLocator.getInstanceFacadeMaterial().obtenerPorId(idMaterialSeleccionado);

            if(material == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Material no encontrado"));
                return;
            }

            CotizacionMaterial cm = new CotizacionMaterial();
            cm.setIdMaterial(material);
            cm.setCantidad(cantidadSeleccionada);

            //Calculo del subtotal
            BigDecimal subtotal = material.getCosto().multiply(cantidadSeleccionada);
            cm.setSubtotal(subtotal);

            listaMateriales.add(cm);

            context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Material agregado", "Se agregó " + material.getNombre()));

        } catch(Exception e){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error interno", e.getMessage()));
        }
    }

    /*Funcion para guardar la lista del numero responsables de mano de obra
    * que se guardará junto con la cotización*/
    public void agregarManoDeObra() {
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            CotizacionManoDeObraId cmdoid = new CotizacionManoDeObraId();
            cmdoid.setNumResponsable(numResponsable);

            CotizacionManoDeObra cmdo = new CotizacionManoDeObra();
            cmdo.setCantidadHoras(cantidadHorasMDO);
            cmdo.setCostoHora(costoHorasMDO);

            //Calculo del subtotal
            BigDecimal subtotal = cmdo.getCostoHora().multiply(cantidadHorasMDO);
            cmdo.setSubtotal(subtotal);

            listaManoDeObra.add(cmdo);

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Responsable agregado", "Se agregó el responsable " + cmdoid.getNumResponsable()));
        }catch(Exception e){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error interno", e.getMessage()));
        }
    }

    //Registro de cotizacion

    public void registrarCotizacion() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            //Guardado de Cada material vinculado a la cotizacion
            /*for(CotizacionMaterial cm : listaMateriales) {
                CotizacionMaterialId id = new CotizacionMaterialId();
                id.setIdFolio(cotizacion.getId());
                id.setIdMaterial(cm.getIdMaterial().getId());

                cm.setId(id); //se establece el id a partir del folio y el id del material (compuesto)
                cm.setIdFolio(cotizacion); //se asocia a la cotizacion

                ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacionMaterial(cm);
            }*/

            //Guardado de Cada Responsable en Mano de obra vinculado a la cotizacion
            /*for(CotizacionManoDeObra cmdo : listaManoDeObra) {
                CotizacionManoDeObraId id = new CotizacionManoDeObraId();
                id.setIdFolio(cotizacion.getId());
                id.setNumResponsable(numResponsable);

                cmdo.setId(id);
                cmdo.setIdFolio(cotizacion);

                ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacionManoDeObra(cmdo);
            }*/

            //Asociacion de la cotización a cada elemento
            for(CotizacionMaterial cm : listaMateriales) {
                cm.setIdFolio(cotizacion);
            }

            for(CotizacionManoDeObra cmdo : listaManoDeObra) {
                cmdo.setIdFolio(cotizacion);
            }

            //Asociacion a las listas de Materiales y Mano de Obra
            cotizacion.setCotizacionMateriales(new LinkedHashSet<>(listaMateriales));
            cotizacion.setCotizacionManoDeObras(new LinkedHashSet<>(listaManoDeObra));

            //Guardado de Cotizacion (Gracias al cascade activado se guarda junto)
            cotizacionHelper.saveCotizacion(cotizacion);

            String precioFinFormat = String.format("%.2f", cotizacion.getPrecioFinal());

            String msg = String.format(
                    "Cotización registrada correctamente.%n" +
                            "Fecha: %t%n" +
                            "Folio: %06d%n" +
                            "Cliente: %s%n" +
                            "ID Vendedor: %03d%n" +
                            "Costo de Materiales: %s%n" +
                            "Costo de Mano de Obra: %s%n" +
                            "Cotizado en: %s%n",
                    cotizacion.getFecha(),
                    cotizacion.getId(),
                    cotizacion.getCliente(),
                    cotizacion.getIdUsuario(),
                    precioFinFormat
            );

            //Limpiar el registro para un nuevo
            cotizacion = new Cotizacion();
            listaManoDeObra.clear();
            listaMateriales.clear();

            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Cotización creada Exitosamente", msg));
        } catch (IllegalArgumentException ex) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validación", ex.getMessage()));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error interno", "No se pudo registrar el usuario."));
        }
    }

    //Setter y Getters del registro
    public Cotizacion getCotizacion() { return cotizacion; }
    public void setCotizacion(Cotizacion cotizacion) {  this.cotizacion = cotizacion; }

    public List<CotizacionMaterial> getListaMateriales() { return listaMateriales; }
    public void setListaMateriales(List<CotizacionMaterial> listaMateriales) {  this.listaMateriales = listaMateriales; }

    public List<CotizacionManoDeObra> getListaManoDeObra() { return listaManoDeObra; }
    public void setListaManoDeObra(List<CotizacionManoDeObra> listaManoDeObra) {  this.listaManoDeObra = listaManoDeObra; }

    public Integer getIdMaterialSeleccionado() { return idMaterialSeleccionado; }
    public void setIdMaterialSeleccionado(Integer idMaterialSeleccionado) {  this.idMaterialSeleccionado = idMaterialSeleccionado; }

    public Integer getNumResponsable() { return numResponsable; }
    public void setNumResponsable(Integer numResponsable) {  this.numResponsable = numResponsable; }

    public BigDecimal getCostoMaterialSeleccionado() { return costoMaterialSeleccionado; }
    public void setCostoMaterialSeleccionado(BigDecimal costoMaterialSeleccionado) { this.costoMaterialSeleccionado = costoMaterialSeleccionado; }

    public BigDecimal getCantidadSeleccionada() { return cantidadSeleccionada; }
    public void setCantidadSeleccionada(BigDecimal cantidadSeleccionada) { this.cantidadSeleccionada = cantidadSeleccionada; }

    public BigDecimal getCantidadHorasMDO() { return cantidadHorasMDO; }
    public void setCantidadHorasMDO(BigDecimal cantidadHorasMDO) { this.cantidadHorasMDO = cantidadHorasMDO; }

    public BigDecimal getCostoHorasMDO() { return costoHorasMDO; }
    public void setCostoHorasMDO(BigDecimal costoHorasMDO) { this.costoHorasMDO = costoHorasMDO; }
}