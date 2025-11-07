package helper;

import imf.cels.entity.Material;
import imf.cels.entity.TipoUnidad;
import imf.cels.integration.ServiceFacadeLocator;
import java.math.BigDecimal;
import java.io.Serializable;

import java.util.List;

public class MaterialHelper implements Serializable {

    public void saveMaterial(String nombre, String tipo, BigDecimal costo, TipoUnidad unidad) {
        Material m = new Material();
        m.setNombre(nombre);
        m.setTipoMaterial(tipo);
        m.setCosto(costo);
        m.setTipoUnidad(unidad);

        // Llamada a la capa de negocio facade
        ServiceFacadeLocator.getInstanceFacadeMaterial().saveMaterial(m);
    }

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
