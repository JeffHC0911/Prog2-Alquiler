package alquiler.resources;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * https://picodotdev.github.io/blog-bitix/2016/04/tutorial-sobre-los-tipos-genericos-de-java/
 * https://www.baeldung.com/java-write-to-file
 * https://www.baeldung.com/reading-file-in-java
 * https://www.arquitecturajava.com/file-to-string-java8-y-manejo-de-ficheros/
 * https://stackoverflow.com/questions/32125579/java-files-readallbytes-without-changing-charset
 *
 * @author Carlos Cuesta
 */

public class FileJSON<T> implements Persistible<T>{

    @Override
    @SuppressWarnings("unchecked") 
    public List<?> read(String filePath) throws Exception { // propagar el posible error

        String contenido = new JSONArray().toString();
        filePath = Helpers.initPath(filePath, ".json");

        if (Helpers.fileExists(filePath)) {
            contenido = new String(Files.readAllBytes(Paths.get(filePath)));
        }

        List<T> lst = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(contenido);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            lst.add((T) o);
            
        }
        return lst;
    }

    @Override
    public void save(List<T> list, String filePath) throws Exception {
        filePath = Helpers.initPath(filePath, ".json");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(new JSONArray(list).toString());
        writer.flush();
        writer.close();
    }
    
}
