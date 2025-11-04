import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class TestMail {
    public static void main(String[] args) {
        //configuracion de credenciales
        final String remitente = "correo@uabc.edu.mx";
        final String psswd = "xxxx xxxx xxxx xxxx"; //Para poder iniciar sesión como remitente es necesario
                                                    //contar con una contraseña de aplicación en caso
                                                    //de utilizar gmail (también entra el dominio de uabc)
        final String destinatario = "correo@gmail.com";

        //Propiedades para Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        //Autenticacion de usuario
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, psswd);
            }
        });

        //Estructura del mensaje
        try{
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Prueba de envío con Jakarta Mail.");
            mensaje.setText("Hola Papu! Este es un mensaje de prueba enviado con Jakarta Mail");

            //Envío
            Transport.send(mensaje);
            System.out.println("Mensaje enviado correctamente");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
