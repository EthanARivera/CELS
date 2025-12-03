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
import imf.cels.entity.CotizacionMaterial;
import imf.cels.integration.ServiceLocator;
import imf.cels.mail.EmailCotizaciones;
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

    public List<Cotizacion> buscarPorId(int id){
        return delegateCotizacion.buscarPorId(id);
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

    // Consultas por usuario vendedor
    public List<Cotizacion> obtenerTodosPorFecha(Integer idUsuario){
        return delegateCotizacion.obtenerTodosPorFecha(idUsuario);
    }

    public List<Cotizacion> obtenerPorFolio(Integer idUsuario){
        return delegateCotizacion.obtenerPorFolio(idUsuario);
    }

    public List<Cotizacion> obtenerPorAnio(Integer idUsuario, int anio){
        return delegateCotizacion.obtenerPorAnio(idUsuario, anio);
    }

    public List<Cotizacion> obtenerPorMes(Integer idUsuario, int mes){
        return delegateCotizacion.obtenerPorMes(idUsuario, mes);
    }

    public List<Integer> obtenerAniosDisponibles(Integer idUsuario){
        return delegateCotizacion.obtenerAniosDisponibles(idUsuario);
    }

    public List<Integer> obtenerMesesDisponibles(Integer idUsuario){
        return delegateCotizacion.obtenerMesesDisponibles(idUsuario);
    }

    public void saveCotizacion(Cotizacion cotizacion){ delegateCotizacion.saveCotizacion(cotizacion); }

    public void saveCotizacionMaterial(CotizacionMaterial cotizacionMaterial) { delegateCotizacion.saveCotizacionMaterial(cotizacionMaterial); }

    public void saveCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) { delegateCotizacion.saveCotizacionManoDeObra(cotizacionManoDeObra); }

    public Integer ultimoFolio() { return delegateCotizacion.ultimoFolio(); }

    public void enviarCotizacionPorCorreo(String correoUsuario, Integer idCotizacion) throws Exception {
        delegateCotizacion.enviarCotizacionPorCorreo(correoUsuario, idCotizacion);
    }

    public void aprobarCotizacion(Integer idFolio) {delegateCotizacion.aprobarCotizacion(idFolio);} // PBI-CO-US18

    public boolean enviarContratoPorCorreo(String correoUsuario, Integer idCotizacion) throws Exception {
        return delegateCotizacion.enviarContratoPorCorreo(correoUsuario, idCotizacion);
    }

    public void aprobarContrato(Integer idFolio) {delegateCotizacion.aprobarContrato(idFolio);} // PBI-CO-US20


    //PBI-CO-US13
    public Cotizacion buscarPorIdUnico(int id) {
        return delegateCotizacion.buscarPorIdUnico(id);
    }

}