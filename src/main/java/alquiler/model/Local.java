package alquiler.model;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Local extends Inmueble {

    private boolean conBanio;
    private boolean conBodega;
    private Inmueble inmueble; // inmueble al que está asociado
    private TipoLocal tipoLocal;

    public Local() {
    }

    public Local(Inmueble inmueble, String codigo, String codigoPostal, String direccion, double area,
            double valorArriendo, boolean disponible, String descripcion, TipoLocal tipoLocal, boolean conBanio,
            boolean conBodega) {
        super(codigo, codigoPostal, direccion, area, valorArriendo, disponible, descripcion, inmueble.administrador,
                TipoInmueble.CONSTRUCCION, inmueble.propietaria);

        this.inmueble = inmueble;
        this.conBanio = conBanio;
        this.conBodega = conBodega;
        this.tipoLocal = tipoLocal;
    }

    public Local(Local l) {
        this(l.inmueble, l.codigo, l.codigoPostal, l.direccion, l.area, l.valorArriendo, l.disponible, l.descripcion,
                l.tipoLocal, l.conBanio, l.conBodega);
    }

    public Local(String strData) throws JSONException, ParseException {
        this(new JSONObject(strData));
    }

    public Local(JSONObject data) throws JSONException, ParseException {
        this(new Inmueble(data.getJSONObject("inmueble")), data.getString("codigo"), data.getString("codigoPostal"),
                data.getString("direccion"), data.getDouble("area"), data.getDouble("valorArriendo"),
                data.getBoolean("disponible"), data.getString("descripcion"),
                data.getEnum(TipoLocal.class, "tipoLocal"), data.getBoolean("conBanio"), data.getBoolean("conBodega"));
    }

    public boolean isConBanio() {
        return this.conBanio;
    }

    public boolean getConBanio() {
        return this.conBanio;
    }

    public void setConBanio(boolean conBanio) {
        this.conBanio = conBanio;
    }

    public boolean isConBodega() {
        return this.conBodega;
    }

    public boolean getConBodega() {
        return this.conBodega;
    }

    public void setConBodega(boolean conBodega) {
        this.conBodega = conBodega;
    }

    public Inmueble getInmueble() {
        return this.inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public TipoLocal getTipoLocal() {
        return this.tipoLocal;
    }

    public void setTipoLocal(TipoLocal tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public JSONObject toJSONObject() {
        return super.toJSONObject().put("inmueble", inmueble.toJSONObject()).put("conBanio", conBanio)
                .put("conBodega", conBodega).put("tipoLocal", tipoLocal);
    }

    public String toCSV() {
        String hayBanio = conBanio ? "Si" : "No";
        String hayBodega = conBodega ? "Si" : "No";
        String esDisponible = disponible ? "Si" : "No";
        return String.format("%s;%s;%s;%.0f;%.0f;%s;%s;%s;%s;%s;%s", getCodigo(), getCodigoPostal(), getDireccion(),
                getArea(), getValorArriendo(), esDisponible, getDescripcion(), getTipoLocal(), hayBodega, hayBanio,
                getInmueble().getCodigo());
    }

    @Override
    public String toString() {
        String hayBanio = conBanio ? "Si" : "No";
        String hayBodega = conBodega ? "Si" : "No";
        String esDisponible = disponible ? "Si" : "No";
        return String.format("%-7s %-6s %-6s %-30s %4.0f %10.0f %-3s %-27s %-20s %-3s %s", getInmueble().getCodigo(),
                getCodigo(), getCodigoPostal(), getDireccion(), getArea(), getValorArriendo(), esDisponible,
                getDescripcion(), getTipoLocal(), hayBodega, hayBanio);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Local)) {
            return false;
        }
        Local local = (Local) o;
        return this.codigo.equals(local.codigo);
    }

}
