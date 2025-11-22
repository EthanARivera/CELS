package imf.cels.facade;

import imf.cels.delegate.DelegateMaterial;
import imf.cels.entity.Material;

import java.util.List;

public class FacadeMaterial {

    private final DelegateMaterial delegateMaterial;

    public FacadeMaterial() {
        this.delegateMaterial = new DelegateMaterial();
    }

    public void saveMaterial(Material material){
        delegateMaterial.saveMaterial(material);
    }

    public List<Material> obtenerMateriales() {
        return delegateMaterial.obtenerMateriales();
    }

    public List<Material> obtenerPorNombre(String nombre){
        return delegateMaterial.obtenerPorNombre(nombre);
    }

    public List<Material> obtenerPorNombreCot(String nombre){
        return delegateMaterial.obtenerPorNombreCot(nombre);
    }

    public Material obtenerPorId(Integer id){
        return delegateMaterial.obtenerPorId(id);
    }

    public void modificarMaterial(Material material){
        delegateMaterial.actualizarMaterial(material);
    }

    public void eliminarMaterial(Integer id){
        delegateMaterial.eliminarMaterial(id);
    }
}