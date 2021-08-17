package alquiler.resources;

/**
 * Sólo las clases que implementen esta interfaz podrán utilizarse 
 * para exportar a formato CSV sin dar muchas vueltas.
 * Vea el cast en FileCSV.save(...)
 * Si dicho cast no se hubiera hecho, no se tendría acceso al método toCSV
 */

public interface Exportable {
    public String toCSV();  
}
