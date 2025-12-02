package imf.cels.mail;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import imf.cels.dao.CotizacionDAO;
import imf.cels.delegate.DelegateCotizacion;
import imf.cels.entity.Cotizacion;
import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.entity.CotizacionMaterial;
import imf.cels.integration.ServiceFacadeLocator;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class EmailCotizaciones {
    private CotizacionDAO cotizacionDAO;

    private DelegateCotizacion delegateCotizacion;

    public EmailCotizaciones(DelegateCotizacion delegateCotizacion) {
        this.delegateCotizacion = delegateCotizacion;
    }

    private byte[] privateGenerarXml(Cotizacion cotizacion) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Cotizacion.class);

        //Marshaller convierte de Java a XML
        Marshaller marshaller = context.createMarshaller();

        //Se formatea el XML para que sea legible
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(cotizacion, baos);

        return baos.toByteArray();
    }

    private byte[] privateGenerarPdf(Cotizacion cotizacion) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        //Formato de factura
        document.add(new Paragraph("Cotización Folio: " + cotizacion.getId()).setBold().setFontSize(18));
        document.add(new Paragraph("Fecha: " + cotizacion.getFecha().toString()));
        document.add(new Paragraph("Cliente: " + cotizacion.getCliente()));
        document.add(new Paragraph("Vendedor: " + cotizacion.getIdUsuario().getNombre()));
        document.add(new Paragraph("Proyecto: " + cotizacion.getDescripcion()));
        document.add(new Paragraph("\n"));

        //Tabla de materiales
        document.add(new Paragraph("Materiales").setBold());
        Table tableMat = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1}));
        tableMat.setWidth(UnitValue.createPercentValue(100));
        tableMat.addHeaderCell("Descripción");
        tableMat.addHeaderCell("Cantidad");
        tableMat.addHeaderCell("Subtotal");

        for (CotizacionMaterial mat : cotizacion.getCotizacionMateriales()) {
            tableMat.addCell(mat.getIdMaterial() != null ? mat.getIdMaterial().getNombre() : "N/A");
            tableMat.addCell(String.valueOf(mat.getCantidad()));
            tableMat.addCell(String.valueOf(mat.getSubtotal()));
        }
        document.add(tableMat);
        document.add(new Paragraph("\n"));

        //Tabla de mano de obra
        document.add(new Paragraph("Mano de Obra").setBold());
        Table tableMo = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1}));
        tableMo.setWidth(UnitValue.createPercentValue(100));
        tableMo.addHeaderCell("Responsable #");
        tableMo.addHeaderCell("Horas");
        tableMo.addHeaderCell("Subtotal");

        for (CotizacionManoDeObra mo : cotizacion.getCotizacionManoDeObras()) {
            tableMo.addCell(String.valueOf(mo.getId() != null ? mo.getId().getNumResponsable() : "N/A"));
            tableMo.addCell(String.valueOf(mo.getCantidadHoras()));
            tableMo.addCell(String.valueOf(mo.getSubtotal()));
        }
        document.add(tableMo);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("PRECIO FINAL: $" + cotizacion.getPrecioFinal().toPlainString()).setBold().setFontSize(16));

        document.close();
        return baos.toByteArray();
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

    private void privateEnviarCorreo(String correoUsuario, String asunto, String cuerpo, byte[] adjuntoPdfBytes, String nombrePdf, byte[] adjuntoXmlBytes, String nombreXml) throws MessagingException {

        // Ejemplo
        // final String remitente = "luis.cedillo@uabc.edu.mx";
        // final String psswd = "zdsv woaa azxy adrg";

        // final String destinatario = "lfcedillocamacho@gmail.com";

        final String remitente = "ventas@elletrero.com.mx";
        final String psswd = "amkh gpga ddfz wira";

        final String destinatarioUsuario = correoUsuario;
        final String destinatarioVentas = "ventas@elletrero.com.mx";

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
        mensaje.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(destinatarioUsuario.trim() + "," + destinatarioVentas.trim(), true));
        mensaje.setSubject(asunto);


        //Cuerpo del mensaje
        MimeBodyPart mimeBodyPartTexto = new MimeBodyPart();
        mimeBodyPartTexto.setText(cuerpo);

        //Se adjunta el pdf
        MimeBodyPart mimeBodyPartPdf = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(adjuntoPdfBytes, "application/pdf");
        mimeBodyPartPdf.setDataHandler(new DataHandler(source));
        mimeBodyPartPdf.setFileName(nombrePdf);

        //Se adjunta el XML
        MimeBodyPart mimeBodyPartXml = new MimeBodyPart();
        DataSource sourceXml = new ByteArrayDataSource(adjuntoXmlBytes, "application/xml");
        mimeBodyPartXml.setDataHandler(new DataHandler(sourceXml));
        mimeBodyPartXml.setFileName(nombreXml);

        //Se juntan todas las partes
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPartTexto);
        multipart.addBodyPart(mimeBodyPartPdf);
        multipart.addBodyPart(mimeBodyPartXml);
        mensaje.setContent(multipart);

        Transport.send(mensaje);
        System.out.println("Mensaje Cotizacion enviado correctamente a " + destinatarioUsuario + " y " + destinatarioVentas);
    }

    public void enviarCotizacionPorCorreo(String correoUsuario, Integer idCotizacion) throws Exception{

        //Obtención de datos
        List<Cotizacion> cotizaciones = delegateCotizacion.buscarPorId(idCotizacion);
        if(cotizaciones == null || cotizaciones.isEmpty()){
            throw new Exception("No se encontro la cotizacion con ID: "+idCotizacion);
        }
        Cotizacion cotizacion = cotizaciones.get(0);
        cotizacion.getCotizacionManoDeObras().size();
        cotizacion.getCotizacionMateriales().size();

        //Se genera el pdf y el XML
        byte[] pdfBytes = privateGenerarPdf(cotizacion);
        byte[] xmlBytes = privateGenerarXml(cotizacion);

        //Se prepara y envia el correo
        String nombrePdf = "Cotizacion_Folio_" + cotizacion.getId() + ".pdf";
        String nombreXml = "Cotizacion_Folio_" + cotizacion.getId() + ".xml";
        String asunto = "Detalle de su Cotización (Folio: " + cotizacion.getId() + ")";
        String cuerpo = "Estimado " + cotizacion.getCliente() + ",\n\n" +
                "Adjuntamos el detalle de su cotización.\n\n" +
                "Saludos cordiales.";

        privateEnviarCorreo(correoUsuario, asunto, cuerpo, pdfBytes, nombrePdf, xmlBytes, nombreXml);
    }

    public boolean enviarContratoPorCorreo(String correoUsuario, Integer idCotizacion) throws Exception {
        List<Cotizacion> cotizaciones = delegateCotizacion.buscarPorId(idCotizacion);

        if(cotizaciones == null || cotizaciones.isEmpty()) {
            throw new Exception("No se encontro la cotizacion con el ID: " + idCotizacion);
        }

        Cotizacion cotizacion = cotizaciones.get(0);

        File archivoGenerado = generarContratoDocx(cotizacion);

        // Ejemplo
        // final String remitente = "luis.cedillo@uabc.edu.mx";
        // final String psswd = "zdsv woaa azxy afvv";

        // final String destinatario = "lfcedillocamacho@gmail.com";

        final String remitente = "ventas@elletrero.com.mx";
        final String psswd = "amkh gpga ddfz wira";

        final String destinatarioUsuario = correoUsuario;
        final String destinatarioVentas = "ventas@elletrero.com.mx";

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
        mensaje.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(destinatarioUsuario.trim() + "," + destinatarioVentas.trim(), true));
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
        System.out.println("Mensaje Contrato enviado correctamente a " + destinatarioUsuario + " y " + destinatarioVentas);

        return true;
    }

}
