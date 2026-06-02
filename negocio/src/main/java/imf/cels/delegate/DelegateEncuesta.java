package imf.cels.delegate;

import imf.cels.entity.DatosEncuesta;
import imf.cels.facade.FacadeEncuesta;
import imf.cels.integration.ServiceFacadeLocator;
import jakarta.decorator.Delegate;

import java.util.List;

public class DelegateEncuesta {

    private FacadeEncuesta getFacade() {
        return ServiceFacadeLocator.getInstanceFacadeEncuesta();
    }

    public void registrarEncuesta(DatosEncuesta encuesta) {
        getFacade().registrarEncuesta(encuesta);
    }

    // Estadistica
    public List<Object[]> contarQ1() {
        return getFacade().contarQ1();
    }

    public List<Object[]> contarQ2() {
        return getFacade().contarQ2();
    }

    public List<Object[]> contarQ3() {
        return getFacade().contarQ3();
    }

    public List<Object[]> contarQ4() {
        return getFacade().contarQ4();
    }

    public List<String> obtenerComentarios() {
        return getFacade().obtenerComentarios();
    }

}