package imf.cels.facade;

import imf.cels.entity.DatosEncuesta;
import imf.cels.integration.ServiceLocator;

import java.util.List;

public class FacadeEncuesta {

    public void registrarEncuesta(DatosEncuesta encuesta) {
        ServiceLocator.getInstanceEncuestaDAO().registrarEncuesta(encuesta);
    }

    // Estadistica
    public List<Object[]> contarQ1() {
        return ServiceLocator.getInstanceEncuestaDAO().contarQ1();
    }

    public List<Object[]> contarQ2() {
        return ServiceLocator.getInstanceEncuestaDAO().contarQ2();
    }

    public List<Object[]> contarQ3() {
        return ServiceLocator.getInstanceEncuestaDAO().contarQ3();
    }

    public List<Object[]> contarQ4() {
        return ServiceLocator.getInstanceEncuestaDAO().contarQ4();
    }

    public List<String> obtenerComentarios() {
        return ServiceLocator.getInstanceEncuestaDAO().obtenerComentarios();
    }
}