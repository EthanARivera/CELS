package imf.cels.negocio;

import imf.cels.entity.Usuario;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class UsuarioNegocio {

    /**
     * Valida que el correo tenga formato correcto y pertenezca a dominios permitidos.
     * Dominios permitidos: gmail.com, yahoo.com, uabc.edu.mx
     **/
    public boolean validarCorreo(String correo) {
        if (correo == null) return false;

        // Expresión regular que valida formato + dominios específicos
        String regex = "^[\\w._%+-]+@(gmail\\.com|yahoo\\.com|uabc\\.edu\\.mx)$";

        return Pattern.matches(regex, correo.toLowerCase());
    }

    // Genera el RFC de manera simplificada
    public String generarRFC(Usuario u) {
        String ap1 = u.getApellidoPrim().toUpperCase();
        String ap2 = (u.getApellidoSeg() != null) ? u.getApellidoSeg().toUpperCase() : "";
        String nom = u.getNombre().toUpperCase();
        String fecha = u.getFechaNacimiento().format(DateTimeFormatter.ofPattern("yyMMdd"));

        String rfc = "";
        rfc += ap1.charAt(0); // primera letra apellido paterno
        rfc += primeraVocal(ap1);
        rfc += ap2.isEmpty() ? 'X' : ap2.charAt(0);
        rfc += nom.charAt(0);
        rfc += fecha;
        return rfc.toUpperCase();
    }

    // Valida que el RFC del usuario coincida con el generado
    public boolean validarRFC(Usuario u) {
        if (u.getRfc() == null || u.getFechaNacimiento() == null) return false;
        String esperado = generarRFC(u);
        return u.getRfc().toUpperCase().startsWith(esperado.substring(0, 10));
    }

    private String primeraVocal(String palabra) {
        for (int i = 1; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            if ("AEIOU".indexOf(c) >= 0) return String.valueOf(c);
        }
        return "X";
    }
}
