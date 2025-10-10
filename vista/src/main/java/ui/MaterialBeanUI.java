package ui;

import imf.cels.entity.Material;
import imf.cels.entity.TipoUnidad;
import imf.cels.integration.ServiceFacadeLocator;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;

@Named("materialUI")
@SessionScoped
public class MaterialBeanUI implements Serializable {

    private Material material = new Material();

    public MaterialBeanUI() {}

    // Devuelve todas las opciones del enum para el menú
    public TipoUnidad[] getTiposUnidad() {
        return TipoUnidad.values();
    }

    public void guardar() {
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

        try {
            // 💡 Conversión manual usando el metodo del enum
            TipoUnidad tipo = material.getTipoUnidad();
            material.setTipoUnidad(TipoUnidad.fromLabel(tipo.getLabel()));

            // Guardar con la capa de negocio
            ServiceFacadeLocator.getInstanceFacadeMaterial().guardarMaterial(material);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Éxito",
                            "Material guardado correctamente."));

            // Limpiar el formulario
            material = new Material();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al guardar",
                            e.getMessage()));
        }
    }


    // Getters y setters
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
