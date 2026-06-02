package ui;

import helper.EncuestaHelper;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("estadisticasBeanUI")
@ViewScoped
public class EstadisticasBeanUI implements Serializable {

    private EncuestaHelper helper;

    // =========================
    // DATA
    // =========================

    private List<String> q1Labels;
    private List<Number> q1Values;

    private List<String> q2Labels;
    private List<Number> q2Values;

    private List<String> q3Labels;
    private List<Number> q3Values;

    private List<String> q4Labels;
    private List<Number> q4Values;

    private List<String> comentarios;

    @PostConstruct
    public void init() {

        helper = new EncuestaHelper();

        cargarQ1();
        cargarQ2();
        cargarQ3();
        cargarQ4();

        comentarios = helper.obtenerComentarios();
    }

    // =========================
    // Q1
    // =========================

    private void cargarQ1() {

        q1Labels = new ArrayList<>();
        q1Values = new ArrayList<>();

        for (Object[] row : helper.contarQ1()) {

            Boolean respuesta = (Boolean) row[0];
            Long total = (Long) row[1];

            q1Labels.add(respuesta ? "Sí" : "No");
            q1Values.add(total);
        }
    }

    // =========================
    // Q2
    // =========================

    private void cargarQ2() {

        q2Labels = new ArrayList<>();
        q2Values = new ArrayList<>();

        for (Object[] row : helper.contarQ2()) {

            Integer estrellas = (Integer) row[0];
            Long total = (Long) row[1];

            q2Labels.add(estrellas + " estrellas");
            q2Values.add(total);
        }
    }

    // =========================
    // Q3
    // =========================

    private void cargarQ3() {

        q3Labels = new ArrayList<>();
        q3Values = new ArrayList<>();

        for (Object[] row : helper.contarQ3()) {

            Integer estrellas = (Integer) row[0];
            Long total = (Long) row[1];

            q3Labels.add(estrellas + " estrellas");
            q3Values.add(total);
        }
    }

    // =========================
    // Q4
    // =========================

    private void cargarQ4() {

        q4Labels = new ArrayList<>();
        q4Values = new ArrayList<>();

        for (Object[] row : helper.contarQ4()) {

            Integer estrellas = (Integer) row[0];
            Long total = (Long) row[1];

            q4Labels.add(estrellas + " estrellas");
            q4Values.add(total);
        }
    }

    // =========================
    // COMMENTS
    // =========================

    public List<String> getComentarios() {
        return comentarios;
    }

    // =========================
    // JSON GETTERS FOR CHART.JS
    // =========================

    public String getQ1Labels() {
        return convertToJsonArray(q1Labels);
    }

    public String getQ1Values() {
        return q1Values.toString();
    }

    public String getQ2Labels() {
        return convertToJsonArray(q2Labels);
    }

    public String getQ2Values() {
        return q2Values.toString();
    }

    public String getQ3Labels() {
        return convertToJsonArray(q3Labels);
    }

    public String getQ3Values() {
        return q3Values.toString();
    }

    public String getQ4Labels() {
        return convertToJsonArray(q4Labels);
    }

    public String getQ4Values() {
        return q4Values.toString();
    }

    // =========================
    // UTILITY
    // =========================

    private String convertToJsonArray(List<String> list) {

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < list.size(); i++) {
            sb.append("\"")
                    .append(list.get(i))
                    .append("\"");
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }

        sb.append("]");
        return sb.toString();
    }


    public int getTotalRespuestas() {
        return q1Values.stream().mapToInt(Number::intValue).sum();
    }

    public int getPorcentajeSi() {
        int total = getTotalRespuestas();
        if (total == 0 || q1Values.isEmpty()) return 0;
        return Math.round(q1Values.get(0).intValue() * 100f / total); // assumes index 0 = "Sí"
    }

    public String getPromedioQ2() {
        return calcularPromedio(q2Labels, q2Values);
    }

    public String getPromedioQ3() {
        return calcularPromedio(q3Labels, q3Values);
    }

    private String calcularPromedio(List<String> labels, List<Number> values) {
        long total = 0, suma = 0;
        for (int i = 0; i < values.size(); i++) {
            int estrellas = i + 1;
            long count = values.get(i).longValue();
            suma += estrellas * count;
            total += count;
        }
        if (total == 0) return "—";
        return String.format("%.1f", (double) suma / total);
    }
}