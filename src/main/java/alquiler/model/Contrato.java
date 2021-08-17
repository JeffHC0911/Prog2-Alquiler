package alquiler.model;

import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import alquiler.resources.Exportable;
import alquiler.resources.Helpers;

public class Contrato implements Exportable {

    private String codigo;
    private Calendar fechaFirma;
    private Calendar fechaInicio;
    private Calendar fechaFin;
    private String terminos;
    private Inmueble inmueble;
    private Arrendatario arrendatario;

    public Contrato() {

    }

    public Contrato(String codigo, Calendar fechaFirma, Calendar fechaInicio, Calendar fechaFin, String terminos,
            Inmueble inmueble, Arrendatario arrendatario) {
        this.codigo = codigo;
        this.fechaFirma = fechaFirma;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.terminos = terminos;
        this.inmueble = inmueble;
        this.arrendatario = arrendatario;
    }

    public Contrato(Contrato c) {
        this(c.codigo, c.fechaFirma, c.fechaInicio, c.fechaFin, c.terminos, c.inmueble, c.arrendatario);
    }

    public Contrato(String strData) throws JSONException, ParseException {
        this(new JSONObject(strData));
    }

    public Contrato(JSONObject data) throws JSONException, ParseException {
        this(data.getString("codigo"), Helpers.getFecha(data.getString("fechaFirma")),
                Helpers.getFecha(data.getString("fechaInicio")), Helpers.getFecha(data.getString("fechaFin")),
                data.getString("terminos"), new Inmueble(data.getJSONObject("inmueble")),
                new Arrendatario(data.getJSONObject("arrendatario")));

    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFechaFirma() {
        return Helpers.strFecha(fechaFirma);
    }

    public void setFechaFirma(Calendar fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public String getFechaInicio() {
        return Helpers.strFecha(fechaInicio);
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return Helpers.strFecha(fechaFin);
    }

    public void setFechaFin(Calendar fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTerminos() {
        return this.terminos;
    }

    public void setTerminos(String terminos) {
        this.terminos = terminos;
    }

    public Inmueble getInmueble() {
        return this.inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public Arrendatario getArrendatario() {
        return this.arrendatario;
    }

    public void setArrendatario(Arrendatario arrendatario) {
        this.arrendatario = arrendatario;
    }

    public int mesesArriendo() {
        return (int) Helpers.mesesIntervalo(fechaFin, fechaInicio);
    }

    public double valorArriendo() {
        return inmueble.getValorArriendo() * Helpers.mesesIntervalo(fechaFin, fechaInicio);
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("codigo", codigo).put("fechaFirma", Helpers.strFecha(fechaFirma))
                .put("fechaInicio", Helpers.strFecha(fechaInicio)).put("fechaFin", Helpers.strFecha(fechaFin))
                .put("terminos", terminos).put("inmueble", inmueble.toJSONObject())
                .put("arrendatario", arrendatario.toJSONObject());
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s;%s;%s;%s;%s;%s", getCodigo(), Helpers.strFecha(fechaFirma),
                Helpers.strFecha(fechaInicio), Helpers.strFecha(fechaFin), terminos, inmueble.getCodigo(),
                arrendatario.getIdentificacion());
    }

    @Override
    public String toString() {
        return String.format("%-6s %7s %-7s %-7s %-29s %-6s %s", getCodigo(), Helpers.strFecha(fechaFirma),
                Helpers.strFecha(fechaInicio), Helpers.strFecha(fechaFin), getTerminos(), getInmueble().getCodigo(),
                getArrendatario().getIdentificacion());

    }
}
