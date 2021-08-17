package alquiler.model;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.json.JSONObject;

import alquiler.resources.Exportable;

public class Inmueble implements Exportable {

    protected String codigo;
    protected String codigoPostal;
    protected String direccion;
    protected double area;
    protected double valorArriendo;
    protected boolean disponible;
    protected String descripcion;
    protected Empleado administrador;
    protected TipoInmueble tipoInmueble;
    protected EmpresaPropietaria propietaria;
    protected DecimalFormat decimalFormat = new DecimalFormat("###,###.##");

    public Inmueble() {
    }

    public Inmueble(String codigo, String codigoPostal, String direccion, double area, double valorArriendo,
            boolean disponible, String descripcion, Empleado administrador, TipoInmueble tipoInmueble,
            EmpresaPropietaria propietaria) {
        this.codigo = codigo;
        this.codigoPostal = codigoPostal;
        this.direccion = direccion;
        this.area = area;
        this.valorArriendo = valorArriendo;
        this.disponible = disponible;
        this.descripcion = descripcion;
        this.administrador = administrador;
        this.tipoInmueble = tipoInmueble;
        this.propietaria = propietaria;
    }

    public Inmueble(Inmueble i) {
        this(i.codigo, i.codigoPostal, i.direccion, i.area, i.valorArriendo, i.disponible, i.descripcion,
                i.administrador, i.tipoInmueble, i.propietaria);
    }

    public Inmueble(String strData) throws ParseException {
        this(new JSONObject(strData));
    }

    public Inmueble(JSONObject data) {
        this(data.getString("codigo"), data.getString("codigoPostal"), data.getString("direccion"),
                data.getDouble("area"), data.getDouble("valorArriendo"), data.getBoolean("disponible"),
                data.getString("descripcion"), new Empleado(data.getJSONObject("administrador")),
                data.getEnum(TipoInmueble.class, "tipoInmueble"),
                new EmpresaPropietaria(data.getJSONObject("propietaria")));

    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoPostal() {
        return this.codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getArea() {
        return this.area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getValorArriendo() {
        return this.valorArriendo;
    }

    public void setValorArriendo(double valorArriendo) {
        this.valorArriendo = valorArriendo;
    }

    public boolean isDisponible() {
        return this.disponible;
    }

    public boolean getDisponible() {
        return this.disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Empleado getAdministrador() {
        return this.administrador;
    }

    public void setAdministrador(Empleado administrador) {
        this.administrador = administrador;
    }

    public TipoInmueble getTipoInmueble() {
        return this.tipoInmueble;
    }

    public void setTipoInmueble(TipoInmueble tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public EmpresaPropietaria getPropietaria() {
        return this.propietaria;
    }

    public void setPropietaria(EmpresaPropietaria propietaria) {
        this.propietaria = propietaria;
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("codigo", codigo).put("codigoPostal", codigoPostal).put("direccion", direccion)
                .put("area", area).put("valorArriendo", valorArriendo).put("disponible", disponible)
                .put("descripcion", descripcion).put("administrador", administrador.toJSONObject())
                .put("tipoInmueble", tipoInmueble).put("propietaria", propietaria.toJSONObject());
    }

    public String toCSV() {
        String strDisponible = disponible ? "Si" : "No";
        return String.format("%s;%s;%s;%.0f;%.0f;%s;%s;%s;%s", codigo, codigoPostal, direccion, area, valorArriendo,
                strDisponible, descripcion, administrador.getIdentificacion(), tipoInmueble);
    }

    @Override
    public String toString() {
        String strDisponible = disponible ? "Si" : "No";
        return String.format("%-6s %-8s %-20s %-7.0f %-9.0f %-3s %-32s %-25s %s", getCodigo(), getCodigoPostal(),
                getDireccion(), getArea(), getValorArriendo(), strDisponible, getDescripcion(),
                getAdministrador().getIdentificacion(), getTipoInmueble());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Inmueble)) {
            return false;
        }
        Inmueble inmueble = (Inmueble) o;
        return this.codigo.equals(inmueble.codigo);
    }

}
