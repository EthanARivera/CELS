package imf.cels.delegate;

import imf.cels.entity.DatosEncuesta;
import imf.cels.facade.FacadeEncuesta;
import imf.cels.integration.ServiceFacadeLocator;

public class DelegateEncuesta {

    private FacadeEncuesta getFacade() {
        return ServiceFacadeLocator.getInstanceFacadeEncuesta();
    }

    public void registrarEncuesta(DatosEncuesta encuesta) {
        getFacade().registrarEncuesta(encuesta);
    }
}