package ui;

import helper.UsuarioHelper;
import imf.cels.delegate.DelegateUsuario;
import imf.cels.entity.UsDatosSensible;
import imf.cels.entity.UsPsswd;
import imf.cels.entity.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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

    private final DelegateUsuario delegate = new DelegateUsuario(); // p/modify

    private UsDatosSensible usDatosSensible;
    private UsPsswd usPsswd;

    @PostConstruct
    public void init() {
        helper = new UsuarioHelper();
        usuarios = helper.obtenerUsuarios();
        if (usuarios == null) usuarios = new ArrayList<Usuario>();
        ordenarPorNombre();
        listaFiltrada = new ArrayList<>(usuarios);
        usuarioSeleccionado = new Usuario();
        usuarioSeleccionado.setUsDatosSensible(new UsDatosSensible());
        usuarioSeleccionado.setUsPsswd(new UsPsswd());
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

    public List<Usuario> getListaFiltrada() {
        if (listaFiltrada == null) {
            listaFiltrada = new ArrayList<Usuario>(usuarios);
        }
        return listaFiltrada;
    }
    // --- Getters y Setters ---

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;

        if (this.usuarioSeleccionado.getUsDatosSensible() == null)
            this.usuarioSeleccionado.setUsDatosSensible(new UsDatosSensible());

        if (this.usuarioSeleccionado.getUsPsswd() == null)
            this.usuarioSeleccionado.setUsPsswd(new UsPsswd());
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


    // Modificacion
    public void guardarCambios() {
        System.out.println("Entrada al metodo de bean guardarcambios");
        if (usuarioSeleccionado != null && usuarioSeleccionado.getId() != null) {
            System.out.println("usuarioSeleccionado no es null");
            try {
                delegate.modificarCorreoYContrasena(usuarioSeleccionado); //llamar delegate
                recargarUsuarios();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Éxito", "Correo y contraseña actualizados correctamente"));
                System.out.println("Correo y contraseña actualizados correctamente");
            } catch (IllegalArgumentException ex) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
                System.out.println("Error: " + ex);
            }
        }
    }

    public void cancelarEdicion() {
        recargarUsuarios(); // restaurar lista
        usuarioSeleccionado = new Usuario();
        usuarioSeleccionado.setUsDatosSensible(new UsDatosSensible());
        usuarioSeleccionado.setUsPsswd(new UsPsswd());
    }

    // Activacion/Desactivacion

    public void prepararCambioEstado(Usuario usuario) {
        this.usuarioSeleccionado = usuario;
    }

    // Cambiar estado y feedback
    public void cambiarEstadoUsuario() {
        if (usuarioSeleccionado != null && usuarioSeleccionado.getId() != null) {
            boolean nuevoEstado = !usuarioSeleccionado.getEstado();
            boolean exito = helper.cambiarEstadoUsuario(usuarioSeleccionado.getId(), nuevoEstado);

            if (exito) {
                usuarioSeleccionado.setEstado(nuevoEstado);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",
                                "Usuario " + usuarioSeleccionado.getNombre() + " actualizado correctamente"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "No se pudo actualizar el estado del usuario"));
            }
        }
    }

}
