package alquiler.model;

import java.text.ParseException;
import java.util.Calendar;


import org.json.JSONObject;

import alquiler.resources.Helpers;

public class Arrendatario extends Persona {

    // definir atributos, constructores, accesores y mutadores

    private Calendar fechaNacimiento;
    private char sexo;
    private String foto;



    public Arrendatario() {

    }

    public Arrendatario(String identificacion, String nombre, String telefono, String correo, Calendar fechaNacimiento,
            char sexo, String foto) {
        super(identificacion, nombre, telefono, correo);
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.foto = foto;
    }

    public Arrendatario(Arrendatario arr) {
        this(arr.identificacion, arr.nombre, arr.telefono, arr.correo, arr.fechaNacimiento, arr.sexo, arr.foto);
    }

    public Arrendatario(String strData) throws ParseException {
        this(new JSONObject(strData));
    }

    public Arrendatario(JSONObject data) throws ParseException {
        super(
            data.getString("identificacion"), 
            data.getString("nombre"), 
            data.getString("telefono"),
            data.getString("correo"));
        this.fechaNacimiento = Helpers.getFecha(data.getString("fechaNacimiento"));
        this.sexo = data.getString("sexo").charAt(0);
        this.foto = data.getString("foto");

    }

    public String getFechaNacimiento() {
        return Helpers.strFecha(fechaNacimiento);
    }



    public void setFechaNacimiento(Calendar fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public char getSexo() {
        return this.sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getFoto() {
        return this.foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public JSONObject toJSONObject() {
        return super.toJSONObject()
        .put("fechaNacimiento", Helpers.strFecha(fechaNacimiento))
        .put("sexo",""+sexo)
        .put("foto", foto);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Arrendatario)) {
            return false;
        }
        Arrendatario a = (Arrendatario) o;
        return this.identificacion.equals(a.identificacion);
    }

    public String toCSV() {
        return String.format(
            "%s;%s;%s;%s;%s;%s;%s", identificacion, nombre, telefono, correo, Helpers.strFecha(fechaNacimiento), sexo, foto);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" %-16s %-3s %s", Helpers.strFecha(fechaNacimiento), getSexo() , getFoto());
    }
}
