package helper;

import imf.cels.entity.Material;
import imf.cels.entity.TipoUnidad;
import imf.cels.integration.ServiceFacadeLocator;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.List;

public class MaterialHelper implements Serializable {

    public void guardarMaterial(String nombre, String tipo, BigDecimal costo, TipoUnidad unidad) {
        Material m = new Material();
        m.setNombre(nombre);
        m.setTipoMaterial(tipo);
        m.setCosto(costo);
        m.setTipoUnidad(unidad);

        ServiceFacadeLocator.getInstanceFacadeMaterial().guardarMaterial(m);
    }

    public List<Material> listarMateriales() {
        return ServiceFacadeLocator.getInstanceFacadeMaterial().listarMateriales();
    }
}
