package imf.cels.dao;

import imf.cels.entity.Cotizacion;
import imf.cels.entity.PedidosTaller;
import imf.cels.persistence.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.time.Instant;

public class PedidosDAO {
    public void guardar(PedidosTaller pedido) {
        EntityManager em = HibernateUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(pedido);
        em.getTransaction().commit();
    }

    public void darDeAltaPedido(Cotizacion cot, String prioridad) {
        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Cotizacion cotManaged = em.find(Cotizacion.class, cot.getId());
            if (cotManaged == null) {
                throw new RuntimeException("La cotización no existe en la BD.");
            }

            PedidosTaller pedido = new PedidosTaller();
            pedido.setId(cotManaged.getId());
            pedido.setCotizacion(cotManaged);
            pedido.setEstadoEnTaller("Por iniciar");
            pedido.setPrioridad(prioridad);
            pedido.setFechaActualizacion(Instant.now());
            em.persist(pedido);
            cotManaged.setPedidosTaller(pedido);
            em.merge(cotManaged);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
}