package imf.cels.integration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import jakarta.persistence.EntityManager;
import imf.cels.dao.*;
import imf.cels.persistence.HibernateUtil;


/**
 *
 * @author total
 */
public class ServiceLocator {

    private static UsuarioDAO usuarioDAO;
    private static MaterialDAO materialDAO;
    private static CotizacionDAO cotizacionDAO;
    private static CotizacionMaterialDAO cotizacionMaterialDAO;
    private static CotizacionManoDeObraDAO cotizacionManoDeObraDAO;

    private static EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    /**
     * se crea la instancia de usuarioDAO si esta no existe
     */
    public static UsuarioDAO getInstanceUsuarioDAO(){
        if(usuarioDAO == null){
            usuarioDAO = new UsuarioDAO(getEntityManager());
            return usuarioDAO;
        } else{
            return usuarioDAO;
        }
    }

    public static MaterialDAO getInstanceMaterialDAO(){
        if(materialDAO == null){
            materialDAO = new MaterialDAO(getEntityManager());
            return materialDAO;
        } else{
            return materialDAO;
        }
    }

    public static CotizacionDAO getInstanceCotizacionDAO(){
        if(cotizacionDAO == null){
            cotizacionDAO = new CotizacionDAO(getEntityManager());
            return cotizacionDAO;
        }
        return cotizacionDAO;
    }

    public static CotizacionMaterialDAO getCotizacionMaterialDAO() {
        if(cotizacionMaterialDAO == null){
            cotizacionMaterialDAO = new CotizacionMaterialDAO(getEntityManager());
            return cotizacionMaterialDAO;
        }
        return cotizacionMaterialDAO;
    }

    public static CotizacionManoDeObraDAO getCotizacionManoDeObraDAO() {
        if(cotizacionManoDeObraDAO == null){
            cotizacionManoDeObraDAO = new CotizacionManoDeObraDAO(getEntityManager());
            return cotizacionManoDeObraDAO;
        }
        return cotizacionManoDeObraDAO;
    }
}