import imf.cels.dao.UsuarioDAO;
import imf.cels.persistence.HibernateUtil;
import imf.cels.entity.Usuario;

import java.util.List;

public class testDAO {

    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(HibernateUtil.getEntityManager());



        for (Usuario usuario : usuarioDAO.findAll()) {
            System.out.println(usuario + "|| id [" + usuario.getId()+ "]" + " nombre [" + usuario.getNombre() + "]");
        }
    }
}