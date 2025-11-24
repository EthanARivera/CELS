package imf.cels.facade;

import imf.cels.dao.CotizacionDAO;
import imf.cels.delegate.DelegateCotizacion;
import imf.cels.entity.Cotizacion;
import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.entity.CotizacionMaterial;
import imf.cels.integration.ServiceLocator;
import java.util.List;

//Imports para la generación y edición del PDF

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import java.io.ByteArrayOutputStream;

//Imports para la generación y edicion del XML
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

//Imports para los correos

import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;

public class FacadeCotizacion {

    private final DelegateCotizacion delegateCotizacion;

    public FacadeCotizacion(){
        this.delegateCotizacion = new DelegateCotizacion();
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return delegateCotizacion.obtenerTodosPorFecha();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return delegateCotizacion.obtenerPorFolio();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return delegateCotizacion.obtenerPorVendedor();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return delegateCotizacion.obtenerPorAnio(anio);
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return delegateCotizacion.obtenerPorMes(mes);
    }

    public List<Integer> obtenerAniosDisponibles(){
        return delegateCotizacion.obtenerAniosDisponibles();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return delegateCotizacion.obtenerMesesDisponibles();
    }

    public void saveCotizacion(Cotizacion cotizacion){ delegateCotizacion.saveCotizacion(cotizacion); }

    public void saveCotizacionMaterial(CotizacionMaterial cotizacionMaterial) { delegateCotizacion.saveCotizacionMaterial(cotizacionMaterial); }

    public void saveCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) { delegateCotizacion.saveCotizacionManoDeObra(cotizacionManoDeObra); }

    public Integer ultimoFolio() { return delegateCotizacion.ultimoFolio(); }

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

    private void privateEnviarCorreo(String asunto, String cuerpo, byte[] adjuntoPdfBytes, String nombrePdf, byte[] adjuntoXmlBytes, String nombreXml) throws MessagingException {

        final String remitente = "CorreoUABC@uabc.edu.mx";
        final String psswd = "XXXX XXXX XXXX XXXX";

        final String destinatario = "CORREO@gmail.com";

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
        System.out.println("Mensaje enviado correctamente a " + destinatario);
    }


    public void enviarCotizacionPorCorreo(Integer idCotizacion) throws Exception{

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

        privateEnviarCorreo(asunto, cuerpo, pdfBytes, nombrePdf, xmlBytes, nombreXml);
    }

    public void aprobarCotizacion(Integer idFolio) {delegateCotizacion.aprobarCotizacion(idFolio);} // PBI-CO-US18

    //actualización de cotización
    public Cotizacion buscarPorIdUnico(int id) {
        return delegateCotizacion.buscarPorIdUnico(id);
    }

}