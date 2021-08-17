package alquiler.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import alquiler.model.Arrendatario;
import alquiler.model.Contrato;
import alquiler.resources.Helpers;
import alquiler.resources.Persistible;

public class CtrlContrato implements Controller<Contrato> {

    private List<Contrato> lstContratos;
    private CtrlInmueble ctrlInmueble;
    private CtrlPersona ctrlPersona;
    private final String RUTA_ARCHIVO = Helpers.RUTA + "contratos";
    private Persistible<Contrato> file;

    public CtrlContrato(Persistible<Contrato> file, CtrlInmueble ctrlInmueble, CtrlPersona ctrlPersona)
            throws Exception {
        this.ctrlInmueble = ctrlInmueble;
        this.ctrlPersona = ctrlPersona;
        System.out.printf("Gestionando archivos con la clase \"%s\"%n", file.getClass().getSimpleName());
        this.file = file;
        lstContratos = list(this.file.read(RUTA_ARCHIVO));
    }

    @Override
    public List<Contrato> list(List<?> items) throws ParseException {
        if (file.getClass().getSimpleName().equals("FileJSON")) {
            return listFromJSON(items);
        } else if (file.getClass().getSimpleName().equals("FileText")) {
            return listFromText(items);
        } else {
            return listFromCSV(items);
        }
    }

    private List<Contrato> listFromJSON(List<?> jsonArray) throws JSONException, ParseException {
        List<Contrato> lst = new ArrayList<>();
        for (Object obj : jsonArray) {
            lst.add(new Contrato((JSONObject) obj));
        }
        return lst;
    }

    private List<Contrato> listFromText(List<?> textArray) throws ParseException {
        List<Contrato> lst = new ArrayList<>();
        for (Object obj : textArray) {

            String linea = (String) obj;
            String codigo = linea.substring(0, 6).trim();
            Calendar fechaFirma = Helpers.getFecha(linea.substring(7, 17).trim());
            Calendar fechaInicio = Helpers.getFecha(linea.substring(18, 28).trim());
            Calendar fechaFin = Helpers.getFecha(linea.substring(29, 39).trim());
            String terminos = linea.substring(40, 65).trim();
            String codigoInmueble = linea.substring(70, 76).trim();
            String idenArrendatario = linea.substring(77).trim();

            int indiceInmueble = ctrlInmueble.indexOf(String.format("{\"codigo\":\"%s\"}", codigoInmueble));
            int indexArrendatario = ctrlPersona.indexOf(String.format("{\"identificacion\":\"%s\"}", idenArrendatario));
            


            lst.add(new Contrato(codigo, fechaFirma, fechaInicio, fechaFin, terminos, ctrlInmueble.get(indiceInmueble),
                    (Arrendatario)ctrlPersona.get(indexArrendatario)));
        }
        return lst;
    }

    private List<Contrato> listFromCSV(List<?> textArray) throws ParseException {
        List<Contrato> lst = new ArrayList<>();
        for (Object obj : textArray) {
            String[] data = (String[]) obj;
            Calendar fechaFirma = Helpers.getFecha(data[1]);
            Calendar fechaInicio = Helpers.getFecha(data[2]);
            Calendar fechaFin = Helpers.getFecha(data[3]);
            int indexInmueble = ctrlInmueble.indexOf(String.format("{\"codigo\":\"%s\"}", data[5]));
            int indexArrendatario = ctrlPersona.indexOf(String.format("{\"identificacion\":\"%s\"}", data[6]));

            lst.add(new Contrato(data[0], fechaFirma, fechaInicio, fechaFin, data[4], ctrlInmueble.get(indexInmueble),
                    (Arrendatario) ctrlPersona.get(indexArrendatario)));
        }
        return lst;
    }

    @Override
    public List<Contrato> list() {
        return lstContratos;
    }

    @Override
    public boolean add(JSONObject data) throws Exception {
        boolean estado = true;
        if (indexOf(data) > -1) {
            System.out.printf("%nEl contrato \"%s\" ya existe", data.getString("codigo"));
            estado = false;
        } else {
            estado = lstContratos.add(new Contrato(data));
            file.save(lstContratos, RUTA_ARCHIVO);
            System.out.println("Contrato ingresado con éxito");
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
        String buscado = data.getString("codigo");
        for (Contrato c : lstContratos) {
            if (c.getCodigo().equals(buscado)) {
                i = lstContratos.indexOf(c);
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
    public Contrato get(int indice) {
        return lstContratos.get(indice);
    }

    @Override
    public Contrato set(int indice, JSONObject data) throws Exception {
        Contrato c = lstContratos.set(indice, new Contrato(data));
        file.save(lstContratos, RUTA_ARCHIVO);
        return c;
    }

    @Override
    public Contrato set(int indice, String strData) throws Exception {
        return set(indice, new JSONObject(strData));
    }

    @Override
    public Contrato remove(int indice) throws Exception {
        Contrato c = lstContratos.remove(indice);
        file.save(lstContratos, RUTA_ARCHIVO);
        return c;
    }

    @Override
    public int size() {
        return lstContratos.size();
    }

}
