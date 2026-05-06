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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        String texto = req.getParameter("texto");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) -> new JsonPrimitive(date.toString()))
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toString()))
                .create();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                Integer id = Integer.parseInt(idParam);
                Material resultado = facadeMaterial.obtenerPorId(id);

                if (resultado == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\": \"Material no encontrado\"}");
                    return;
                }

                resp.getWriter().write(gson.toJson(resultado));

            } else if (texto != null) {
                List<Material> resultados = facadeMaterial.obtenerPorNombreCot(texto);
                resp.getWriter().write(gson.toJson(resultados));

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Debes enviar un parametro 'id' o 'texto'\"}");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"El ID debe ser un numero\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Error en el servidor\"}");
        }
    }
}
