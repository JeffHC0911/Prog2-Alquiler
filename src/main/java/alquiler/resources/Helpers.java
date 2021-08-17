package alquiler.resources;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;

/**
 *
 * @author Carlos Cuesta Iglesias
 */
public class Helpers {

     // Importante: no se revisan proyectos que cambien esta ruta
     public static final String RUTA = "./archivos/";

    public static final String mensaje = "\nMétodo sin implementar.\n" 
        + "Por favor revise la traza de este error para saber qué y en qué\n" 
        + "parte se le está solicitando implementar la funcionalidad requerida:";

    public static String strFecha(Calendar fecha) {
        return (
            new SimpleDateFormat("yyyy-MM-dd")
        ).format(fecha.getTime());
    }

    public static Calendar getFecha(String strFecha) throws ParseException {
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(
            new SimpleDateFormat("yyyy-MM-dd").parse(strFecha)
        );
        return fecha;
    }

    public static String hoy() {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }

    public static long mesesIntervalo(Calendar fecha1, Calendar fecha2) {
        double meses = fecha1.getTimeInMillis() - fecha2.getTimeInMillis();
        double minutosTotal = meses / 60000;
        double mesTotal = minutosTotal / 43800;
        return (long) mesTotal;

    }

    public static boolean hayCruce(Calendar fechaHora1Inicio, Calendar fechaHora1Fin, Calendar fechaHora2Inicio,
            Calendar fechaHora2Fin) {

        if (fechaHora2Inicio.before(fechaHora1Fin) && fechaHora2Fin.after(fechaHora1Inicio)) {
            return true;

        }
        return false;

    }

    /**
     * Verifica si dos intervalos de tiempo son iguales
     * 
     * @param fechaHoraInicio1
     * @param fechaHoraFin1
     * @param fechaHoraInicio2
     * @param fechaHoraFin2
     * @return Si los intervalos dados son iguales retorna true, false en caso
     *         contrario
     */
    public static boolean rangosFechaHoraIguales(Calendar fechaHoraInicio1, Calendar fechaHoraFin1,
            Calendar fechaHoraInicio2, Calendar fechaHoraFin2) {
        return fechaHoraInicio1.equals(fechaHoraInicio2) && fechaHoraFin1.equals(fechaHoraFin2);
    }

    public static long horasIntervalo(Calendar fechaHora1, Calendar fechaHora2) {
        double milisegundos = fechaHora2.getTimeInMillis() - fechaHora1.getTimeInMillis();
        double minutos = milisegundos / 60000;
        double horas = minutos / 60;
        return (long) Math.ceil(horas);
    }

    public static int stringToInt(String texto) {
        int valor;

        if (texto.trim().equals("")) {
            valor = 0;
        } else {
            valor = Integer.parseInt(texto);
        }
        return valor;
    }

    public static double stringToDouble(String texto) {
        double valor;

        if (texto.trim().equals("")) {
            valor = 0;
        } else {
            valor = Double.parseDouble(texto);
        }
        return valor;
    }

    public static boolean stringToBoolean(String texto){
        boolean valor;
        valor = Boolean.parseBoolean(texto);
        return valor;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Manejo de archivos

    public static boolean fileExists(String fileName) {
        File f = new File(fileName);
        // true si existe y no es un directorio
        return f.exists() && !f.isDirectory();
    }

    public static boolean pathExists(String path) {
        File f = new File(path);
        // true si existe y es un directorio
        return f.exists() && f.isDirectory();
    }

    public static void createFolderIfNotExist(String folder) {
        // si no existe o si existe y no es un directorio...
        if (!pathExists(folder)) {
            File file = new File(folder);
            file.mkdirs();
        }
    }

    public static String getPath(String path) {
        File file = new File(path);
        return file.getParent().replace("\\", "/");
    }

    public static String initPath(String filePath, String extension) {
        filePath += extension;
        String path = getPath(filePath);
        createFolderIfNotExist(path);
        return filePath;
    }

    // -------------------------------------------------------------------------------------------------------------------

    public static long aleatorio(long a, long b) {
        return ThreadLocalRandom.current().nextLong(a, b);
    }

    public static double[] getValores(JSONArray jsonArray) {
        double[] valores = null;
        if (jsonArray != null) {
            int len = jsonArray.length();
            valores = new double[len];
            for (int i = 0; i < jsonArray.length(); i++) {
                valores[i] = jsonArray.getDouble(i);
            }
            return valores;
        }
        return valores;
    }

}
