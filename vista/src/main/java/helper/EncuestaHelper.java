package helper;

import imf.cels.delegate.DelegateEncuesta;
import imf.cels.entity.DatosEncuesta;

import java.util.List;


public class EncuestaHelper {

    private final DelegateEncuesta delegateEncuesta;

    public EncuestaHelper() {
        this.delegateEncuesta = new DelegateEncuesta();
    }

    public void registrarEncuesta(DatosEncuesta encuesta) {
        delegateEncuesta.registrarEncuesta(encuesta);
    }

    // Estadistica
    public List<Object[]> contarQ1(){
        return delegateEncuesta.contarQ1();
    }

    public List<Object[]> contarQ2(){
        return delegateEncuesta.contarQ2();
    }

    public List<Object[]> contarQ3(){
        return delegateEncuesta.contarQ3();
    }

    public List<Object[]> contarQ4(){
        return delegateEncuesta.contarQ4();
    }

    public List<String> obtenerComentarios(){
        return delegateEncuesta.obtenerComentarios();
    }
}