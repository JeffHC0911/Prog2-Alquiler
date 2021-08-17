package alquiler.controller;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import alquiler.model.Apartamento;
import alquiler.model.Empleado;
import alquiler.model.EmpresaPropietaria;
import alquiler.model.Inmueble;
import alquiler.model.Local;
import alquiler.model.Perfil;
import alquiler.model.TipoInmueble;
import alquiler.model.TipoLocal;
import alquiler.resources.Helpers;
import alquiler.resources.Persistible;

public class CtrlInmueble implements Controller<Inmueble> {

    private List<Inmueble> lstInmuebles;
    private CtrlPersona ctrlPersonas;
    private Class<? extends Inmueble> clase;
    private Persistible<Inmueble> file;

    public CtrlInmueble(Persistible<Inmueble> file, CtrlPersona ctrlPersonas) throws Exception {
        this.ctrlPersonas = ctrlPersonas;
        System.out.printf("Gestionando archivos con la clase \"%s\"%n", file.getClass().getSimpleName());
        initlist(file);
    }

    @Override
    public List<Inmueble> list() {
        return lstInmuebles;
    }

    private void initlist(Persistible<Inmueble> file) throws Exception {
        this.file = file;

        clase = Inmueble.class;
        lstInmuebles = list(file.read(getFilePath()));

        clase = Local.class;
        lstInmuebles.addAll(list(file.read(getFilePath())));

        clase = Apartamento.class;
        lstInmuebles.addAll(list(file.read(getFilePath())));
    }

    @Override
    public List<Inmueble> list(List<?> items) throws ParseException {
        if (file.getClass().getSimpleName().equals("FileJSON")) {
            return listFromJSON(items);
        } else if (file.getClass().getSimpleName().equals("FileText")) {
            return listFromText(items);
        } else {
            return listFromCSV(items);
        }
    }

    private List<Inmueble> listFromJSON(List<?> jsonList) throws ParseException {
        List<Inmueble> lst = new ArrayList<>();

        if (clase.getSimpleName().equals("Inmueble")) {
            for (Object obj : jsonList) {
                JSONObject jsonObj = (JSONObject) obj;
                lst.add(new Inmueble(jsonObj));
            }
        } else if (clase.getSimpleName().equals("Apartamento")) {
            for (Object obj : jsonList) {
                JSONObject jsonObj = (JSONObject) obj;
                lst.add(new Apartamento(jsonObj));
            }
        } else if (clase.getSimpleName().equals("Local")) {
            for (Object obj : jsonList) {
                JSONObject jsonObj = (JSONObject) obj;
                lst.add(new Local(jsonObj));
            }
        } else {
            System.out.println("Error: no ha establecido una clase Inmueble, Apartamento o Local");
        }
        return lst;
    }

    private List<Inmueble> listFromText(List<?> textArray) {
        List<Inmueble> lst = new ArrayList<>();

        if (clase.getSimpleName().equals("Apartamento")) {
            for (Object obj : textArray) {
                String linea = (String) obj;
                String codigoInmueble = linea.substring(0,7).trim();
                String codigo = linea.substring(8, 14).trim();
                String codigoPostal = linea.substring(15, 21).trim();
                String direccion = linea.substring(22, 52).trim();
                double area = Helpers.stringToDouble(linea.substring(53, 57).trim());
                double valorArriendo = Helpers.stringToDouble(linea.substring(58, 68).trim());
                boolean disponible = linea.substring(69, 72).trim().equals("Si") ? true : false;
                String descripcion = linea.substring(73).trim();

                int indiceInm = indexOf(String.format("{\"codigo\":\"%s\"}", codigoInmueble));

                lst.add(new Apartamento(get(indiceInm), codigo, codigoPostal, direccion, area, valorArriendo,
                        disponible, descripcion));
            }
        } else if (clase.getSimpleName().equals("Local")) {
            for (Object obj : textArray) {
                String linea = (String) obj;
                String codigoInmueble = linea.substring(0,7).trim();
                String codigo = linea.substring(8, 14).trim();
                String codigoPostal = linea.substring(15, 21).trim();
                String direccion = linea.substring(22, 52).trim();
                double area = Helpers.stringToDouble(linea.substring(53, 57).trim());
                double valorArriendo = Helpers.stringToDouble(linea.substring(58, 68).trim());
                boolean disponible = linea.substring(69, 72).trim().equals("Si") ? true : false;
                String descripcion = linea.substring(73, 100).trim();
                TipoLocal tipoLocal = TipoLocal.valueOf(linea.substring(101, 121).trim());
                boolean conBanio = linea.substring(122, 125).trim().equals("Si") ? true : false;
                boolean conBodega = linea.substring(126).trim().equals("Si") ? true : false;

                int indiceInmueble = indexOf(String.format("{\"codigo\":\"%s\"}", codigoInmueble));

                lst.add(new Local(get(indiceInmueble), codigo, codigoPostal, direccion, area, valorArriendo, disponible,
                        descripcion, tipoLocal, conBanio, conBodega));
            }
        } else if (clase.getSimpleName().equals("Inmueble")) {
            for (Object obj : textArray) {
                String linea = (String) obj;
                String codigo = linea.substring(0, 6).trim();
                String codigoPostal = linea.substring(7, 15).trim();
                String direccion = linea.substring(16, 36).trim();
                double area = Helpers.stringToDouble(linea.substring(37, 44).trim());
                double valorArriendo = Helpers.stringToDouble(linea.substring(45, 54).trim());
                boolean disponible = linea.substring(55, 58).trim().equals("Si") ? true : false;
                String descripcion = linea.substring(59, 91).trim();
                String idenEmpleado = linea.substring(92, 117).trim();
                TipoInmueble tipoInmueble = TipoInmueble.valueOf(linea.substring(118).trim());

                int iEmpleado = ctrlPersonas.indexOf(String.format("{\"identificacion\":\"%s\"}", idenEmpleado));
                Empleado admin = (Empleado) ctrlPersonas.get(iEmpleado);

                Empleado gerente = new Empleado("E00", "Mario Casas", "3014567812", "macasa@gmail.com", Perfil.GERENTE);
                EmpresaPropietaria propietaria = new EmpresaPropietaria("0001111", "INMUEBLES S.A", "Bogotá D.C", "018009945",
                "inmusa@gmail.com", gerente);

                lst.add(new Inmueble(codigo, codigoPostal, direccion, area, valorArriendo, disponible, descripcion,
                        admin, tipoInmueble, propietaria));
            }
        } else {
            System.out.println("Error: no ha establecido una clase Inmueble, Apartamento o Local");
        }
        return lst;
    }

    private List<Inmueble> listFromCSV(List<?> textArray) {
        List<Inmueble> lst = new ArrayList<>();
        if (clase.getSimpleName().equals("Apartamento")) {
            for (Object obj : textArray) {
                String[] data = (String[]) obj;
                double area = Helpers.stringToDouble(data[3]);
                double valorArriendo = Helpers.stringToDouble(data[4]);
                boolean disponible = data[5].equals("Si") ? true : false;

                int indexInmueble = indexOf(String.format("{\"codigo\":\"%s\"}", data[7]));

                lst.add(new Apartamento(lstInmuebles.get(indexInmueble), data[0], data[1], data[2], area,
                        valorArriendo, disponible, data[6]));
            }
        } else if (clase.getSimpleName().equals("Local")) {
            for (Object obj : textArray) {
                String[] data = (String[]) obj;
                boolean disponible = data[6].equals("Si") ? true : false;
                TipoLocal tipoLocal = TipoLocal.valueOf(data[7]);
                boolean conBanio = data[8].equals("Si") ? true : false;
                boolean conBodega = data[9].equals("Si") ? true : false;

                int indexInmueble = indexOf(String.format("{\"codigo\":\"%s\"}", data[10]));
                
                lst.add(new Local(lstInmuebles.get(indexInmueble), data[0], data[1], data[2], Helpers.stringToDouble(data[3]), Helpers.stringToDouble(data[4]),
                        disponible, data[6], tipoLocal, conBanio, conBodega));
            }
        } else if (clase.getSimpleName().equals("Inmueble")) {
            for (Object obj : textArray) {
                String[] data = (String[]) obj;
                double area = Helpers.stringToDouble(data[3]);
                double valorArriendo = Helpers.stringToDouble(data[4]);
                boolean disponible = data[5].equals("Si") ? true : false;
                int indexAdmin = ctrlPersonas.indexOf(String.format("{\"identificacion\":\"%s\"}", data[7]));
                TipoInmueble tipoInmueble = TipoInmueble.valueOf(data[8]);

                Empleado gerente = new Empleado("G01", "Luisa Grisales", "0000000", "sdf", Perfil.GERENTE);
                EmpresaPropietaria propietaria = new EmpresaPropietaria("000", "Inmuebles", "dds", "311221345",
                        "juejue@gmail.com", gerente);


                lst.add(new Inmueble(data[0], data[1], data[2], area, valorArriendo, disponible, data[6],
                        (Empleado) ctrlPersonas.get(indexAdmin), tipoInmueble, propietaria));
            }
        }
        return lst;
    }

    /**
     * Sobrecarga para filtar un inmueble determinado
     * 
     * @param clase El inmueble a listar. Ej: Local.class
     * @return Una sublista de la lista Inmuebles
     */

    public List<Inmueble> list(Class<? extends Inmueble> clase) {
        List<Inmueble> lstAux = new ArrayList<>();

        for (Inmueble inmueble : lstInmuebles) {
            if (inmueble.getClass().equals(clase)) {
                lstAux.add(inmueble);
            }
        }
        return lstAux;
    }

    @Override
    public boolean add(JSONObject data) throws Exception {
        boolean estado = true;
        if (indexOf(data) > -1) {
            System.out.println("Lo sentimos, el inmueble con este código ya existe");
            estado = false;
        } else {
            Inmueble inm = getInmueble(data);
            if (inm != null) {
                estado = lstInmuebles.add(inm);
                List<Inmueble> lst;
                if (clase.equals(Inmueble.class)) {
                    lst = list(Inmueble.class);
                } else if (clase.equals(Apartamento.class)) {
                    lst = list(Apartamento.class);
                } else {
                    lst = list(Local.class);
                }
                file.save(lst, getFilePath());
            } else {
                System.out.println("No se pudo agregar la instancia");
            }
        }
        return estado;
    }

    @Override
    public boolean add(String strData) throws Exception {
        return add(new JSONObject(strData));
    }

    @Override
    public int indexOf(JSONObject data) {

        /*int i = -1;
        String buscado = data.getString("codigo");
        for (Inmueble inmueble : lstInmuebles) {
            if (buscado.equals(inmueble.getCodigo())) {
                i = lstInmuebles.indexOf(inmueble);
                break;
            }
        }
        return i;*/

        int i = -1;
        String buscado;
        if(data.has("conBanio")){
            buscado = data.getString("codigo");
            for (Inmueble lo : lstInmuebles) {
                if(lo instanceof Local){
                    if(lo.getCodigo().equals(buscado)){
                        i = lstInmuebles.indexOf(lo);
                        break;
                    }
                }
            }
            return i;
        }else if(data.has("inmueble")){
            buscado = data.getString("codigo");
            for (Inmueble apar : lstInmuebles) {
                if(apar instanceof Apartamento){
                    if(apar.getCodigo().equals(buscado)){
                        i = lstInmuebles.indexOf(apar);
                        break;
                    }
                }
            }
            return i;
        }else{
            buscado = data.getString("codigo");
            for (Inmueble inmueble : lstInmuebles) {
                if(inmueble.getCodigo().equals(buscado)){
                    i = lstInmuebles.indexOf(inmueble);
                    break;
                }
            }
            return i;
        }
    }

    @Override
    public int indexOf(String strData) {
        return indexOf(new JSONObject(strData));
    }

    @Override
    public Inmueble get(int indice) {
        return lstInmuebles.get(indice);
    }

    @Override
    public Inmueble set(int indice, JSONObject data) throws Exception {
        Inmueble inmueble = getInmueble(data);
        if (inmueble != null) {
            inmueble = lstInmuebles.set(indice, inmueble);
            List<Inmueble> lst;
            if (clase.equals(Inmueble.class)) {
                lst = list(Inmueble.class);
            } else if (clase.equals(Apartamento.class)) {
                lst = list(Apartamento.class);
            } else {
                lst = list(Local.class);
            }
            file.save(lst, getFilePath());
        }
        return inmueble;
    }

    @Override
    public Inmueble set(int indice, String strData) throws Exception {
        return set(indice, new JSONObject(strData));
    }

    @Override
    public Inmueble remove(int indice) throws Exception {
        Inmueble inmueble = lstInmuebles.remove(indice);
        List<Inmueble> lst;
        if (clase.equals(Inmueble.class)) {
            lst = list(Inmueble.class);
        } else if (clase.equals(Apartamento.class)) {
            lst = list(Apartamento.class);
        } else {
            lst = list(Local.class);
        }

        file.save(lst, getFilePath());
        return inmueble;
    }

    @Override
    public int size() {
        return lstInmuebles.size();
    }

    public void setClase(Class<? extends Inmueble> clase) {
        this.clase = clase;
    }

    private Inmueble getInmueble(JSONObject data) {
        String mensaje = "\n" + "-".repeat(80);
        mensaje += "\n>>>>>>>> Falta indicar el tipo <<<<<<<<<\n";
        mensaje += "Use ctrlXyz.setSubclase([Inmueble|Apartamento|Local].class);\n";
        mensaje += "antes de usar el controlador de Inmuebles para agregar o modificar instancias\n";
        mensaje += "-".repeat(80);

        try {
            if (clase == null) {
                throw new Exception(mensaje);
            }
            Constructor<?> constructor = clase.getConstructor(new Class[] { JSONObject.class });
            return clase.cast(constructor.newInstance(data));
        } catch (Exception e) {
            e.printStackTrace();
            if (clase != null) {
                System.out.printf("%n>>> Usando %s para crear la instancia <<<%n", clase.getSimpleName());
            }
        }
        return null;
    }

    public String getFilePath() {
        if (clase.getSimpleName().equals("Apartamento")) {
            return Helpers.RUTA + "apartamentos";
        } else if (clase.getSimpleName().equals("Local")) {
            return Helpers.RUTA + "locales";
        } else {
            return Helpers.RUTA + "inmuebles";
        }
    }

}
