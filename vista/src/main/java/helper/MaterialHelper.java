package helper;

import imf.cels.integration.ServiceFacadeLocator;
import imf.cels.entity.Material;
import java.util.List;

public class MaterialHelper {

    public List<Material> obtenerMateriales() {
        return ServiceFacadeLocator.getInstanceFacadeMaterial().obtenerMateriales();
    }

    public List<Material> obtenerPorNombre(String nombre){
        return ServiceFacadeLocator.getInstanceFacadeMaterial().obtenerPorNombre(nombre);
    }

    public Material obtenerPorId(Integer id){
        return ServiceFacadeLocator.getInstanceFacadeMaterial().obtenerPorId(id);
    }

    public void modificar(Material material){
        ServiceFacadeLocator.getInstanceFacadeMaterial().modificarMaterial(material);
    }

    public void eliminar(Integer id){
        ServiceFacadeLocator.getInstanceFacadeMaterial().eliminarMaterial(id);
    }
}
