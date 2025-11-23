package converter;

import helper.MaterialHelper;
import imf.cels.entity.Material;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "materialConverter", managed = true)
public class MaterialConverter implements Converter<Material> {

    @Inject
    private MaterialHelper materialHelper;

    @Override
    public Material getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return materialHelper.buscarPorId(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Material material) {
        return (material != null && material.getId() != null)
                ? material.getId().toString() : "";
    }
}