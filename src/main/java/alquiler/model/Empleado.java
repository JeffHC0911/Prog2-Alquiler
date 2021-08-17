package alquiler.model;

import org.json.JSONObject;

public class Empleado extends Persona {

    private Perfil perfil;

    public Empleado() {
        // constructor por defecto
    }

    // c. parametrizado
    public Empleado(String identificacion, String nombre, String telefono, String correo, Perfil perfil) {
        super(identificacion, nombre, telefono, correo);
        this.perfil = perfil;
    }

    // c. copia
    public Empleado(Empleado e) {
        this(e.identificacion, e.nombre, e.telefono, e.correo, e.perfil);
    }

    public Empleado(String strData) {
        this(new JSONObject(strData));
    }

    public Empleado(JSONObject data){
        super(
            data.getString("identificacion"), 
            data.getString("nombre"), 
            data.getString("telefono"), 
            data.getString("correo"));
        this.perfil = data.getEnum(Perfil.class, "perfil");
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public JSONObject toJSONObject() {
        return super.toJSONObject().put("perfil", perfil);
    }

    public String toCSV() {
        return String.format(
            "%s;%s;%s;%s;%s", identificacion, nombre, telefono, correo, perfil);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" %s", perfil);
    }
    
}
