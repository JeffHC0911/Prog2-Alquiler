package alquiler.controller;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import alquiler.model.Arrendatario;
import alquiler.model.Empleado;
import alquiler.model.Perfil;
import alquiler.model.Persona;
import alquiler.resources.Helpers;
import alquiler.resources.Persistible;

public class CtrlPersona implements Controller<Persona> {

    private Class<? extends Persona> subclase;
    private List<Persona> lstPersonas;
    private Persistible<Persona> file;

    public CtrlPersona(Persistible<Persona> file) throws Exception {
        System.out.printf("Gestionando archivos con la clase \"%s\"%n", file.getClass().getSimpleName());
        this.file = file;
        initList(file);
    }

    /**
     * Agrega las listas de estudiantes y profesores a una sola lista.
     * 
     * @param file Una instancia de archivo parametrizada para persistencia de
     *             personas
     * @throws Exception
     */
    private void initList(Persistible<Persona> file) throws Exception {
        this.file = file;

        subclase = Arrendatario.class;
        lstPersonas = list(this.file.read(getFilePath()));

        subclase = Empleado.class;
        lstPersonas.addAll(list(this.file.read(getFilePath())));
    }

    @Override
    public List<Persona> list() {
        return lstPersonas;
    }

    /**
     * Recibe una lista de personas como argumento y dependiendo del tipo de archivo
     * de origen delega a métodos auxiliares su procesamiento para que la conviertan
     * en una lista de instancias de Java.
     * 
     * @return Una lista de personas creada a partir de JSON, archivos planos o CSV
     * @throws ParseException
     */
    @Override
    public List<Persona> list(List<?> list) throws ParseException {
        if (file.getClass().getSimpleName().equals("FileJSON")) {
            return listFromJSON(list);
        } else if (file.getClass().getSimpleName().equals("FileText")) {
            return listFromText(list);
        } else {
            return listFromCSV(list);
        }
    }

    /**
     * Convierte una lista de JSONObjects a una lista de Estudiantes o Profesores
     * 
     * @param jsonList La lista con JSONObjects
     * @return Una lista de Estudiantes o Profesores según la referencia actual en
     *         "clase"
     * @throws ParseException
     * @throws Exception
     */
    private List<Persona> listFromJSON(List<?> jsonList) throws ParseException {
        List<Persona> lst = new ArrayList<>();

        if (subclase.getSimpleName().equals("Arrendatario")) {
            for (Object obj : jsonList) {
                JSONObject jsonObj = (JSONObject) obj;
                lst.add(new Arrendatario(jsonObj));
            }
        } else if (subclase.getSimpleName().equals("Empleado")) {
            for (Object obj : jsonList) {
                JSONObject jsonObj = (JSONObject) obj;
                lst.add(new Empleado(jsonObj));
            }
        } else {
            System.out.println("Error: no ha establecido una clase Profesor o Estudiante");
        }

        return lst;
    }

    /**
     * Convierte una lista de Strings a una lista de Estudiantes o Profesores
     * 
     * @param textArray La lista líneas de texto
     * @return Una lista de Estudiantes o Profesores según la referencia actual en
     *         "clase"
     * @throws ParseException
     * @throws Exception
     */
    private List<Persona> listFromText(List<?> textArray) throws ParseException {
        List<Persona> lst = new ArrayList<>();

        if (subclase.getSimpleName().equals("Arrendatario")) {
            for (Object obj : textArray) {
                String linea = (String) obj;
                String identificacion = linea.substring(0, 7).trim();
                String nombre = linea.substring(8, 50).trim();
                String telefono = linea.substring(51, 62).trim();
                String correo = linea.substring(63, 88).trim();
                Calendar fechaNacimiento = Helpers.getFecha(linea.substring(89, 105).trim());
                char sexo = linea.substring(106, 109).trim().charAt(0);
                String foto = linea.substring(110).trim();
                lst.add(new Arrendatario(identificacion, nombre, telefono, correo, fechaNacimiento, sexo, foto));
            }
        } else if (subclase.getSimpleName().equals("Empleado")) {
            for (Object obj : textArray) {
                String linea = (String) obj;
                String identificacion = linea.substring(0, 7).trim();
                String nombre = linea.substring(8, 50).trim();
                String telefono = linea.substring(51, 62).trim();
                String correo = linea.substring(63, 88).trim();
                Perfil perfil = Perfil.valueOf(linea.substring(89).trim());
                lst.add(new Empleado(identificacion, nombre, telefono, correo, perfil));
            }
        } else {
            System.out.println("Error: no ha establecido una clase Arrendatario o Empleado");
        }
        return lst;
    }

    private List<Persona> listFromCSV(List<?> textArray) throws ParseException {
        List<Persona> lst = new ArrayList<>();

        if (subclase.getSimpleName().equals("Arrendatario")) {
            for (Object obj : textArray) {
                String[] data = (String[]) obj;
                Calendar fechaNacimiento = Helpers.getFecha(data[4]);
                char sexo = data[5].charAt(0);
                lst.add(new Arrendatario(data[0], data[1], data[2], data[3], fechaNacimiento, sexo, data[6]));
            }
        } else if (subclase.getSimpleName().equals("Empleado")) {
            for (Object obj : textArray) {
                String[] data = (String[]) obj;
                Perfil perfil = Perfil.valueOf(data[4]);
                lst.add(new Empleado(data[0], data[1], data[2], data[3], perfil));
            }
        } else {
            System.out.println("Error: no ha establecido una clase Arrendatario o Empleado");
        }
        return lst;
    }

    public void setSubclase(Class<? extends Persona> subclase) {
        this.subclase = subclase;
    }

    private Persona getPersona(JSONObject data) {
        String mensaje = "\n" + "-".repeat(80);
        mensaje += "\n>>>>>>>> Falta indicar el tipo <<<<<<<<<\n";
        mensaje += "Use ctrlXyz.setSubclase([Empleado|Arrendatario].class);\n";
        mensaje += "antes de usar el controlador de Personas para agregar o modificar instancias\n";
        mensaje += "-".repeat(80);

        System.out.println("=============================");
        System.out.println(data);
        System.out.println("=============================");

        try {
            if (subclase == null) {
                throw new NullPointerException(mensaje); //////////////////
            }
            Constructor<?> constructor = subclase.getConstructor(new Class[] { JSONObject.class });
            return subclase.cast(constructor.newInstance(data));
        } catch (Exception e) {
            e.printStackTrace();
            if (subclase != null) {
                System.out.printf("%n>>> Usando %s para crear la instancia <<<%n", subclase.getName());
            }
        }
        return null;
    }

    /**
     * Sobre carga el método list para filtrar un tipo determinado de la superclase
     * Persona
     * 
     * @param subclase El tipo de persona que deseamos listar
     * @return Una lista auxiliar de personas, con el tipo que se le indicó
     */
    public List<Persona> list(Class<? extends Persona> subclase) {
        List<Persona> lstAux = new ArrayList<>();

        for (Persona persona : lstPersonas) {
            if (persona.getClass().equals(subclase)) {
                lstAux.add(persona);
            }

        }
        return lstAux;
    }

    @Override
    public boolean add(JSONObject data) throws Exception {
        boolean estado = true;
        if (indexOf(data) > -1) {
            System.out.printf("%nLa persona \"%s\"  ya existe%n", data.getString("identificacion"));
            estado = false;
        } else {
            Persona p = getPersona(data);
            if (p != null) {
                estado = lstPersonas.add(p);
                List<Persona> lst = subclase.equals(Empleado.class) ? list(Empleado.class) : list(Arrendatario.class);
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
        int i = -1;
        String buscado;
        /*String buscado = data.getString("identificacion");
        for (Persona persona : lstPersonas) {
            if(persona.getIdentificacion().equals(buscado)){
                i = lstPersonas.indexOf(persona);
                break;
            }
        }
        return i;*/
        if (data.has("foto")) {
            buscado = data.getString("foto");
            for (Persona persona : lstPersonas) {
                if (persona instanceof Arrendatario) {
                    Arrendatario a = (Arrendatario) persona;
                    if (a.getFoto().equals(buscado)) {
                        i = lstPersonas.indexOf(a);
                        break;
                    }
                }
            }
            return i;
        }else{
            buscado = data.getString("identificacion");
            for (Persona persona : lstPersonas) {
                if (persona.getIdentificacion().equals(buscado)) {
                    i = lstPersonas.indexOf(persona);
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
    public Persona get(int indice) {
        return lstPersonas.get(indice);
    }

    @Override
    public Persona set(int indice, JSONObject data) throws Exception {
        Persona p = getPersona(data);
        /*
         * if (p != null) { p = lstPersonas.set(indice, p); file.save(lstPersonas,
         * RUTA_ARCHIVO); } return p;
         */
        List<Persona> lst = subclase.equals(Empleado.class) ? list(Empleado.class) : list(Arrendatario.class);
        file.save(lst, getFilePath());
        return p;
    }

    @Override
    public Persona set(int indice, String strData) throws Exception {
        return set(indice, new JSONObject(strData));
    }

    @Override
    public Persona remove(int indice) throws Exception {
        Persona persona = lstPersonas.remove(indice);
        List<Persona> lst = subclase.equals(Empleado.class) ? list(Empleado.class) : list(Arrendatario.class);
        file.save(lst, getFilePath());
        return persona;
    }

    @Override
    public int size() {
        return lstPersonas.size();
    }

    public String getFilePath() {
        if (subclase.getSimpleName().equals("Arrendatario")) {
            return Helpers.RUTA + "arrendatarios";
        } else if (subclase.getSimpleName().equals("Empleado")) {
            return Helpers.RUTA + "empleado";
        } else {
            return Helpers.RUTA + "personas";
        }
    }
}
