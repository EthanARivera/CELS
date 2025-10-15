package imf.cels.integration;

import imf.cels.facade.FacadeMaterial;
import imf.cels.facade.FacadeUsuario;

public class ServiceFacadeLocator {

    private static FacadeUsuario facadeUsuario;
    private static FacadeMaterial facadeMaterial;


    public static FacadeUsuario getInstanceFacadeUsuario() {
        if (facadeUsuario == null) {
            facadeUsuario = new FacadeUsuario();
            return facadeUsuario;
        } else {
            return facadeUsuario;
        }
    }
    //Material
    public static FacadeMaterial getInstanceFacadeMaterial() {
        if (facadeMaterial == null) {
            facadeMaterial = new FacadeMaterial();
            return facadeMaterial;
        } else {
            return facadeMaterial;
        }
    }
}