package imf.cels.delegate;

import imf.cels.entity.Usuario;
import imf.cels.integration.ServiceLocator;
import imf.cels.negocio.UsuarioNegocio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class DelegateUsuario {
    private final UsuarioNegocio negocio = new imf.cels.negocio.UsuarioNegocio();

    /*public Usuario login(String password, String correo){
        Usuario usuario = new Usuario();
        List<Usuario> usuarios = ServiceLocator.getInstanceUsuarioDAO().findAll();

        for(Usuario us:usuarios){
            if(us.getPsswd().equalsIgnoreCase(password) && us.getEmail().equalsIgnoreCase(correo)){
                usuario = us;
            }
        }
        return usuario;
    }*/
    public Usuario login(String password, String correo){
        Usuario usuario = new Usuario();
        List<Usuario> usuarios = ServiceLocator.getInstanceUsuarioDAO().findAll();

        String encryptedPassword = encryptPassword(password);

        for(Usuario us : usuarios){
            if(us.getPsswd().equals(encryptedPassword) && us.getEmail().equalsIgnoreCase(correo)){
                usuario = us;
                break;
            }
        }
        return usuario;
    }

    public void saveUsario(Usuario usuario){
        ServiceLocator.getInstanceUsuarioDAO().save(usuario);
    }

    public List<Usuario> obtenerUsuarios() {
        return ServiceLocator.getInstanceUsuarioDAO().findAll();
    }

    public Usuario obtenerPorId(Integer id) {
        return ServiceLocator.getInstanceUsuarioDAO().findById(id);
    }

    public List<Usuario> obtenerPorNombre(String nombre) {
        return ServiceLocator.getInstanceUsuarioDAO().findByName(nombre);
    }


    // Format y validacion de registro de usuario
    public void registrarUsuario(Usuario usuario){
        // Capitalize primer letra de nombres
        usuario.setNombre(capitalizeFirstLetter(usuario.getNombre()));
        usuario.setApellidoPrim(capitalizeFirstLetter(usuario.getApellidoPrim()));
        if (usuario.getApellidoSeg() != null && !usuario.getApellidoSeg().isEmpty()){
            usuario.setApellidoSeg(capitalizeFirstLetter(usuario.getApellidoSeg()));
        }

        // Convertire RFC en mayusculas
        if (usuario.getRfc() != null ){
            usuario.setRfc(usuario.getRfc().toUpperCase());
        }

        // Encripcion de contraseña antes de guardar
        if (usuario.getPsswd() != null && usuario.getPsswd().length() < 64) {
            usuario.setPsswd(encryptPassword(usuario.getPsswd()));
        }

        // Validacion
        if(!negocio.validarCorreo(usuario.getEmail()))
            throw new IllegalArgumentException("Correo Inválido");

        if(!negocio.validarRFC(usuario))
            throw new IllegalArgumentException("RFC no coincide con los datos");

        if(ServiceLocator.getInstanceUsuarioDAO().existeCorreo(usuario.getEmail()))
            throw new IllegalArgumentException("Correo ya está registrado");

        ServiceLocator.getInstanceUsuarioDAO().save(usuario); //guardar usuario
    }

    // Metodo para capitalize
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        str = str.toLowerCase(); // normalize rest of letters
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    // SHA-256 password encryption
    private String encryptPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256 hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convertir la contraseña en un arreglo de bytes usando UTF-8
            //     y calcular su hash (SHA-256 genera un arreglo de 32 bytes)
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Crear un StringBuilder para convertir el arreglo de bytes a una cadena hexadecimal
            StringBuilder hexString = new StringBuilder();

            // Loop through each byte in the hash array/Recorrer cada byte del hash
            for (byte b : hash) {
                // Convertir the byte to a hex string
                //      `0xff & b` ensures we treat the byte as unsigned (0-255)
                String hex = Integer.toHexString(0xff & b);

                // If the hex string has only 1 character, prepend '0' to make it 2 characters
                //      (e.g., 'A' becomes '0A') — ensures consistent 64-character hash
                if (hex.length() == 1) hexString.append('0');

                // Agregar el valor hexadecimal al StringBuilder
                hexString.append(hex);
            }

            // Convertir el StringBuilder a String y devolverlo
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // If SHA-256 algorithm is not available (rare), throw a RuntimeException
            throw new RuntimeException("Error encrypting password", e);
        }
    }
}
