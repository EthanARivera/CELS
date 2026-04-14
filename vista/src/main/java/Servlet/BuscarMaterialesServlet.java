package Servlet;

import com.google.gson.Gson;
import imf.cels.entity.Material;
import imf.cels.facade.FacadeMaterial;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.AltaCotizacionBeanUI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.io.IOException;
import java.util.List;

@WebServlet("/buscarMateriales")
public class BuscarMaterialesServlet extends HttpServlet {
    FacadeMaterial facadeMaterial = new FacadeMaterial();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String texto = req.getParameter("texto");
        List<Material> resultados = facadeMaterial.obtenerPorNombreCot(texto);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) -> new JsonPrimitive(date.toString()))
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toString())) // <-- AGREGA ESTA LÍNEA
                .create();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(resultados));
    }
}
