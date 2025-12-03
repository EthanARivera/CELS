package ui;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named("pedidosUI")
@ViewScoped
public class PedidosBeanUI implements Serializable {
    private List<String> prioridades = Arrays.asList("Baja", "Media", "Alta", "Urgente");
    private List<String> estados = Arrays.asList("Por empezar", "En progreso", "Terminado", "Con impedimentos");
    private String estadoSeleccionado;

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

    public void validarEstado() {
        boolean abrirDialogo = "Terminado".equalsIgnoreCase(estadoSeleccionado);
        org.primefaces.PrimeFaces.current().ajax().addCallbackParam("mostrarDialogo", abrirDialogo);
    }
}