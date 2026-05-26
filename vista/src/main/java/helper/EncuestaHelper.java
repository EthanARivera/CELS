package helper;

import imf.cels.delegate.DelegateEncuesta;
import imf.cels.entity.DatosEncuesta;

public class EncuestaHelper {

    private final DelegateEncuesta delegateEncuesta;

    public EncuestaHelper() {
        this.delegateEncuesta = new DelegateEncuesta();
    }

    public void registrarEncuesta(DatosEncuesta encuesta) {
        delegateEncuesta.registrarEncuesta(encuesta);
    }
}