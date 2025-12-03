package ui;

import helper.CotizacionHelper;
import helper.PedidosHelper;
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

    @Inject
    private PedidosHelper pedidosHelper;

    @Inject
    private PedidosBeanUI pedidosUI;

    private CotizacionHelper cotizacionHelper;

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

    // Aprobación de Contrato
    private boolean dialogAprobacionContratoVisible;
    private String textoConfirmacionContrato;
    private Integer idContratoSeleccionado;


    @PostConstruct
    public void init(){
        try {
            loginUI.verificarSesion();
            System.out.println(loginUI.getUsuario().getNombre());
        } catch (Exception e) {
            e.printStackTrace();
        }

        cotizacionHelper = new CotizacionHelper();

        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        if (viewId.contains("gestion_pedidos_productor")) {
            cargarPedidos();
            return;
        }

        if (viewId.contains("gestion_pedidos_vendedor")) {
            cargarPedidosVendedor();
            return;
        }

        if (viewId.contains("gestion_pedidos") && loginUI.getUsuario().getCodigoTipoUsuario() == 0) {
            cargarPedidosGerente();
            return;
        }

        if (loginUI.getUsuario().getCodigoTipoUsuario() == 0) {
            cotizaciones = cotizacionHelper.obtenerTodosPorFecha();
            aniosDisponibles = cotizacionHelper.obtenerAniosDisponibles();
            mesesDisponibles = cotizacionHelper.obtenerMesesDisponibles();
        }
        else if (loginUI.getUsuario().getCodigoTipoUsuario() == 1) {
            cotizaciones = cotizacionHelper.obtenerTodosPorFecha(loginUI.getUsuario().getId());
            aniosDisponibles = cotizacionHelper.obtenerAniosDisponibles(loginUI.getUsuario().getId());
            mesesDisponibles = cotizacionHelper.obtenerMesesDisponibles(loginUI.getUsuario().getId());
        }
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
            if (loginUI.getUsuario().getCodigoTipoUsuario() == 0) {
                cotizaciones = cotizacionHelper.obtenerTodosPorFecha();
            }
            else if (loginUI.getUsuario().getCodigoTipoUsuario() == 1) {
                cotizaciones = cotizacionHelper.obtenerTodosPorFecha(loginUI.getUsuario().getId());
            }

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


    // Aprobación de Contrato
    // Mostrar diálogo
    public void mostrarDialogoAprobacionContrato(Cotizacion cotizacion) {
        this.idContratoSeleccionado = cotizacion.getId();
        this.textoConfirmacionContrato = "";
        this.dialogAprobacionContratoVisible = true;
    }

    // Aprobar contrato
    public void aprobarContrato() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            if (textoConfirmacionContrato == null ||
                    !"Aprobado".equalsIgnoreCase(textoConfirmacionContrato.trim())) {

                context.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Confirmación incorrecta",
                        "Debe escribir exactamente 'Aprobado' para confirmar."
                ));
                return;
            }

            cotizacionHelper.aprobarContrato(idContratoSeleccionado);
            dialogAprobacionContratoVisible = false;

            // actualizar tabla
            if (loginUI.getUsuario().getCodigoTipoUsuario() == 0) {
                cotizaciones = cotizacionHelper.obtenerTodosPorFecha();
            }
            else if (loginUI.getUsuario().getCodigoTipoUsuario() == 1) {
                cotizaciones = cotizacionHelper.obtenerTodosPorFecha(loginUI.getUsuario().getId());
            }

            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Contrato aprobado",
                    "El contrato del folio " + idContratoSeleccionado + " fue aprobado correctamente."
            ));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error al aprobar contrato",
                    e.getMessage()
            ));
        }
    }

    public void cancelarAprobacionContrato() {
        dialogAprobacionContratoVisible = false;
        textoConfirmacionContrato = "";
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

        if (!cotizacion.getisCotizacionAprobada()) {
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
    public void buscarPorId(){ cotizaciones = cotizacionHelper.buscarPorId(idBusqueda); }

    public int getIdBusqueda() { return idBusqueda; }
    public void setIdBusqueda(int idBusqueda) { this.idBusqueda = idBusqueda; }

    public void consultarPorFecha()
    {
        if (loginUI.getUsuario().getCodigoTipoUsuario() == 0) {
        cotizaciones = cotizacionHelper.obtenerTodosPorFecha();
        }
        else if (loginUI.getUsuario().getCodigoTipoUsuario() == 1) {
            cotizaciones = cotizacionHelper.obtenerTodosPorFecha(loginUI.getUsuario().getId());
        }
    }

    public void consultarPorFolio(){
        if (loginUI.getUsuario().getCodigoTipoUsuario() == 0) {
            cotizaciones = cotizacionHelper.obtenerPorFolio();
        }
        else if (loginUI.getUsuario().getCodigoTipoUsuario() == 1) {
            cotizaciones = cotizacionHelper.obtenerPorFolio(loginUI.getUsuario().getId());
        }
    }

    public void consultarPorVendedor(){ cotizaciones = cotizacionHelper.obtenerPorVendedor(); }

    public void obtenerPorAnio(){ if (loginUI.getUsuario().getCodigoTipoUsuario() == 0) {
        cotizaciones = cotizacionHelper.obtenerPorAnio(anioFiltro);
    }
    else if (loginUI.getUsuario().getCodigoTipoUsuario() == 1) {
        cotizaciones = cotizacionHelper.obtenerPorAnio(loginUI.getUsuario().getId(), anioFiltro);
    } }

    public void obtenerPorMes(){
        if (loginUI.getUsuario().getCodigoTipoUsuario() == 0) {
            cotizaciones = cotizacionHelper.obtenerPorMes(mesFiltro);
        }
        else if (loginUI.getUsuario().getCodigoTipoUsuario() == 1) {
            cotizaciones = cotizacionHelper.obtenerPorMes(loginUI.getUsuario().getId(), mesFiltro);
        }
    }

    public void seleccionarCotizacion(Cotizacion c) {
        this.cotizacionSeleccionada = c;
    }

    public void cargarPedidos() {
        cotizaciones = cotizacionHelper.obtenerCotizacionesConPedido();
    }

    public void cargarPedidosVendedor() {
        cotizaciones = cotizacionHelper.obtenerCotizacionesContratoAprobado(loginUI.getUsuario().getId());
    }

    public void cargarPedidosGerente() {
        cotizaciones = cotizacionHelper.obtenerCotizacionesContratoAprobado();
    }

    public void darDeAltaPedido() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            pedidosHelper.darDeAltaPedido(cotizacionSeleccionada, cotizacionSeleccionada.getPrioridadSeleccionada());

            cargarPedidos();

            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Pedido dado de alta", "Ahora es visible para el productor."
            ));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error al dar de alta", e.getMessage()
            ));
        }
    }

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


    public boolean isDialogAprobacionContratoVisible() { return dialogAprobacionContratoVisible; }
    public void setDialogAprobacionContratoVisible(boolean v) { this.dialogAprobacionContratoVisible = v; }

    public String getTextoConfirmacionContrato() { return textoConfirmacionContrato; }
    public void setTextoConfirmacionContrato(String t) { this.textoConfirmacionContrato = t; }

    public Integer getIdContratoSeleccionado() { return idContratoSeleccionado; }
    public void setIdContratoSeleccionado(Integer idContratoSeleccionado) { this.idContratoSeleccionado = idContratoSeleccionado; }

}