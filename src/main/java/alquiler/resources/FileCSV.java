package alquiler.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Esto es un simple ejercicio académico para explicar interfaces, genéricos, casting y archivos
 *
 * @author Carlos Cuesta
 */

public class FileCSV<T> implements Persistible<T> {

    @Override
    public List<?> read(String filePath) throws Exception { // propagar el posible error

        filePath = Helpers.initPath(filePath, ".csv");
        // cada elemento de la lista guardará un array de strings
        List<String[]> lst = new ArrayList<>(); // <-- OJO

        if (Helpers.fileExists(filePath)) {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                // se divide la línea y se guardan los datos resultantes en un array
                String data[] = scanner.nextLine().split(";");
                // se agrega el array de datos a un elemento de la lista
                lst.add(data); 
            }
            scanner.close();
        }

        return lst;
    }

    @Override
    public void save(List<T> list, String filePath) throws Exception {
        filePath = Helpers.initPath(filePath, ".csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (T t : list) {
            Exportable aux = (Exportable) t; // <-- OJO
            writer.write(aux.toCSV() + "\n");
        }
        writer.flush();
        writer.close();
    }
    
}
