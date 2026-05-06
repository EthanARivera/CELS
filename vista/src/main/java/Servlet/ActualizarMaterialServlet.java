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

@WebServlet("/actualizarMaterial")
public class ActualizarMaterialServlet extends HttpServlet {
    FacadeMaterial materialFacade = new FacadeMaterial();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) -> new JsonPrimitive(date.toString()))
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toString()))
                .create();

        BufferedReader reader = req.getReader();
        Material materialModificado = gson.fromJson(reader, Material.class);

        try {
            if (materialModificado.getId() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\": \"El campo 'id' es obligatorio para actualizar\"}");
                return;
            }

            materialFacade.modificarMaterial(materialModificado);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write(gson.toJson(materialModificado));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Error al actualizar el material: " + e.getMessage() + "\"}");
        }
    }
}