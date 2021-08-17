package alquiler.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import alquiler.model.Aval;
import alquiler.model.TipoAval;
import alquiler.resources.Helpers;
import alquiler.resources.Persistible;

public class CtrlAval implements Controller<Aval> {

    private List<Aval> lstAvales;
    private CtrlContrato ctrlContrato;
    private final String RUTA_ARCHIVO = Helpers.RUTA + "avales";
    private Persistible<Aval> file;

    public CtrlAval(Persistible<Aval> file, CtrlContrato ctrlContrato) throws Exception {
        this.ctrlContrato = ctrlContrato;
        System.out.printf("Gestionando archivos con la clase \"%s\"%n", file.getClass().getSimpleName());
        this.file = file;
        lstAvales = list(this.file.read(RUTA_ARCHIVO));
    }

    @Override
    public List<Aval> list(List<?> items) throws ParseException {
        if (file.getClass().getSimpleName().equals("FileJSON")) {
            return listFromJSON(items);
        } else if (file.getClass().getSimpleName().equals("FileText")) {
            return listFromText(items);
        } else {
            return listFromCSV(items);
        }
    }

    private List<Aval> listFromJSON(List<?> jsonArray) throws ParseException {
        List<Aval> lst = new ArrayList<>();
        for (Object obj : jsonArray) {
            lst.add(new Aval((JSONObject) obj));
        }
        return lst;
    }

    private List<Aval> listFromText(List<?> textArray) throws ParseException {
        List<Aval> lst = new ArrayList<>();
        for (Object obj : textArray) {

            String linea = (String) obj;
            String codigoContrato = linea.substring(0,6).trim();
            TipoAval tipoAval = TipoAval.valueOf(linea.substring(7, 20).trim());
            Calendar fecha = Helpers.getFecha(linea.substring(21, 32).trim());
            String detalle = linea.substring(33).trim();
            
            int iContrato = ctrlContrato.indexOf(String.format("{\"codigo\":\"%s\"}", codigoContrato));

            lst.add(new Aval(fecha, detalle, tipoAval, ctrlContrato.get(iContrato)));
        }
        return lst;
    }

    private List<Aval> listFromCSV(List<?> textArray) throws ParseException {
        List<Aval> lst = new ArrayList<>();
        for (Object obj : textArray) {
            String[] data = (String[]) obj;

            String codigoContrato = data[0];
            TipoAval tipoAval = TipoAval.valueOf(data[2]);
            Calendar fecha = Helpers.getFecha(data[3]);


            int indiceContrato = ctrlContrato.indexOf(String.format("{\"codigo\":\"%s\"}", codigoContrato));
            
            lst.add(new Aval(fecha, data[1], tipoAval, ctrlContrato.get(indiceContrato)));
        }
        return lst;
    }

    @Override
    public List<Aval> list() {
        return lstAvales;
    }

    @Override
    public boolean add(JSONObject data) throws Exception {
        boolean estado = true;
        if (indexOf(data) > 1) {
            System.out.printf("El aval con detalle \"%s\" ya existe%n%n", data.getString("detalle"));
            estado = false;
        } else {
            estado = lstAvales.add(new Aval(data));
            file.save(lstAvales, RUTA_ARCHIVO);
            System.out.println("Aval agregado con éxito");
        }
        return estado;
    }

    @Override
    public boolean add(String strData) throws Exception {
        return add(new JSONObject(strData));
    }

    @Override
    public int indexOf(JSONObject data) {
        int i = -1;
        String buscado = data.getString("detalle");

        for (Aval aval : lstAvales) {
            if (aval.getDetalle().equals(buscado)) {
                i = lstAvales.indexOf(aval);
                break;
            }
        }
        return i;
    }

    @Override
    public int indexOf(String strData) {
        return indexOf(new JSONObject(strData));
    }

    @Override
    public Aval get(int indice) {
        return lstAvales.get(indice);
    }

    @Override
    public Aval set(int indice, JSONObject data) throws Exception {
        Aval aval = lstAvales.set(indice, new Aval(data));
        file.save(lstAvales, RUTA_ARCHIVO);
        return aval;
    }

    @Override
    public Aval set(int indice, String strData) throws Exception {
        return set(indice, new JSONObject(strData));
    }

    @Override
    public Aval remove(int indice) throws Exception {
        Aval aval = lstAvales.remove(indice);
        file.save(lstAvales, RUTA_ARCHIVO);
        return aval;
    }

    @Override
    public int size() {
        return lstAvales.size();
    }

}
