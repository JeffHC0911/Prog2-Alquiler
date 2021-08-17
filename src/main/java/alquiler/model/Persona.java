package alquiler.model;

import org.json.JSONObject;

import alquiler.resources.Exportable;

public abstract class Persona implements Exportable{

    protected String identificacion;
    protected String nombre;
    protected String telefono;
    protected String correo;

    // constructor por defecto
    public Persona() {
    }

    // constructor parametrizado
    public Persona(String identificacion, String nombre, String telefono, String correo) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    // constructor copia
    public Persona(Persona p) {
        this(p.identificacion, p.nombre, p.telefono, p.correo);
    }

    public Persona(String strData) {
        this(new JSONObject(strData));
    }

    public Persona(JSONObject data) {
        this(data.getString("identificacion"), data.getString("nombre"), data.getString("telefono"),
                data.getString("correo"));
    }

    // accesor
    public String getIdentificacion() {
        return this.identificacion;
    }

    // mutador
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public JSONObject toJSONObject(){
        return new JSONObject()
            .put("identificacion", identificacion)
            .put("nombre", nombre)
            .put("telefono", telefono)
            .put("correo", correo);

    }

    @Override // anotación
    public String toString() {
        return String.format("%7s %-42s %11s %-25s", identificacion, nombre,
                telefono, correo);
    }

}
