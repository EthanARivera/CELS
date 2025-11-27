package imf.cels.delegate;

import imf.cels.dao.UsuarioDAO;
import imf.cels.entity.Usuario;
import imf.cels.entity.UsDatosSensible;
import imf.cels.entity.UsPsswd;
import imf.cels.integration.ServiceLocator;
import imf.cels.negocio.UsuarioNegocio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class DelegateUsuario {
    private final UsuarioNegocio negocio = new imf.cels.negocio.UsuarioNegocio();

    public Usuario login(String password, String correo) {

        String encryptedPassword = encryptPassword(password);

        List<Usuario> usuarios = ServiceLocator.getInstanceUsuarioDAO().findAll();

        for (Usuario us : usuarios) {

            String psswd = ServiceLocator.getInstanceUsuarioDAO().obtenerPsswd(us.getId());
            String correoBD = ServiceLocator.getInstanceUsuarioDAO().obtenerCorreo(us.getId());

            if (psswd.equals(encryptedPassword) && correoBD.equalsIgnoreCase(correo)) {
                return us;
            }
        }

        return null;
    }


    public void saveUsuario(Usuario usuario) {

        UsuarioDAO dao = ServiceLocator.getInstanceUsuarioDAO();

        // 1. Guardar primero el usuario (genera id)
        dao.save(usuario);

        // 2. Asignar PK compartida a usDatosSensible
        UsDatosSensible uds = usuario.getUsDatosSensible();
        uds.setId(usuario.getId());
        uds.setUsuario(usuario);
        dao.saveDatosSensible(uds);

        // 3. Asignar PK compartida a usPsswd
        UsPsswd up = usuario.getUsPsswd();
        up.setId(usuario.getId());
        up.setUsuario(usuario);
        dao.savePsswd(up);
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
        //PARA REGISTRAR USUARIOS NUEVOS
        //Si las clases hijas son null, se inicializan
        if (usuario.getUsDatosSensible() == null)
            usuario.setUsDatosSensible(new UsDatosSensible());

        if (usuario.getUsPsswd() == null)
            usuario.setUsPsswd(new UsPsswd());

        usuario.getUsDatosSensible().setUsuario(usuario);
        usuario.getUsPsswd().setUsuario(usuario);

        // Capitalize primer letra de nombres
        usuario.setNombre(capitalizeFirstLetter(usuario.getNombre()));
        usuario.setApellidoPrim(capitalizeFirstLetter(usuario.getApellidoPrim()));
        if (usuario.getApellidoSeg() != null && !usuario.getApellidoSeg().isEmpty()){
            usuario.setApellidoSeg(capitalizeFirstLetter(usuario.getApellidoSeg()));
        }

        // Convertire RFC en mayusculas
        if (usuario.getUsDatosSensible().getRfc() != null ){
            usuario.getUsDatosSensible().setRfc(usuario.getUsDatosSensible().getRfc().toUpperCase());
        }

        // Encripcion de contraseña antes de guardar
        if (usuario.getUsPsswd().getPsswd() != null && usuario.getUsPsswd().getPsswd().length() < 64) {
            usuario.getUsPsswd().setPsswd(encryptPassword(usuario.getUsPsswd().getPsswd()));
        }

        // Validacion
        if(!negocio.validarCorreo(usuario.getUsDatosSensible().getEmail()))
            throw new IllegalArgumentException("Correo Inválido");

        if(!negocio.validarRFC(usuario))
            throw new IllegalArgumentException("RFC no coincide con los datos");

        if(ServiceLocator.getInstanceUsuarioDAO().existeCorreo(usuario.getUsDatosSensible().getEmail()))
            throw new IllegalArgumentException("Correo ya está registrado");

        saveUsuario(usuario); //guardar usuario
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



    // modificacion de correo y contraseña
    public void modificarCorreoYContrasena(Usuario usuario) {
        System.out.println("Entrada a delegate modificarCorreoYContrasena");

        // Validar formato
        if (!negocio.validarCorreo(usuario.getUsDatosSensible().getEmail())) {
            throw new IllegalArgumentException("Correo inválido");
        }

        UsuarioDAO dao = ServiceLocator.getInstanceUsuarioDAO();

        // Buscar por email en la entidad hija
        Usuario existing = dao.findByOneParameterUnique(
                usuario.getUsDatosSensible().getEmail(),
                "usDatosSensible.email"
        );

        if (existing != null && !existing.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("El correo ya está registrado por otro usuario");
        }

        // Encriptar contraseña
        usuario.getUsPsswd().setPsswd(encryptPassword(
                usuario.getUsPsswd().getPsswd()
        ));

        // Guardar en BD
        dao.actualizarCorreoYContrasena(
                usuario.getId(),
                usuario.getUsDatosSensible().getEmail(),
                usuario.getUsPsswd().getPsswd()
        );
    }


    // Activacion/Desactivacion
    public void cambiarEstadoUsuario(Integer idUsuario, boolean nuevoEstado){
        Usuario usuario = ServiceLocator.getInstanceUsuarioDAO().findById(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        ServiceLocator.getInstanceUsuarioDAO().cambiarEstado(idUsuario, nuevoEstado);
    }

    public String obtenerCorreo(Integer idUsuario) { return ServiceLocator.getInstanceUsuarioDAO().obtenerCorreo(idUsuario); }

    public Integer obtenerIdUsuario(Integer idUsuario) { return ServiceLocator.getInstanceUsuarioDAO().obtenerIdUsuario(idUsuario); }

    public String obtenerNombre(Integer idUsuario) { return ServiceLocator.getInstanceUsuarioDAO().obtenerNombre(idUsuario); }

}
