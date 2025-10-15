package ui;

import helper.MaterialHelper;
import imf.cels.entity.Material;
import imf.cels.entity.TipoUnidad;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;

@Named("materialUI")
@SessionScoped
public class MaterialUI implements Serializable {
    private final MaterialHelper helper = new MaterialHelper();

    private Material material = new Material();

    public MaterialUI() {}

    // Devuelve todas las opciones del enum para el menú
    public TipoUnidad[] getTiposUnidad() {
        return TipoUnidad.values();
    }

    public void guardar() {
        try{
            // Validaciones
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
        // Guarda correctamente usando el Converter
        helper.saveMaterial(material.getNombre(), material.getTipoMaterial(),
                material.getCosto(), material.getTipoUnidad());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Éxito",
                            "Material guardado correctamente."));

            // Limpia el formulario
            material = new Material();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al guardar", e.getMessage()));
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
}
