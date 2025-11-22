package helper;

import imf.cels.entity.Cotizacion;
import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.entity.CotizacionMaterial;
import imf.cels.entity.Material;
import imf.cels.integration.ServiceFacadeLocator;

import java.util.ArrayList;
import java.util.List;

public class CotizacionHelper {

    public void saveCotizacion(Cotizacion cotizacion) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacion(cotizacion);
    }

    public void saveCotizacionMaterial(CotizacionMaterial cotizacionMaterial) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacionMaterial(cotizacionMaterial);
    }

    public void saveCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacionManoDeObra(cotizacionManoDeObra);
    }

    public void enviarEmail() { ServiceFacadeLocator.getInstanceFacadeCotizacion().enviarEmail(); }

    public String obtenerCorreo(Integer idUsuario) {
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerCorreo(idUsuario); }

    public Integer obtenerIdUsuario(Integer idUsuario) {
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerIdUsuario(idUsuario); }

    public String obtenerNombre(Integer idUsuario) {
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerNombre(idUsuario);
    }

    public Integer ultimoFolio() { return ServiceFacadeLocator.getInstanceFacadeCotizacion().ultimoFolio(); }

    public List<Material> buscarMateriales(String nombre) {
        List<Material> materiales = ServiceFacadeLocator.getInstanceFacadeMaterial().obtenerMateriales();
        List<Material> materialesAux = new ArrayList<>();

        for(Material material : materiales) {
            if(material.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                materialesAux.add(material);
            }
        }
        return materialesAux;
    }

}
