package imf.cels.delegate;

import imf.cels.entity.Material;
import imf.cels.integration.ServiceLocator;

import java.util.List;

public class DelegateMaterial {

    public void saveMaterial(Material material){
        ServiceLocator.getInstanceMaterialDAO().save(material);
    }

    public List<Material> obtenerMateriales() {
        return ServiceLocator.getInstanceMaterialDAO().findAll();
    }

    public Material obtenerPorId(Integer id) {
        return ServiceLocator.getInstanceMaterialDAO().findById(id);
    }

    public List<Material> obtenerPorNombre(String nombre) {
        return ServiceLocator.getInstanceMaterialDAO().findByName(nombre);
    }

    public void actualizarMaterial(Material material){
        ServiceLocator.getInstanceMaterialDAO().update(material);
    }
}

