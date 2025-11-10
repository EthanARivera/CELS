package helper;

import imf.cels.entity.Cotizacion;
import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.entity.CotizacionMaterial;
import imf.cels.integration.ServiceFacadeLocator;

public class CotizacionHelper {

    public void saveCotizacion(Cotizacion cotizacion) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacion(cotizacion);
    }

    //No creo que necesite estos, pero los dejaré por si las dudas
    /*public void saveCotizacionMaterial(CotizacionMaterial cotizacionMaterial) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacionMaterial(cotizacionMaterial);
    }

    public void saveCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacionManoDeObra(cotizacionManoDeObra);
    }*/

    public void enviarCotizacionPorCorreo(Integer idCotizacion) throws Exception {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().enviarCotizacionPorCorreo(idCotizacion);
    }

}
