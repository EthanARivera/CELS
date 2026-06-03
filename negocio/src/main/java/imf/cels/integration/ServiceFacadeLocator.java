package imf.cels.integration;

import imf.cels.facade.FacadeEncuesta;
import imf.cels.facade.FacadeMaterial;
import imf.cels.facade.FacadeCotizacion;
import imf.cels.facade.FacadeUsuario;

public class ServiceFacadeLocator {

    private static FacadeUsuario facadeUsuario;
    private static FacadeMaterial facadeMaterial;
    private static FacadeCotizacion facadeCotizacion;
    private static FacadeEncuesta facadeEncuesta;

    public static FacadeUsuario getInstanceFacadeUsuario() {
        if (facadeUsuario == null) {
            facadeUsuario = new FacadeUsuario();
            return facadeUsuario;
        } else {
            return facadeUsuario;
        }
    }

    public static FacadeMaterial getInstanceFacadeMaterial() {
        if (facadeMaterial == null) {
            facadeMaterial = new FacadeMaterial();
            return facadeMaterial;
        } else {
            return facadeMaterial;
        }
    }

    public static FacadeCotizacion getInstanceFacadeCotizacion() {
        if (facadeCotizacion == null) {
            facadeCotizacion = new FacadeCotizacion();
        }
        return facadeCotizacion;
    }

    public static FacadeEncuesta getInstanceFacadeEncuesta() {
        if (facadeEncuesta == null) {
            facadeEncuesta = new FacadeEncuesta();
        }
        return facadeEncuesta;
    }

}
