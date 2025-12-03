package ui;

import helper.PedidosHelper;
import imf.cels.entity.Cotizacion;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named("pedidosUI")
@ViewScoped
public class PedidosBeanUI implements Serializable {
    private List<String> prioridades = Arrays.asList("Baja", "Media", "Alta", "Urgente");
    private List<String> estados = Arrays.asList("Por iniciar", "En progreso", "Terminado", "Con impedimentos");
    private String prioridadSeleccionada;
    private String estadoSeleccionado;
    private String textoConfirmacion;
    private Cotizacion cotizacionSeleccionada;
    private PedidosHelper pedidosHelper = new PedidosHelper();

    public List<String> getPrioridades() {
        return prioridades;
    }

    public List<String> getEstados() {
        return estados;
    }

    public String getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(String estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public String getPrioridadSeleccionada() {
        return prioridadSeleccionada;
    }

    public void setPrioridadSeleccionada(String prioridadSeleccionada) {
        this.prioridadSeleccionada = prioridadSeleccionada;
    }

    public void validarEstado() {
        boolean abrirDialogo = "Terminado".equalsIgnoreCase(estadoSeleccionado);
        org.primefaces.PrimeFaces.current().ajax().addCallbackParam("mostrarDialogo", abrirDialogo);
    }

    public String getTextoConfirmacion() {
        return textoConfirmacion;
    }

    public void setTextoConfirmacion(String textoConfirmacion) {
        this.textoConfirmacion = textoConfirmacion;
    }

    public void seleccionarCotizacion(Cotizacion c){
        this.cotizacionSeleccionada = c;
    }

    public void darDeAltaPedido() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            pedidosHelper.darDeAltaPedido(cotizacionSeleccionada, prioridadSeleccionada);

            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Pedido dado de alta",
                    "Ahora es visible para el productor."
            ));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error al dar de alta",
                    e.getMessage()
            ));
        }
    }
}