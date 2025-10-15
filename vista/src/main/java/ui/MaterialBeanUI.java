package ui;

import helper.MaterialHelper;
import imf.cels.entity.Material;
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

@Named("materialBean")
@ViewScoped
public class MaterialBeanUI implements Serializable {

    private MaterialHelper helper;
    private List<Material> materiales;       // Lista completa
    private List<Material> listaFiltrada;  // Lista que se muestra (filtrada)

    private Material materialSeleccionado;

    private String nombreBusqueda = "";
    private Integer idBusqueda;

    @PostConstruct
    public void init() {
        helper = new MaterialHelper();
        materiales = helper.obtenerMateriales();
        if (materiales == null) materiales = new ArrayList<Material>();
        ordenarPorNombre();
        listaFiltrada = new ArrayList<>(materiales);
        materialSeleccionado = new Material();
    }

    //Ordenar por nombre para mantener consistencia visual
    private void ordenarPorNombre() {
        Collections.sort(materiales, Comparator.comparing
                (u -> (u.getNombre() == null) ? "" : u.getNombre().toLowerCase()
                ));
    }

    //Aplicar filtros por nombre o ID
    public void aplicarFiltro() {
        if (materiales == null) {
            materiales = new ArrayList<>();
        }

        ordenarPorNombre();

        if ((nombreBusqueda == null || nombreBusqueda.trim().isEmpty()) &&
                (idBusqueda == null)) {
            listaFiltrada = new ArrayList<>(materiales);
            return;
        }

        String filtroNombre = (nombreBusqueda != null) ? nombreBusqueda.trim().toLowerCase() : "";
        listaFiltrada = new ArrayList<>();

        for (Material m : materiales) {
            boolean coincide = true;

            if (!filtroNombre.isEmpty() &&
                    (m.getNombre() == null || !m.getNombre().toLowerCase().contains(filtroNombre))) {
                coincide = false;
            }

            if (idBusqueda != null &&
                    (m.getId() == null || !m.getId().equals(idBusqueda))) {
                coincide = false;
            }

            if (coincide) {
                listaFiltrada.add(m);
            }
        }
    }

    // --- Recargar desde la base de datos ---
    public void recargarMateriales() {
        materiales = helper.obtenerMateriales();
        ordenarPorNombre();
        listaFiltrada = new ArrayList<>(materiales);
        nombreBusqueda = "";
        idBusqueda = null;
    }

    public void prepararEdicion(Material material){
        this.materialSeleccionado = material;
    }

    public void guardarCambios(){
        try{
            helper.modificar(materialSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Modificación exitosa", "El material fue modificado correctamente"));
            recargarMateriales();
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Modificación fallida", "El material no fue modificado"));

        }
    }

    // --- Getters y Setters ---
    public List<Material> getMateriales() {
        return materiales;
    }

    public List<Material> getListaFiltrada() {
        if (listaFiltrada == null) {
            listaFiltrada = new ArrayList<Material>(materiales);
        }
        return listaFiltrada;
    }

    public Material getMaterialSeleccionado() {
        return materialSeleccionado;
    }

    public void setMaterialSeleccionado(Material materialSeleccionado) {
        this.materialSeleccionado =materialSeleccionado;
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

    public void eliminarMaterial(Material material){
        try{
            helper.eliminar(material.getId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eliminación exitosa", "El material fue elimnado correctamente"));
            recargarMateriales();
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Eliminación fallida", "El material no fue elimnado"));

        }
    }
}
