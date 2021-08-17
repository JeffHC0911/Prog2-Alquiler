package alquiler.model;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Apartamento extends Inmueble {

    private Inmueble inmueble;

    public Apartamento() {

    }

    public Apartamento(Inmueble inmueble, String codigo, String codigoPostal, String direccion, double area,
            double valorArriendo, boolean disponible, String descripcion) {
        super(codigo, codigoPostal, direccion, area, valorArriendo, disponible, descripcion, inmueble.administrador,
                TipoInmueble.CONSTRUCCION, inmueble.propietaria);
        this.inmueble = inmueble;

    }

    public Apartamento(Apartamento a) {
        this(a.inmueble, a.codigo, a.codigoPostal, a.direccion, a.area, a.valorArriendo, a.disponible, a.descripcion);
    }

    public Apartamento(String strData) throws JSONException, ParseException {
        this(new JSONObject(strData));
    }

    public Apartamento(JSONObject data) throws JSONException, ParseException {
        this(new Inmueble(data.getJSONObject("inmueble")), data.getString("codigo"), data.getString("codigoPostal"),
                data.getString("direccion"), data.getDouble("area"), data.getDouble("valorArriendo"),
                data.getBoolean("disponible"), data.getString("descripcion"));
    }

    public Inmueble getInmueble() {
        return this.inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public JSONObject toJSONObject() {
        return super.toJSONObject().put("inmueble", inmueble.toJSONObject());
    }

    public String toCSV() {
        String esDisponible = disponible ? "Si" : "No";
        return String.format("%s;%s;%s;%.0f;%.0f;%s;%s;%s", getCodigo(), getCodigoPostal(), getDireccion(), getArea(),
                getValorArriendo(), esDisponible, getDescripcion(), getInmueble().getCodigo());
    }

    @Override
    public String toString() {
        String esDisponible = disponible ? "Si" : "No";
        return String.format("%-7s %-6s %-6s %-30s %-4.0f %-10.0f %-3s %s", getInmueble().getCodigo(), getCodigo(), getCodigoPostal(),
                getDireccion(), getArea(), getValorArriendo(), esDisponible, getDescripcion());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Apartamento)) {
            return false;
        }
        Apartamento apar = (Apartamento) o;
        return this.codigo.equals(apar.codigo);
    }

}
