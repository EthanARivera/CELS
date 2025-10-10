package imf.cels.delegate;

import imf.cels.entity.Material;
import imf.cels.integration.ServiceLocator;
import java.util.List;

public class DelegateMaterial {

    public void guardarMaterial(Material material) {
        ServiceLocator.getInstanceMaterialDAO().save(material);
    }

    public List<Material> listarMateriales() {
        return ServiceLocator.getInstanceMaterialDAO().obtenerTodos();
    }
}
