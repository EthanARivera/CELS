package ui;

import helper.UsuarioHelper;
import imf.cels.entity.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named("usuarioBean")
@ViewScoped
public class UsuarioBeanUI implements Serializable {

    private UsuarioHelper helper;
    private List<Usuario> usuarios;       // Lista completa
    private List<Usuario> listaFiltrada;  // Lista que se muestra (filtrada)

    private Usuario usuarioSeleccionado;

    private String nombreBusqueda = "";
    private Integer idBusqueda;

    @PostConstruct
    public void init() {
        helper = new UsuarioHelper();
        usuarios = helper.obtenerUsuarios();
        if (usuarios == null) usuarios = new ArrayList<Usuario>();
        ordenarPorNombre();
        listaFiltrada = new ArrayList<>(usuarios);
        usuarioSeleccionado = new Usuario();
    }

    //Ordenar por nombre para mantener consistencia visual
    private void ordenarPorNombre() {
        Collections.sort(usuarios, Comparator.comparing
                (u -> (u.getNombre() == null) ? "" : u.getNombre().toLowerCase()
        ));
    }

    //Aplicar filtros por nombre o ID
    public void aplicarFiltro() {
        if (usuarios == null) {
            usuarios = new ArrayList<>();
        }

        ordenarPorNombre();

        if ((nombreBusqueda == null || nombreBusqueda.trim().isEmpty()) &&
                (idBusqueda == null)) {
            listaFiltrada = new ArrayList<>(usuarios);
            return;
        }

        String filtroNombre = (nombreBusqueda != null) ? nombreBusqueda.trim().toLowerCase() : "";
        listaFiltrada = new ArrayList<>();

        for (Usuario u : usuarios) {
            boolean coincide = true;

            if (!filtroNombre.isEmpty() &&
                    (u.getNombre() == null || !u.getNombre().toLowerCase().contains(filtroNombre))) {
                coincide = false;
            }

            if (idBusqueda != null &&
                    (u.getId() == null || !u.getId().equals(idBusqueda))) {
                coincide = false;
            }

            if (coincide) {
                listaFiltrada.add(u);
            }
        }
    }

    // --- Recargar desde la base de datos ---
    public void recargarUsuarios() {
        usuarios = helper.obtenerUsuarios();
        ordenarPorNombre();
        listaFiltrada = new ArrayList<>(usuarios);
        nombreBusqueda = "";
        idBusqueda = null;
    }

    // --- Getters y Setters ---
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Usuario> getListaFiltrada() {
        if (listaFiltrada == null) {
            listaFiltrada = new ArrayList<Usuario>(usuarios);
        }
        return listaFiltrada;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }

    public Integer getIdBusqueda() {
        return idBusqueda;
    }

    public void setIdBusqueda(Integer idBusqueda) {
        this.idBusqueda = idBusqueda;
    }
}
