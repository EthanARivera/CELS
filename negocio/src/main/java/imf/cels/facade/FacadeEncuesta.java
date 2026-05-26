package imf.cels.facade;

import imf.cels.entity.DatosEncuesta;
import imf.cels.integration.ServiceLocator;

public class FacadeEncuesta {

    public void registrarEncuesta(DatosEncuesta encuesta) {
        ServiceLocator.getInstanceEncuestaDAO().registrarEncuesta(encuesta);
    }
}