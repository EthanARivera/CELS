package imf.cels.dao;

import imf.cels.entity.Material;
import jakarta.persistence.EntityManager;
import imf.cels.persistence.AbstractDAO;
import jakarta.persistence.EntityTransaction;

import java.util.List;


public class MaterialDAO extends AbstractDAO<Material> {
    private final EntityManager entityManager;

    public MaterialDAO(EntityManager entityManager) {
        super(Material.class);
        this.entityManager = entityManager;
    }

    public List<Material> obtenerTodos(){
        return entityManager
                .createQuery("SELECT a FROM Material a", Material.class)
                .getResultList();
    }

    public Material findById(int id) {
        return entityManager.find(Material.class, id);
    }

    public List<Material> findByName(String name) {
        try {
            return entityManager.createQuery(
                            "SELECT u FROM Material u WHERE u.nombre = :nombre", Material.class)
                    .setParameter("nombre", name)
                    .getResultList();
        } catch (jakarta.persistence.NoResultException e) {
            return null; // No se encontró el material
        }
    }

    public void update(Material material) {
        EntityTransaction transaction = null;
        try{
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.merge(material); //Se actualiza el material

                transaction.commit(); //Guarda los cambios en la base de datos
        } catch(Exception e){
            if(transaction != null && transaction.isActive()){
                transaction.rollback(); //Revierte el cambio
            }
            throw e;
        }
    }

    public void eliminar(int id){
        EntityTransaction transaction = null;
        try{
            transaction = entityManager.getTransaction();
            transaction.begin();

            Material material = entityManager.find(Material.class, id);
            if(material != null){
                entityManager.remove(material);
            }
            transaction.commit();
        }catch(Exception e){
            if(transaction != null && transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}

