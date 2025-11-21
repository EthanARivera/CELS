package ui;

import helper.MaterialHelper;
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
import java.time.LocalDate;
import java.util.*;

@Named ()
@ViewScoped
public class CotizacionBeanUI implements Serializable {
    private final CotizacionHelper cotizacionHelper = new CotizacionHelper();
    private final MaterialHelper materialHelper = new MaterialHelper();
    private final DelegateCotizacion delegateCotizacion = new DelegateCotizacion();

    private Cotizacion cotizacion = new Cotizacion();
    private boolean modoEdicion = false; //Bandera para la accion del boton guardar actualizacion

    private String tituloFormulario = "Nueva Cotización";
    private CotizacionMaterial  nuevoMaterial = new CotizacionMaterial();
    private CotizacionManoDeObra nuevaManoDeObra = new CotizacionManoDeObra();

    private List<Cotizacion> cotizaciones;
    private List<CotizacionMaterial> listaMateriales = new ArrayList<>();
    private List<CotizacionManoDeObra> listaManoDeObra = new ArrayList<>();

    //variables auxiliares
    private Integer idMaterialSeleccionado;
    private Integer numResponsable;
    private BigDecimal costoMaterialSeleccionado = BigDecimal.ZERO;
    private BigDecimal cantidadSeleccionada = BigDecimal.ZERO;
    private BigDecimal cantidadHorasMDO = BigDecimal.ZERO;
    private BigDecimal costoHorasMDO = BigDecimal.ZERO;

    // Totales manejados SOLO en el bean
    // Campos para totales y ganancia
    private BigDecimal totalMateriales = BigDecimal.ZERO;
    private BigDecimal totalManoDeObra = BigDecimal.ZERO;
    private BigDecimal costoBruto = BigDecimal.ZERO;
    private BigDecimal gananciaPercent = BigDecimal.ZERO; // ej. 50 para 50%
    private BigDecimal precioFinal = BigDecimal.ZERO;
    private boolean requiereFactura = false;

    //Filtros para consulta
    private int anioFiltro;
    private int mesFiltro;
    private int idBusqueda;
    private List<Integer> aniosDisponibles;
    private List<Integer> mesesDisponibles;

    @PostConstruct
    public void init() {
        cotizaciones = delegateCotizacion.obtenerTodosPorFecha();
        aniosDisponibles = delegateCotizacion.obtenerAniosDisponibles();
        mesesDisponibles = delegateCotizacion.obtenerMesesDisponibles();

        if (cotizacion.getIdUsuario() == null) {
            cotizacion.setIdUsuario(new Usuario());
        }

        //IMPORTANTE PARA ACTUALIZACION
        System.out.println("INIT COTIZACION — Bean cargado");

        Map<String, String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String idString = params.get("idCotizacion");
        System.out.println("INIT COTIZACION: Bean cargado");

        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                System.out.println("Cargando cotización ID: " + id);
                cargarCotizacionParaEditarPorId(id);  // mEtodo que esta abajo
                modoEdicion = true;
            } catch (NumberFormatException ignore) {
                System.out.println("ERROR PARSEANDO ID  ");
            }
        }
    }

    //IMPORTANTE PARA ACTUALIZACION
    public void cargarCotizacionParaEditarPorId(int id) {

        Cotizacion c = delegateCotizacion.buscarPorIdUnico(id);

        if (c != null) {

            // Asignar la entidad al bean
            this.cotizacion = c;

            //cargar listas desde la entidad
            this.listaMateriales = new ArrayList<>(c.getCotizacionMateriales());
            this.listaManoDeObra = new ArrayList<>(c.getCotizacionManoDeObras());

            //GANANCIA como NO existe en BD, la dejamos en 0
            this.gananciaPercent = BigDecimal.ZERO;

            //FACTURA como NO existe en BD, siempre false
            this.requiereFactura = false;

            //Recalcular totales a partir de los materiales y mano de obra
            recalcularTotales();

            //Activar modo edición
            this.modoEdicion = true;

            //Título
            this.tituloFormulario = "Actualizar Cotización";

            System.out.println("COTIZACIÓN CARGADA EN BEAN correctamente.");
        }
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

    public List<Material> getMaterialesDisponibles() { return materialHelper.obtenerMateriales(); }

    //actualizacion
    public void guardarPseudoModificacion() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            // Siempre es una NUEVA cotización con el ID NULL
            cotizacion.setId(null);

            // Fecha nueva
            cotizacion.setFecha(LocalDate.now());

            for (CotizacionMaterial cm : listaMateriales) {
                cm.setIdFolio(cotizacion);
            }

            for (CotizacionManoDeObra cmdo : listaManoDeObra) {
                cmdo.setIdFolio(cotizacion);
            }

            cotizacion.setCotizacionMateriales(new LinkedHashSet<>(listaMateriales));
            cotizacion.setCotizacionManoDeObras(new LinkedHashSet<>(listaManoDeObra));

            recalcularTotales();
            cotizacion.setPrecioFinal(precioFinal);

            cotizacionHelper.saveCotizacion(cotizacion);

            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Cotización Actualizada",
                            "Se generó una nueva cotización basada en la anterior."));

            limpiarFormulario();

        } catch (Exception ex) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al actualizar", ex.getMessage()));
        }
    }


    //Metodo para guardar cotizacion segun su tipo (Registro nuevo o actualizacion)
    public void guardarCotizacion() {
        if (modoEdicion) {
            guardarPseudoModificacion();
        } else {
            registrarCotizacion();
        }
    }

    //para limpiar el formulario despues de guardar
    private void limpiarFormulario() {
        this.cotizacion = new Cotizacion();
        cotizacion.setIdUsuario(new Usuario());
        this.listaMateriales.clear();
        this.listaManoDeObra.clear();
        this.modoEdicion = false;
    }

    //Cambia el texto del boton dependiendo de si es registro o actualizacion
    public String getTextoBotonGuardar() {
        if (modoEdicion) {
            return "Guardar Cambios";
        } else {
            return "Generar Cotización";
        }
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
            //settear la fecha


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

            //Asignar la fecha actual
            cotizacion.setFecha(LocalDate.now());
            // recalcular totales antes de persisitir  la información
            recalcularTotales();
            //Asignacion del precio final a la cotizacion
            cotizacion.setPrecioFinal(precioFinal);


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
            cotizacion.setIdUsuario(new Usuario());
            listaManoDeObra.clear();
            listaMateriales.clear();

            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Cotización creada Exitosamente", msg));
        } catch (IllegalArgumentException ex) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validación", ex.getMessage()));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error interno", "No se pudo registrar la cotizacion."));
        }
    }

    // ---- recalcula todos los totales ----
    private void recalcularTotales() {
        totalMateriales = listaMateriales.stream()
                .map(CotizacionMaterial::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalManoDeObra = listaManoDeObra.stream()
                .map(CotizacionManoDeObra::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        costoBruto = totalMateriales.add(totalManoDeObra);

        // aplicar ganancia que esté guardada
        BigDecimal gananciaDecimal = gananciaPercent.divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP);
        precioFinal = costoBruto.add(costoBruto.multiply(gananciaDecimal));

        // si requiere factura, sumamos IVA (8% ejemplo)
        if (requiereFactura) {
            BigDecimal iva = BigDecimal.valueOf(0.08);
            precioFinal = precioFinal.add(precioFinal.multiply(iva));
        }
    }

    //Funciones directas para el xhtml
    // ---- eliminar material (se invoca desde XHTML) ----
    public void eliminarMaterial(CotizacionMaterial cm) {
        if (cm != null) {
            listaMateriales.remove(cm);
            recalcularTotales();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Material eliminado", "Material removido de la lista"));
        }
    }

    // ---- eliminar mano de obra ----
    public void eliminarManoDeObra(CotizacionManoDeObra mo) {
        if (mo != null) {
            listaManoDeObra.remove(mo);
            recalcularTotales();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Responsable eliminado", "Responsable removido"));
        }
    }

    // ---- actualizar subtotal material (cuando se edita cantidad) ----
    public void actualizarSubtotalMaterial(CotizacionMaterial cm) {
        if (cm != null && cm.getIdMaterial() != null && cm.getCantidad() != null) {
            BigDecimal precio = cm.getIdMaterial().getCosto();
            cm.setSubtotal(precio.multiply(cm.getCantidad()));
            recalcularTotales();
        }
    }

    // ---- actualizar subtotal mano de obra (cuando se cambian horas o costo) ----
    public void actualizarSubtotalMDO(CotizacionManoDeObra mo) {
        if (mo != null && mo.getCostoHora() != null && mo.getCantidadHoras() != null) {
            mo.setSubtotal(mo.getCostoHora().multiply(mo.getCantidadHoras()));
            // aseguramos que el embeddable id tenga numResponsable si no existe
            if (mo.getId() == null) {
                CotizacionManoDeObraId id = new CotizacionManoDeObraId();
                id.setNumResponsable(this.numResponsable != null ? this.numResponsable : 0);
                mo.setId(id);
            }
            recalcularTotales();
        }
    }


    // ---- aplicar ganancia (se llama desde botón) ----
    public void aplicarGanancia() {
        if (gananciaPercent == null) gananciaPercent = BigDecimal.ZERO;
        recalcularTotales();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Ganancia aplicada",
                        "Índice: " + gananciaPercent + "%"));
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

    public BigDecimal getTotalMateriales() { return totalMateriales; }
    public void setTotalMateriales(BigDecimal totalMateriales) { this.totalMateriales = totalMateriales; }

    public BigDecimal getTotalManoDeObra() { return totalManoDeObra; }
    public void setTotalManoDeObra(BigDecimal totalManoDeObra) { this.totalManoDeObra = totalManoDeObra; }

    public BigDecimal getCostoBruto() { return costoBruto; }
    public void setCostoBruto(BigDecimal costoBruto) { this.costoBruto = costoBruto; }

    public BigDecimal getGananciaPercent() { return gananciaPercent; }
    public void setGananciaPercent(BigDecimal gananciaPercent) { this.gananciaPercent = gananciaPercent; }

    public BigDecimal getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(BigDecimal precioFinal) { this.precioFinal = precioFinal; }

    public boolean isRequiereFactura() { return requiereFactura; }
    public void setRequiereFactura(boolean requiereFactura) { this.requiereFactura = requiereFactura; }

    public void enviarEmail() { cotizacionHelper.enviarEmail(); }

    //actualizacion de cotizaciones
    //Se usa para el cambio de titulo dependiendo de si es registro o actualizacion
    public String getTituloFormulario() {
        return tituloFormulario;
    }

    public void setTituloFormulario(String tituloFormulario) {
        this.tituloFormulario = tituloFormulario;
    }
}