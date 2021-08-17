package alquiler.model;

import org.json.JSONObject;

public class EmpresaPropietaria {

    private String nit;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private Empleado gerente;

    public EmpresaPropietaria() {

    }

    public EmpresaPropietaria(String nit, String nombre, String direccion, String telefono, String correo,
            Empleado gerente) {
        this.nit = nit;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.gerente = gerente;

    }

    public EmpresaPropietaria(EmpresaPropietaria ep) {
        this(ep.nit, ep.nombre, ep.direccion, ep.telefono, ep.correo, ep.gerente);
    }

    public EmpresaPropietaria(String strData) {
        this(new JSONObject(strData));
    }

    public EmpresaPropietaria(JSONObject data) {
        this(data.getString("nit"), data.getString("nombre"), data.getString("direccion"), data.getString("telefono"),
                data.getString("correo"), new Empleado(data.getJSONObject("gerente")));

    }

    public String getNit() {
        return this.nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Empleado getGerente() {
        return this.gerente;
    }

    public void setGerente(Empleado gerente) {
        this.gerente = gerente;

    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("nit", getNit()).put("nombre", nombre).put("direccion", direccion)
                .put("telefono", telefono).put("correo", correo).put("gerente", gerente.toJSONObject());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EmpresaPropietaria)) {
            return false;
        }
        EmpresaPropietaria ep = (EmpresaPropietaria) o;
        return this.nit.equals(ep.nit);
    }

    @Override
    public String toString() {
        return String.format("NIT: %s - Nombre empresa: %s - Dir: %s - %nTeléfono: %s - Correo: %s - %nGERENTE: %s ",
                getNit(), getNombre(), getDireccion(), getTelefono(), getCorreo(), getGerente());
    }

}
