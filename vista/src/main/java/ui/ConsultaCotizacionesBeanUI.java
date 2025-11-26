package ui;

import helper.CotizacionHelper;
import imf.cels.delegate.DelegateCotizacion;
import imf.cels.entity.*;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named ("cotizacionUI")
@ViewScoped
public class ConsultaCotizacionesBeanUI implements Serializable {
    @Inject
    private LoginBeanUI loginUI;

    private CotizacionHelper cotizacionHelper;

    private final DelegateCotizacion delegateCotizacion = new DelegateCotizacion();

    //Filtros y Busqueda de cotizaciones
    private List<Cotizacion> cotizaciones;
    private int anioFiltro;
    private int mesFiltro;
    private int idBusqueda;
    private List<Integer> aniosDisponibles;
    private List<Integer> mesesDisponibles;

    //variables para la facturación
    private Cotizacion cotizacionSeleccionada;
    private String emailParaEnvio;

    // Aprobación de Cotización
    private boolean dialogAprobacionVisible;
    private String textoConfirmacion;
    private Integer idCotizacionSeleccionada;


    @PostConstruct
    public void init(){
        try {
            loginUI.verificarSesion();
            System.out.println(loginUI.getUsuario().getNombre());
        } catch (Exception e) {
            e.printStackTrace();
        }

        cotizacionHelper = new CotizacionHelper();
        cotizaciones = delegateCotizacion.obtenerTodosPorFecha();
        aniosDisponibles = delegateCotizacion.obtenerAniosDisponibles();
        mesesDisponibles = delegateCotizacion.obtenerMesesDisponibles();
    }

    public String obtenerNombreMes(int mes){
        String[] nombres = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        if (mes >= 1 && mes <= 12) return nombres[mes - 1];
        return "Mes " + mes;
    }

    /*******************************************
     *    Aprobación (Marinela Verganio)
     *******************************************/

    // Aprobación de Cotización
    public void mostrarDialogoAprobacion(Cotizacion cotizacion) {
        this.idCotizacionSeleccionada = cotizacion.getId();
        this.textoConfirmacion = "";
        this.dialogAprobacionVisible = true;
    }

    public void aprobarCotizacion() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // Check if confirmation text is exactly "Aprobado"
            if (textoConfirmacion == null || !"Aprobado".equalsIgnoreCase(textoConfirmacion.trim())) {
                context.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Confirmación incorrecta",
                        "El texto ingresado es incorrecto. Escriba exactamente 'Aprobado' para confirmar."
                ));
                return;
            }

            // Approve the quotation
            cotizacionHelper.aprobarCotizacion(idCotizacionSeleccionada);
            dialogAprobacionVisible = false;

            // Refresh list of cotizaciones
            cotizaciones = delegateCotizacion.obtenerTodosPorFecha();

            // Success message
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Cotización aprobada",
                    "La cotización con ID " + idCotizacionSeleccionada + " ha sido aprobada correctamente."
            ));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error interno",
                    "No se pudo aprobar la cotización: " + e.getMessage()
            ));
        }
    }

    public void cancelarCotizacion() {
        dialogAprobacionVisible = false;
        textoConfirmacion = "";
    }

    /**************************************************
     *         Envío de Correos (Brayan Leon)
     **************************************************/

    public void enviarCorreo(Cotizacion cotizacion) {
        FacesContext context = FacesContext.getCurrentInstance();

        //validación
        if (cotizacion == null || cotizacion.getId() == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Error", "No se pudo seleccionar la cotización."));
            return;
        }

        try {

            cotizacionHelper.enviarCotizacionPorCorreo(cotizacion.getId());

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Correo enviado para el Folio #" + cotizacion.getId()));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al enviar", "No se pudo enviar el correo: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void enviarContrato(Cotizacion cotizacion) {
        FacesContext context = FacesContext.getCurrentInstance();

        //validación
        if (cotizacion == null || cotizacion.getId() == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Error", "No se pudo seleccionar la cotización."));
            return;
        }

        if (!cotizacion.getisCotizacionAprobado()) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Cotización no aprobada",
                    "Debe aprobar la cotización antes de enviar el contrato."
            ));
            return;
        }

        try {
            boolean enviado = cotizacionHelper.enviarContratoPorCorreo(cotizacion.getId());

            if (enviado) {
                context.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Éxito",
                        "El contrato ha sido enviado al correo del cliente para el Folio #" + cotizacion.getId()
                ));
            } else {
                context.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Error",
                        "No se pudo enviar el contrato."
                ));
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al enviar", "No se pudo enviar el contrato: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    //Setters/Getters
    public void buscarPorId(){ cotizaciones = delegateCotizacion.buscarPorId(idBusqueda); }

    public int getIdBusqueda() { return idBusqueda; }
    public void setIdBusqueda(int idBusqueda) { this.idBusqueda = idBusqueda; }

    public void consultarPorFecha(){ cotizaciones = delegateCotizacion.obtenerTodosPorFecha(); }

    public void consultarPorFolio(){ cotizaciones = delegateCotizacion.obtenerPorFolio(); }

    public void consultarPorVendedor(){ cotizaciones = delegateCotizacion.obtenerPorVendedor(); }

    public void obtenerPorAnio(){ cotizaciones = delegateCotizacion.obtenerPorAnio(anioFiltro); }

    public void obtenerPorMes(){ cotizaciones = delegateCotizacion.obtenerPorMes(mesFiltro); }

    public List<Cotizacion> getCotizaciones() { return cotizaciones; }

    public int getAnioFiltro() { return anioFiltro; }
    public void setAnioFiltro(int anioFiltro) { this.anioFiltro = anioFiltro; }

    public List<Integer> getAniosDisponibles() { return aniosDisponibles; }

    public int getMesFiltro() { return mesFiltro; }
    public void  setMesFiltro(int mesFiltro) { this.mesFiltro = mesFiltro; }

    public List<Integer> getMesesDisponibles() { return mesesDisponibles; }

    //setters / getters para aprobacion (marinela)

    public boolean isDialogAprobacionVisible() { return dialogAprobacionVisible; }
    public void setDialogAprobacionVisible(boolean dialogAprobacionVisible) { this.dialogAprobacionVisible = dialogAprobacionVisible; }

    public String getTextoConfirmacion() { return textoConfirmacion; }
    public void setTextoConfirmacion(String textConfirmacion) { this.textoConfirmacion = textConfirmacion; }

    public Integer getIdCotizacionSeleccionada() { return idCotizacionSeleccionada; }
    public void  setIdCotizacionSeleccionada(Integer idCotizacionSeleccionada) { this.idCotizacionSeleccionada = idCotizacionSeleccionada; }
}