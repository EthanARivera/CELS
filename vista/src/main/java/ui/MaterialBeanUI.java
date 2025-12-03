package ui;

import helper.MaterialHelper;
import imf.cels.entity.Material;
import imf.cels.entity.TipoUnidad;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named("materialUI")
@SessionScoped //@ViewScoped
public class MaterialBeanUI implements Serializable {

    private final MaterialHelper helper = new MaterialHelper();
    private Material material = new Material();
    private List<Material> materiales;       // Lista completa
    private List<Material> listaFiltrada;  // Lista que se muestra (filtrada)

    private Material materialSeleccionado;

    private String nombreBusqueda = "";
    private Integer idBusqueda;

    public MaterialBeanUI() {
    }


    // Devuelve todas las opciones del enum para el menú
    public TipoUnidad[] getTiposUnidad() {
        return TipoUnidad.values();
    }

    public void guardar() {
        try {
            // Validaciones de campos vacios
            if (material.getNombre() == null || material.getNombre().isBlank() ||
                    material.getTipoMaterial() == null || material.getTipoMaterial().isBlank() ||
                    material.getCosto() == null || material.getCosto().compareTo(BigDecimal.ZERO) <= 0 ||
                    material.getTipoUnidad() == null) {

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Datos inválidos",
                                "Por favor complete todos los campos correctamente."));
                return;
            }

            //Validación para duplicados
            String nombreLimpios = material.getNombre().trim();
            List<Material> existentes = helper.buscarPorNombre(nombreLimpios);

            if (existentes != null && !existentes.isEmpty()) {
                for (Material m : existentes) {
                    // Comparamos ignorando mayúsculas/minúsculas
                    if (m.getNombre().equalsIgnoreCase(nombreLimpios)) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Material duplicado:",
                                        "El material '" + nombreLimpios + "' ya existe en el sistema."));

                        FacesContext.getCurrentInstance().validationFailed(); //Para que no se redireccione si hay duplicado
                        return;
                    }
                }
            }

            // Guarda correctamente usando el Converter
            helper.saveMaterial(material.getNombre(), material.getTipoMaterial(),
                    material.getCosto(), material.getTipoUnidad());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Éxito:",
                            "Material guardado correctamente."));

            // Limpia el formulario
            material = new Material();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al guardar", e.getMessage()));

            // Si hay una excepción técnica, tampoco queremos que redireccione
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public void limpiarFormulario() {
        material = new Material(); // reinicia el objeto del formulario
    }


    // Getters y setters
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    //CONSULTAS DE MATERIALES//
    @PostConstruct
    public void init() {
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
