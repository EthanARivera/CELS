package imf.cels.respaldos;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServicioRespaldo {

    // Usuario de la base de datos
    private static final String DB_USER = "root";
    //Contraseña de la base de datos
    private static final String DB_PASSWORD = "bnleon19";
    //Nombre de la base de datos
    private static final String DB_NAME = "cels";
    //Ruta donde se guardaran los respaldos
    private static final String RUTA_CARPETA = "C:\\Respaldos_CELS\\";
    //Ruta donde tienes instalado el MYSQL
    private static final String RUTA_MYSQL_BIN = "C:\\Program Files\\MySQL\\MySQL Server 9.4\\bin\\";

    private ScheduledExecutorService scheduler;

    public void iniciarServicio() {
        if (scheduler != null && !scheduler.isShutdown()) return;
        scheduler = Executors.newSingleThreadScheduledExecutor();

        long segundos = calcularSegundosParaProximoSabado();
        scheduler.scheduleAtFixedRate(this::ejecutarRespaldo, segundos, 7 * 24 * 60 * 60, TimeUnit.SECONDS);
        System.out.println("[Respaldo] Servicio iniciado.");
    }

    public void detenerServicio() {
        if (scheduler != null) scheduler.shutdownNow();
    }

    private long calcularSegundosParaProximoSabado() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime siguiente = ahora.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
                .withHour(23).withMinute(59).withSecond(0);
        if (ahora.isAfter(siguiente)) siguiente = siguiente.plusWeeks(1);
        return Duration.between(ahora, siguiente).getSeconds();
    }

    public void ejecutarRespaldo() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        String nombreArchivo = DB_NAME + "_Respaldo_" + fecha + ".sql";
        String rutaSalida = RUTA_CARPETA + nombreArchivo;

        new File(RUTA_CARPETA).mkdirs();

        List<String> comandos = new ArrayList<>();
        comandos.add(RUTA_MYSQL_BIN + "mysqldump.exe"); // El programa
        comandos.add("-u" + DB_USER);

        if (!DB_PASSWORD.isEmpty()) {
            comandos.add("-p" + DB_PASSWORD);
        }

        comandos.add("--databases");
        comandos.add(DB_NAME);
        comandos.add("--routines");
        comandos.add("--events");
        comandos.add("--triggers");
        comandos.add("--result-file=" + rutaSalida);

        try {
            System.out.println("[Respaldo] Ejecutando respaldo...");
            ProcessBuilder pb = new ProcessBuilder(comandos);
            pb.redirectErrorStream(true);
            Process proceso = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[SQL Output]: " + line);
                }
            }

            int exitCode = proceso.waitFor();

            if (exitCode == 0) {
                File archivo = new File(rutaSalida);
                if (archivo.exists() && archivo.length() > 0) {
                    System.out.println("[Respaldo] Archivo creado: " + nombreArchivo + " (" + archivo.length() + " bytes)");
                } else {
                    System.err.println("[Respaldo] Alerta: El proceso terminó bien pero el archivo está vacío o no existe.");
                }
            } else {
                System.err.println("[Respaldo] Código de error: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}