package Servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import imf.cels.entity.Material;
import imf.cels.facade.FacadeMaterial;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;

@WebServlet("/crearMaterial")
public class CrearMaterialServlet extends HttpServlet {
    FacadeMaterial materialFacade = new FacadeMaterial();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) -> new JsonPrimitive(date.toString()))
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toString()))
                .create();

        BufferedReader reader = req.getReader();

        Material nuevoMaterial = gson.fromJson(reader, Material.class);

        try {
            materialFacade.saveMaterial(nuevoMaterial);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write(gson.toJson(nuevoMaterial));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Error al guardar el material\"}");
        }
    }
}