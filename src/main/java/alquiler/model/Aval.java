package alquiler.model;

import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import alquiler.resources.Exportable;
import alquiler.resources.Helpers;

public class Aval implements Exportable {

    private Calendar fecha;
    private String detalle;
    private TipoAval tipoAval;
    private Contrato contrato;

    public Aval() {

    }

    public Aval(Calendar fecha, String detalle, TipoAval tipoAval, Contrato contrato) {
        this.fecha = fecha;
        this.detalle = detalle;
        this.tipoAval = tipoAval;
        this.contrato = contrato;

    }

    public Aval(Aval av) {
        this(av.fecha, av.detalle, av.tipoAval, av.contrato);
    }

    public Aval(String strData) throws JSONException, ParseException {
        this(new JSONObject(strData));
    }

    public Aval(JSONObject data) throws JSONException, ParseException {
        this.fecha = Helpers.getFecha(data.getString("fecha"));
        this.detalle = data.getString("detalle");
        this.tipoAval = data.getEnum(TipoAval.class, "tipoAval");
        this.contrato = new Contrato(data.getJSONObject("contrato"));

    }

    public String getFecha() {
        return Helpers.strFecha(fecha);
    }

    public void setCalendar(Calendar fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public TipoAval getTipoAval() {
        return this.tipoAval;
    }

    public void setTipoAval(TipoAval tipoAval) {
        this.tipoAval = tipoAval;
    }

    public Contrato getContrato() {
        return this.contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("fecha", Helpers.strFecha(fecha)).put("detalle", detalle).put("tipoAval", tipoAval)
                .put("contrato", contrato.toJSONObject());
    }

    @Override
    public String toCSV() {

        return String.format("%s;%s;%s;%s", getContrato().getCodigo(), getDetalle(), getTipoAval(),
                Helpers.strFecha(fecha));
    }

    @Override
    public String toString() {
        return String.format("%6s %-13s %-11s %s", getContrato().getCodigo(), getTipoAval(), Helpers.strFecha(fecha),
                getDetalle());
    }

}
