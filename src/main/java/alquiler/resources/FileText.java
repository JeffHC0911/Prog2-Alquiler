package alquiler.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * https://picodotdev.github.io/blog-bitix/2016/04/tutorial-sobre-los-tipos-genericos-de-java/
 * https://www.baeldung.com/java-write-to-file
 * https://www.baeldung.com/reading-file-in-java
 * https://www.arquitecturajava.com/file-to-string-java8-y-manejo-de-ficheros/
 *
 * @author Carlos Cuesta
 */

public class FileText<T> implements Persistible<T> {

    @Override
    public List<?> read(String filePath) throws Exception { // propagar el posible error

        filePath = Helpers.initPath(filePath, ".txt");
        List<String> lst = new ArrayList<>();

        if (Helpers.fileExists(filePath)) {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                lst.add(scanner.nextLine()); 
            }
            scanner.close();
        }

        return lst;
    }

    @Override
    public void save(List<T> list, String filePath) throws Exception {
        filePath = Helpers.initPath(filePath, ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (T t : list) {
            writer.write(t.toString() + "\n");
        }
        writer.flush();
        writer.close();
    }
    
}
