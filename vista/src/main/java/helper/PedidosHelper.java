package helper;

import imf.cels.dao.PedidosDAO;
import imf.cels.entity.Cotizacion;
import imf.cels.entity.PedidosTaller;
import imf.cels.integration.ServiceLocator;
import imf.cels.persistence.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import java.time.Instant;

@ApplicationScoped
public class PedidosHelper {
    private PedidosDAO pedidosDAO = new PedidosDAO();

    public void darDeAltaPedido(Cotizacion cot, String prioridad) throws Exception{
        pedidosDAO.darDeAltaPedido(cot, prioridad);
    }
}