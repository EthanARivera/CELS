package ui;

import helper.EncuestaHelper;
import imf.cels.entity.DatosEncuesta;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("encuestaBeanUI")
@ViewScoped
public class EncuestaBeanUI implements Serializable {

    private DatosEncuesta encuesta;
    private String respuestaTiempo;
    private List<String> opcionesTiempo;
    private EncuestaHelper encuestaHelper;

    @PostConstruct
    public void init() {
        encuestaHelper = new EncuestaHelper();
        encuesta = new DatosEncuesta();
        respuestaTiempo = null;
        opcionesTiempo = new java.util.ArrayList<>();
        opcionesTiempo.add("si");
        opcionesTiempo.add("no");
    }

    public List<String> getOpcionesTiempo() { return opcionesTiempo; }

    public void onRatingChange() {
        // actualiza el valor en el Bean via ajax
    }

    public void limpiar() {
        encuesta = new DatosEncuesta();
        respuestaTiempo = null;
    }

    public void guardarTiempo() {
        // para guardar el valor de la primera pregunta
    }

    public void enviarEncuesta() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        System.out.println("DrespuestaTiempo: '" + respuestaTiempo + "'");
        System.out.println("q2: " + encuesta.getQ2());
        System.out.println("q3: " + encuesta.getQ3());
        System.out.println("q4: " + encuesta.getQ4());

        if (respuestaTiempo == null || respuestaTiempo.isEmpty()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo requerido", "Debes responder la pregunta de tiempo de entrega."));
            ctx.validationFailed();
            return;
        }
        if (encuesta.getQ2() == null || encuesta.getQ2() == 0) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo requerido", "Debes calificar la atención al cliente."));
            ctx.validationFailed();
            return;
        }
        if (encuesta.getQ3() == null || encuesta.getQ3() == 0) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo requerido", "Debes calificar la calidad del producto."));
            ctx.validationFailed();
            return;
        }
        if (encuesta.getQ4() == null || encuesta.getQ4() == 0) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo requerido", "Debes indicar la probabilidad de recomendación."));
            ctx.validationFailed();
            return;
        }

        encuesta.setQ1(respuestaTiempo.equals("si"));

        try {
            encuestaHelper.registrarEncuesta(encuesta);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "¡Gracias!", "Tu encuesta fue registrada correctamente."));
            limpiar();
        } catch (Exception e) {
            e.printStackTrace();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo guardar la encuesta. Intenta de nuevo."));
        }
    }

    // Getters y Setters
    public DatosEncuesta getEncuesta() { return encuesta; }
    public void setEncuesta(DatosEncuesta encuesta) { this.encuesta = encuesta; }

    public String getRespuestaTiempo() { return respuestaTiempo; }
    public void setRespuestaTiempo(String respuestaTiempo) { this.respuestaTiempo = respuestaTiempo; }
}