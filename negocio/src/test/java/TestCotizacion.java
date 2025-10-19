import imf.cels.entity.*;
import imf.cels.persistence.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

public class TestCotizacion {
    public static void main(String[] args) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Buscar usuario existente
            Usuario usuario = em.find(Usuario.class, 1);
            if (usuario == null) {
                System.err.println("No se encontró el usuario con id = 1");
                return;
            }

            // Crear materiales (si no existen)
            Material mat1 = new Material();
            mat1.setNombre("Cemento gris 50kg");
            mat1.setCosto(BigDecimal.valueOf(180.50));
            mat1.setTipoUnidad(TipoUnidad.KILOGRAMOS);
            em.persist(mat1);

            Material mat2 = new Material();
            mat2.setNombre("Varilla de acero 3/8");
            mat2.setCosto(BigDecimal.valueOf(120.00));
            mat2.setTipoUnidad(TipoUnidad.METROS);
            em.persist(mat2);

            // Crear cotización
            Cotizacion cot = new Cotizacion();
            cot.setIdUsuario(usuario);
            cot.setCliente("Constructora MX");
            cot.setFecha(LocalDate.now());
            cot.setDescripcion("Remodelación de fachada comercial");
            cot.setTipoProyecto(TipoProyecto.FACHADA);
            cot.setPrecioFinal(BigDecimal.ZERO);

            // Forzamos persistencia de cotización para obtener su ID
            em.persist(cot);
            em.flush(); // importante: ahora cot.getId() ya tiene valor

            // Crear relación cotización-material 1
            CotizacionMaterial cm1 = new CotizacionMaterial();
            CotizacionMaterialId cm1Id = new CotizacionMaterialId();
            cm1Id.setIdFolio(cot.getId());
            cm1Id.setIdMaterial(mat1.getId());
            cm1.setId(cm1Id);
            cm1.setIdFolio(cot);
            cm1.setIdMaterial(mat1);
            cm1.setCantidad(BigDecimal.valueOf(5));
            cm1.setSubtotal(mat1.getCosto().multiply(cm1.getCantidad()));
            cot.getCotizacionMateriales().add(cm1);

            // Crear relación cotización-material 2
            CotizacionMaterial cm2 = new CotizacionMaterial();
            CotizacionMaterialId cm2Id = new CotizacionMaterialId();
            cm2Id.setIdFolio(cot.getId());
            cm2Id.setIdMaterial(mat2.getId());
            cm2.setId(cm2Id);
            cm2.setIdFolio(cot);
            cm2.setIdMaterial(mat2);
            cm2.setCantidad(BigDecimal.valueOf(10));
            cm2.setSubtotal(mat2.getCosto().multiply(cm2.getCantidad()));
            cot.getCotizacionMateriales().add(cm2);

            // Crear mano de obra
            CotizacionManoDeObra mdo1 = new CotizacionManoDeObra();
            CotizacionManoDeObraId mdoid1 = new CotizacionManoDeObraId();
            mdoid1.setIdFolio(cot.getId());
            mdoid1.setNumResponsable(1);
            mdo1.setId(mdoid1);
            mdo1.setCantidadHoras(BigDecimal.valueOf(8));
            mdo1.setCostoHora(BigDecimal.valueOf(150.00));
            mdo1.setSubtotal(mdo1.getCostoHora().multiply(mdo1.getCantidadHoras()));
            mdo1.setIdFolio(cot);
            cot.getCotizacionManoDeObras().add(mdo1);

            // Calcular precio final
            BigDecimal totalMateriales = cot.getCotizacionMateriales().stream()
                    .map(CotizacionMaterial::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalMDO = cot.getCotizacionManoDeObras().stream()
                    .map(CotizacionManoDeObra::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            cot.setPrecioFinal(totalMateriales.add(totalMDO));

            tx.commit();
            System.out.println("Cotización guardada correctamente con ID: " + cot.getId());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            HibernateUtil.close();
        }
    }
}
