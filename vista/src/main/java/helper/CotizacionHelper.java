package helper;

import imf.cels.entity.Cotizacion;
import imf.cels.entity.CotizacionManoDeObra;
import imf.cels.entity.CotizacionMaterial;
import imf.cels.entity.Material;
import imf.cels.integration.ServiceFacadeLocator;
import imf.cels.integration.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class CotizacionHelper {

    public void saveCotizacion(Cotizacion cotizacion) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacion(cotizacion);
    }

    public void saveCotizacionMaterial(CotizacionMaterial cotizacionMaterial) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacionMaterial(cotizacionMaterial);
    }

    public void saveCotizacionManoDeObra(CotizacionManoDeObra cotizacionManoDeObra) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().saveCotizacionManoDeObra(cotizacionManoDeObra);
    }

    public String obtenerCorreo(Integer idUsuario) {
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerCorreo(idUsuario); }

    public Integer obtenerIdUsuario(Integer idUsuario) {
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerIdUsuario(idUsuario); }

    public String obtenerNombre(Integer idUsuario) {
        return ServiceFacadeLocator.getInstanceFacadeUsuario().obtenerNombre(idUsuario);
    }

    public Integer ultimoFolio() { return ServiceFacadeLocator.getInstanceFacadeCotizacion().ultimoFolio(); }

    public List<Material> buscarMateriales(String nombre) {
        List<Material> materiales = ServiceFacadeLocator.getInstanceFacadeMaterial().obtenerMateriales();
        List<Material> materialesAux = new ArrayList<>();

        for(Material material : materiales) {
            if(material.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                materialesAux.add(material);
            }
        }
        return materialesAux;
    }

    public void enviarCotizacionPorCorreo(String correoUsuario, Integer idCotizacion) throws Exception {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().enviarCotizacionPorCorreo(correoUsuario, idCotizacion);
    }

    public boolean enviarContratoPorCorreo(String correoUsuario, Integer idCotizacion) throws Exception {
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().enviarContratoPorCorreo(correoUsuario, idCotizacion);
    }

    // Aprobación de Cotización
    public void aprobarCotizacion(Integer idFolio) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().aprobarCotizacion(idFolio);
    }

    // Aprobación de Contrato
    public void aprobarContrato(Integer idFolio) {
        ServiceFacadeLocator.getInstanceFacadeCotizacion().aprobarContrato(idFolio);
    }

    // Consultas Generales
    public List<Cotizacion> buscarPorId(int id){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().buscarPorId(id);
    }

    public List<Cotizacion> obtenerTodosPorFecha(){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerTodosPorFecha();
    }

    public List<Cotizacion> obtenerPorFolio(){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerPorFolio();
    }

    public List<Cotizacion> obtenerPorVendedor(){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerPorVendedor();
    }

    public List<Cotizacion> obtenerPorAnio(int anio){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerPorAnio(anio);
    }

    public List<Cotizacion> obtenerPorMes(int mes){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerPorMes(mes);
    }

    public List<Integer> obtenerAniosDisponibles(){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerAniosDisponibles();
    }

    public List<Integer> obtenerMesesDisponibles(){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerMesesDisponibles();
    }

    // Consultas por usuario vendedor
    public List<Cotizacion> obtenerTodosPorFecha(Integer idUsuario){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerTodosPorFecha(idUsuario);
    }

    public List<Cotizacion> obtenerPorFolio(Integer idUsuario){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerPorFolio(idUsuario);
    }

    public List<Cotizacion> obtenerPorAnio(Integer idUsuario, int anio){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerPorAnio(idUsuario, anio);
    }

    public List<Cotizacion> obtenerPorMes(Integer idUsuario, int mes){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerPorMes(idUsuario, mes);
    }

    public List<Integer> obtenerAniosDisponibles(Integer idUsuario){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerAniosDisponibles(idUsuario);
    }

    public List<Integer> obtenerMesesDisponibles(Integer idUsuario){
        return ServiceFacadeLocator.getInstanceFacadeCotizacion().obtenerMesesDisponibles(idUsuario);
    }
}
