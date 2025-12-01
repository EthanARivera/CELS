package imf.cels.dao;

import imf.cels.entity.Cotizacion;
import imf.cels.integration.ServiceLocator;
import imf.cels.persistence.AbstractDAO;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class CotizacionDAO extends AbstractDAO<Cotizacion>{
    private final EntityManager entityManager;

    public CotizacionDAO(EntityManager em) {
        super(Cotizacion.class);
        this.entityManager = em;
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c ORDER BY c.fecha DESC", Cotizacion.class)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c ORDER BY c.id ASC", Cotizacion.class)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c ORDER BY c.idUsuario.nombre ASC", Cotizacion.class)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE YEAR(c.fecha) = :anio ORDER BY c.fecha DESC", Cotizacion.class)
                .setParameter("anio", anio).getResultList();
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE MONTH(c.fecha) = :mes ORDER BY c.fecha DESC", Cotizacion.class)
                .setParameter("mes", mes).getResultList();
    }

    public List<Cotizacion> buscarPorId(int id){
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE c.id = :id",  Cotizacion.class)
                .setParameter("id", id).getResultList();
    }

    public List<Integer> obtenerAniosDisponibles(){
        return entityManager
                .createQuery("SELECT DISTINCT YEAR(c.fecha) FROM Cotizacion c ORDER BY YEAR(c.fecha) DESC",  Integer.class)
                .getResultList();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return entityManager
                .createQuery("SELECT DISTINCT MONTH(c.fecha) FROM Cotizacion c ORDER BY MONTH(c.fecha)", Integer.class)
                .getResultList();
    }

    // Mismos métodos para la búsqueda pero del usuario vendedor

    public List<Cotizacion> obtenerTodosPorFecha(Integer idUsuario) {
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE c.idUsuario.id = :idUsuario ORDER BY c.fecha DESC", Cotizacion.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorFolio(Integer idUsuario) {
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE c.idUsuario.id = :idUsuario ORDER BY c.id ASC", Cotizacion.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorAnio(Integer idUsuario, int anio) {
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE c.idUsuario.id = :idUsuario AND YEAR(c.fecha) = :anio ORDER BY c.fecha DESC", Cotizacion.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("anio", anio)
                .getResultList();
    }

    public List<Cotizacion> obtenerPorMes(Integer idUsuario, int mes) {
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE c.idUsuario.id = :idUsuario AND MONTH(c.fecha) = :mes ORDER BY c.fecha DESC", Cotizacion.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("mes", mes)
                .getResultList();
    }

    public List<Cotizacion> buscarPorId(Integer idUsuario, int id) {
        return entityManager
                .createQuery("SELECT c FROM Cotizacion c WHERE c.id = :id AND c.idUsuario.id = :idUsuario", Cotizacion.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Integer> obtenerAniosDisponibles(Integer idUsuario) {
        return entityManager
                .createQuery("SELECT DISTINCT YEAR(c.fecha) FROM Cotizacion c WHERE c.idUsuario.id = :idUsuario ORDER BY YEAR(c.fecha) DESC", Integer.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    public List<Integer> obtenerMesesDisponibles(Integer idUsuario) {
        return entityManager
                .createQuery("SELECT DISTINCT MONTH(c.fecha) FROM Cotizacion c WHERE c.idUsuario.id = :idUsuario ORDER BY MONTH(c.fecha)", Integer.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }



    //Registro de cotizaciones

    //Guardado de entidad Cotizacion
    public void registrarCotizacion(Cotizacion cotizacion) {

        //Validacion
       if(cotizacion.getIdUsuario() == null) {
            throw new IllegalArgumentException("No se encuentra ningún usuario vendedor activo. ");
        }

        if(cotizacion.getFecha() == null) {
            throw new IllegalArgumentException("La fecha no se puede recuperar de manera correcta. ");
        }

        if(cotizacion.getCliente() == null) {
            throw new IllegalArgumentException("Es necesario especificar el nombre del cliente. ");
        }

        if(cotizacion.getPrecioFinal() == null) {
            throw new IllegalArgumentException("El precio final no puede ser nulo.");
        }

        //Guardado
        ServiceLocator.getInstanceCotizacionDAO().save(cotizacion);
    }

    //función con query para obtener el último folio existente
    public Integer ultimoFolio() {
        Integer ultimoIdFolio = entityManager
                .createQuery("SELECT MAX(c.id) FROM Cotizacion c", Integer.class)
                .getSingleResult();

        //retorna 0 si no se encuentra ningun folio
        return (ultimoIdFolio != null) ? ultimoIdFolio : 0;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }


    // Aprobación de Cotización
    public void aprobarCotizacion(Integer idFolio) {
        Cotizacion cotizacion = entityManager.find(Cotizacion.class, idFolio);

        if (cotizacion == null) {
            throw new IllegalArgumentException("No se encontró la cotización con el folio especificado.");
        }

        if (Boolean.TRUE.equals(cotizacion.getisCotizacionAprobada())) {
            throw new IllegalStateException("La cotización ya fue aprobada y no puede desaprobarse.");
        }

        /*executeInsideTransaction(em -> {
            cotizacion.setAprobado(true);
            em.merge(cotizacion);
        });*/
        try {
            entityManager.getTransaction().begin();
            cotizacion.setIsCotizacionAprobada(true);
            entityManager.merge(cotizacion);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    public File generarContratoDocx(Cotizacion cotizacion) throws Exception {
        //Se Carga la plantilla del contrato desde resources
        InputStream in = getClass().getClassLoader().getResourceAsStream("plantillas/plantillaContrato.docx");
        if (in == null) {
            throw new Exception("No se encontró la plantilla: plantillas/plantillaContrato.docx");
        }

        XWPFDocument doc = new XWPFDocument(in);

        //Se reemplazan los placeholders del documento
        for (XWPFParagraph p : doc.getParagraphs()) {
            for (XWPFRun run : p.getRuns()) {
                String text = run.getText(0);
                if (text != null) {
                    text = text.replace("${CLIENTE}", cotizacion.getCliente());
                    text = text.replace("${FECHA}", java.time.LocalDate.now().toString());
                    text = text.replace("${DESCRIPCION}", cotizacion.getDescripcion());
                    text = text.replace("${DESCRIPCION_CORTA}",
                            cotizacion.getDescripcion().length() > 40
                                    ? cotizacion.getDescripcion().substring(0, 40)
                                    : cotizacion.getDescripcion());
                    text = text.replace("${PRECIO_FINAL}", cotizacion.getPrecioFinal().toString());
                    run.setText(text, 0);
                }
            }
        }

        //Se Guarda el archivo temporal con el contrato modificado
        File archivoFinal = File.createTempFile("contrato_" + cotizacion.getId(), ".docx");
        archivoFinal.deleteOnExit();

        FileOutputStream out = new FileOutputStream(archivoFinal);
        doc.write(out);
        out.close();
        doc.close();
        in.close();

        return archivoFinal;
    }

    public boolean enviarContratoPorCorreo(Integer idCotizacion) throws Exception {
        Cotizacion cotizacion = entityManager.find(Cotizacion.class, idCotizacion);

        File archivoGenerado = generarContratoDocx(cotizacion);

        // Ejemplo
        // final String remitente = "luis.cedillo@uabc.edu.mx";
        // final String psswd = "zdsv woaa azxy afvv";

        // final String destinatario = "lfcedillocamacho@gmail.com";

        final String remitente = "CORREO";
        final String psswd = "CONTRASEÑA APLICACION";

        final String destinatario = "CORREO";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, psswd);
            }
        });

        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(remitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject("Contrato de cotización: " + idCotizacion);

        //Cuerpo del mensaje
        MimeBodyPart texto = new MimeBodyPart();
        texto.setText("Contrato de la cotización: " + idCotizacion);

        //Se adjunta el documento
        MimeBodyPart adjuntoDocx = new MimeBodyPart();
        adjuntoDocx.attachFile(archivoGenerado);
        adjuntoDocx.setFileName("Contrato_Folio_" + idCotizacion + ".docx");

        //Se juntan todas las partes
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(texto);
        multipart.addBodyPart(adjuntoDocx);
        mensaje.setContent(multipart);

        Transport.send(mensaje);

        return true;
    }
    // Aprobación de Contrato
    public void aprobarContrato(Integer idFolio) {
        Cotizacion cotizacion = entityManager.find(Cotizacion.class, idFolio);

        if (cotizacion == null) {
            throw new IllegalArgumentException("No se encontró el contrato con el folio especificado.");
        }

        if (Boolean.TRUE.equals(cotizacion.getisContratoAprobado())) {
            throw new IllegalStateException("El contrato ya fue aprobada y no puede desaprobarse.");
        }

        /*executeInsideTransaction(em -> {
            cotizacion.setAprobado(true);
            em.merge(cotizacion);
        });*/
        try {
            entityManager.getTransaction().begin();
            cotizacion.setisContratoAprobado(true);
            entityManager.merge(cotizacion);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

}