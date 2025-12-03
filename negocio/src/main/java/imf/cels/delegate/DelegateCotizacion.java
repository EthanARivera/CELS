package imf.cels.delegate;

import imf.cels.dao.CotizacionDAO;
import imf.cels.entity.Cotizacion;
import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.entity.CotizacionMaterial;
import imf.cels.integration.ServiceLocator;
import imf.cels.mail.EmailCotizaciones;

import java.util.List;

public class DelegateCotizacion {

    private CotizacionDAO cotizacionDAO = ServiceLocator.getInstanceCotizacionDAO();
    private EmailCotizaciones emailCotizaciones;

    public DelegateCotizacion() {
        this.emailCotizaciones = new EmailCotizaciones(this);
    }


    public void setEmailCotizaciones(EmailCotizaciones emailCotizaciones) {
        this.emailCotizaciones = emailCotizaciones;
    }

    public List<Cotizacion> buscarPorId(int id){
        return ServiceLocator.getInstanceCotizacionDAO().buscarPorId(id);
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerTodosPorFecha();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorFolio();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorVendedor();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorAnio(anio);
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorMes(mes);
    }

    public List<Integer> obtenerAniosDisponibles(){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerAniosDisponibles();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return  ServiceLocator.getInstanceCotizacionDAO().obtenerMesesDisponibles();
    }

    // Consultas para usuario vendedor
    public List<Cotizacion> obtenerTodosPorFecha(Integer idUsuario){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerTodosPorFecha(idUsuario);
    }

    public List<Cotizacion> obtenerPorFolio(Integer idUsuario){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorFolio(idUsuario);
    }

    public List<Cotizacion> obtenerPorAnio(Integer idUsuario, int anio){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorAnio(idUsuario, anio);
    }

    public List<Cotizacion> obtenerPorMes(Integer idUsuario, int mes){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerPorMes(idUsuario, mes);
    }

    public List<Integer> obtenerAniosDisponibles(Integer idUsuario){
        return ServiceLocator.getInstanceCotizacionDAO().obtenerAniosDisponibles(idUsuario);
    }

    public List<Integer> obtenerMesesDisponibles(Integer idUsuario){
        return  ServiceLocator.getInstanceCotizacionDAO().obtenerMesesDisponibles(idUsuario);
    }

    //Guardado de entidad Cotizacion
    //Llamando a CotizacionDAO desde ServiceLocator
    public void saveCotizacion(Cotizacion cotizacion){
        ServiceLocator.getInstanceCotizacionDAO().registrarCotizacion(cotizacion);
    }

    //Guardado de entidad CotizacionMaterial
    //Llamando a CotizacionMaterialDAO desde ServiceLocator
    public void saveCotizacionMaterial(CotizacionMaterial cotizacionMaterial) {
        ServiceLocator.getCotizacionMaterialDAO().registrarCotizacionMaterial(cotizacionMaterial);

    }


    //Guardado de entidad CotizacionManoDeObra
    //Llamando a CotizacionManoDeObraDAO desde ServiceLocator
    public void saveCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) {
        ServiceLocator.getCotizacionManoDeObraDAO().registrarCotizacionManoDeObra(cotizacionManoDeObra);
    }

    // Aprobación de Cotización
    public void aprobarCotizacion(Integer idFolio) {
        ServiceLocator.getInstanceCotizacionDAO().aprobarCotizacion(idFolio);
    }

    // Apribación de Contrato
    public void aprobarContrato(Integer idFolio) {
        ServiceLocator.getInstanceCotizacionDAO().aprobarContrato(idFolio);
    }

    public Integer ultimoFolio() { return ServiceLocator.getInstanceCotizacionDAO().ultimoFolio(); }

    public boolean enviarContratoPorCorreo(String correoUsuario, Integer idCotizacion) throws Exception {
        return emailCotizaciones.enviarContratoPorCorreo(correoUsuario, idCotizacion);
    }

    public void enviarCotizacionPorCorreo(String correoUsuario, Integer idCotizacion) throws Exception {
        emailCotizaciones.enviarCotizacionPorCorreo(correoUsuario, idCotizacion);
    }

    //actualización de cotización PBI-CO-US13
    public Cotizacion buscarPorIdUnico(int id) {
        return ServiceLocator.getInstanceCotizacionDAO().buscarPorIdUnico(id);
    }

}