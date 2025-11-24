package Servlet;

import com.google.gson.Gson;
import imf.cels.entity.Material;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.AltaCotizacionBeanUI;

import java.io.IOException;
import java.util.List;

@WebServlet("/buscarMateriales")
public class BuscarMaterialesServlet extends HttpServlet {
    @Inject
    AltaCotizacionBeanUI ui;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String texto = req.getParameter("texto");
        List<Material> resultados = ui.buscarMateriales(texto);

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(resultados));
    }
}
