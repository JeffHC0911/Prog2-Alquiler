package alquiler.resources;

import java.util.List;


public interface Persistible <T> {

    public void save(List<T> list, String filePath )throws Exception;

    public List<?> read(String path) throws Exception;

    
}
