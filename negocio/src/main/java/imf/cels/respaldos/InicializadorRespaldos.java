package imf.cels.respaldos;

import imf.cels.respaldos.ServicioRespaldo;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InicializadorRespaldos implements ServletContextListener {

    private ServicioRespaldo servicio;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servicio = new ServicioRespaldo();
        servicio.iniciarServicio();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (servicio != null) {
            servicio.detenerServicio();
        }
    }
}