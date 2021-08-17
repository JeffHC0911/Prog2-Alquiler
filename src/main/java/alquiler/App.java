package alquiler;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;

import alquiler.controller.CtrlAval;
import alquiler.controller.CtrlContrato;
import alquiler.controller.CtrlInmueble;
import alquiler.controller.CtrlPersona;
import alquiler.model.*;
import alquiler.resources.FileCSV;
import alquiler.resources.FileJSON;
import alquiler.resources.FileText;
import alquiler.resources.Helpers;

public class App {

        /**
         * Agregue una opción con un informe en el que cada línea incluye sólo la
         * información que se describe a continuación: identificación del arrendatario,
         * código y tipo de inmueble, fechas de firma, inicio y finalización del
         * contrato y tipo de aval e ingresos previstos según el tiempo en meses de
         * arrendamiento
         */

        static Empleado gerente;
        static EmpresaPropietaria empresa;
        private static CtrlContrato ctrlContratos;
        private static CtrlPersona ctrlPersonas;
        private static CtrlInmueble ctrlInmueble;
        private static CtrlAval ctrlAvales;
        private static DecimalFormat decimalFormat;
        private static int cantidadArrendadoLocal = 0;
        private static int cantidadArrendandoOficina = 0;
        private static int cantidadArrendadoApartamento = 0;
        private static int cantidadLocales = 0;
        private static int cantidadOficinas = 0;
        private static int cantidadApartamentos = 0;
        private static double promedioLocal = 0;
        private static double promedioOficina = 0;
        private static double promedioApartamento = 0;

        static Scanner in = new Scanner(System.in);

        public static void main(String[] args) throws Exception {
                in.useDelimiter("[\n]+|[\r\n]+");
                decimalFormat = new DecimalFormat("###,###.##");
                Helpers.createFolderIfNotExist("archivos");
                crearControladores();
                crearGerente();
                crearEmpresa();

                Calendar hoy = Calendar.getInstance(); // guarda en hoy la fecha del sistema
                System.out.printf("%nFecha actual: %s%n", Helpers.strFecha(hoy));

                do {
                        int opcion = leerOpcion();

                        switch (opcion) {
                                case 1:
                                        informe();
                                        break;
                                case 2:
                                        informeDisponibilidad();
                                        break;
                                case 3:
                                        agregarEmpleados();
                                        break;
                                case 4:
                                        agreagarArrendatario();
                                        break;
                                case 5:
                                        agregarInmueble();
                                        break;
                                case 6:
                                        agregarLocales();
                                        break;
                                case 7:
                                        agrearApartamentos();
                                        break;
                                case 8:
                                        crearContratos();
                                        break;
                                case 9:
                                        agregarAvales();
                                        break;
                                case 10:
                                        listarAptos();
                                        break;
                                case 11:
                                        listarLocales();
                                        break;
                                case 12:
                                        listarInmuebles();
                                        break;
                                case 13:
                                        listarArrendatarios();
                                        break;
                                case 14:
                                        listarEmpleados();
                                        break;
                                case 15:
                                        listarAvales();
                                        break;
                                case 16:
                                        listarContratos();
                                        break;
                                case 17:
                                        searchDatos();
                                        break;
                                case 18:
                                        deleteDatos();
                                        break;
                                case 19:
                                        updateDatos();
                                        break;
                                case 99:
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Opción inválida");
                        }
                } while (true);

        }

        private static void updateDatos() throws Exception {
                int elegir = 0;
                do {
                        String opciones = "\nDatos para modificar:\n"
                                        + " 1 - Apartamentos                                            \n"
                                        + " 2 - Locales                                                  \n"
                                        + " 3 - Inmuebles                                                 \n"
                                        + " 4 - Arrendatarios                                               \n"
                                        + " 5 - Empleados                                                    \n"
                                        + " 6 - Contratos                                                    \n"
                                        + " 7 - Avales                                                       \n"
                                        + "\nElija una opción (0 para salir) > ";
                        System.out.println(opciones);
                        elegir = in.nextInt();

                        if (elegir == 1) {
                                updateApartamento();

                        } else if (elegir == 2) {
                                updateLocal();
                        } else if (elegir == 3) {
                                updateInmueble();
                        } else if (elegir == 4) {
                                updateArrendatario();
                        } else if (elegir == 5) {
                                updateEmpleado();
                        } else if (elegir == 6) {
                                updateContrato();
                        } else if (elegir == 7) {
                                updateAval();
                        } else {
                                System.out.println("Opción inválida");
                        }
                } while (elegir != 0);
        }

        private static void deleteDatos() throws Exception {
                int elegir = 0;
                do {
                        String opciones = "\nDatos para eliminar:\n"
                                        + " 1 - Apartamentos                                            \n"
                                        + " 2 - Locales                                                  \n"
                                        + " 3 - Inmuebles                                                 \n"
                                        + " 4 - Arrendatarios                                               \n"
                                        + " 5 - Empleados                                                    \n"
                                        + " 6 - Contratos                                                    \n"
                                        + " 7 - Avales                                                       \n"
                                        + "\nElija una opción (0 para salir) > ";
                        System.out.println(opciones);
                        elegir = in.nextInt();

                        if (elegir == 1) {
                                deleteApartamento();
                        } else if (elegir == 2) {
                                deleteLocal();
                        } else if (elegir == 3) {
                                deleteInmueble();
                        } else if (elegir == 4) {
                                deleteArrendatario();
                        } else if (elegir == 5) {
                                deleteEmpleado();
                        } else if (elegir == 6) {
                                deleteContrato();
                        } else if (elegir == 7) {
                                deleteAval();
                        } else {
                                System.out.println("Opción inválida");
                        }
                } while (elegir != 0);
        }

        private static void searchDatos() {
                int elegir = 0;
                do {
                        String opciones = "\nDatos para buscar:\n"
                                        + " 1 - Apartamento                                            \n"
                                        + " 2 - Locales                                                  \n"
                                        + " 3 - Inmuebles                                                 \n"
                                        + " 4 - Arrendatarios                                               \n"
                                        + " 5 - Empleados                                                    \n"
                                        + " 6 - Contratos                                                    \n"
                                        + " 7 - Avales                                                       \n"
                                        + "\nElija una opción (0 para salir) > ";
                        System.out.println(opciones);
                        elegir = in.nextInt();

                        if (elegir == 1) {
                                System.out.print("Código del apartamento: ");
                                String codigo = in.next().toUpperCase();
                                searchApartamento(codigo);
                        } else if (elegir == 2) {
                                System.out.print("Código del local: ");
                                String codigo = in.next().toUpperCase();
                                searchLocal(codigo);
                        } else if (elegir == 3) {
                                System.out.println("Código del inmueble: ");
                                String codigo = in.next().toUpperCase();
                                searchInmueble(codigo);
                        } else if (elegir == 4) {
                                System.out.println("Identificacion del arrendatario: ");
                                String identificacion = in.next().toUpperCase();
                                searchArrendatario(identificacion);
                        } else if (elegir == 5) {
                                System.out.print("Identificación del empleado: ");
                                String identificacion = in.next().toUpperCase();
                                searchEmpleado(identificacion);
                        } else if (elegir == 6) {
                                System.out.print("Código del contrato: ");
                                String codigo = in.next().toUpperCase();
                                searchContrato(codigo);
                        } else if (elegir == 7) {
                                System.out.print("Código del contrato: ");
                                String codigoContrato = in.next().toUpperCase();
                                searchAval(codigoContrato);
                        } else {
                                System.out.println("Opción inválida");
                        }
                } while (elegir != 0);

        }

        private static void menuAval(Aval a) throws Exception {
                System.out.print("¿Desea modificar el contrato? SI/NO: ");
                String modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nCódigo del contrato: ");
                        int indexContrato = searchContrato(in.next().toUpperCase());

                        Contrato c = ctrlContratos.get(indexContrato);
                        a.setContrato(c);
                        System.out.println("\nContrato actualizado");
                }

                System.out.print("¿Desea modificar la fecha del aval? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nFecha de aval: ");
                        a.setCalendar(Helpers.getFecha(in.next()));
                        System.out.println("\nFecha aval actualizada");
                }

                System.out.print("¿Desea modificar el tipo de aval? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nTipo aval (Bancario, tercero, nomina, contrato): ");
                        a.setTipoAval(TipoAval.valueOf(in.next().toUpperCase()));
                        System.out.println("\nTipo aval actualizado");
                }

                System.out.print("¿Desea modificar los detalles? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nDetalles: ");
                        a.setDetalle(in.next());
                        System.out.println("\nDetalles actualizados");
                }
        }

        public static void updateAval() throws Exception {
                String codigoContrato;

                do {
                        System.out.print("Código del contrato (\"0\" para terminar): ");
                        codigoContrato = in.next().toUpperCase();
                        if (!codigoContrato.equals("0")) {
                                int indexAval = searchAval(codigoContrato);

                                if (indexAval == -1) {
                                        System.out.println("Aval no encontrado");
                                        continue;
                                } else {
                                        Aval aval = ctrlAvales.get(indexAval);
                                        menuAval(aval);
                                        ctrlAvales.set(indexAval, aval.toJSONObject());
                                }
                        }
                } while (!codigoContrato.equals("0"));
        }

        private static void menuContrato(Contrato c) throws Exception {
                System.out.print("¿Desea modificar el inmueble? SI/NO: ");
                String modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nInmueble actual ->> %s%n", c.getInmueble().getCodigo());
                        System.out.print("\nIndice inmueble: ");
                        int indexInmueble = in.nextInt();

                        c.setInmueble(ctrlInmueble.get(indexInmueble));
                }

                System.out.print("¿Desea modificar el arrendatario? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nIdentificación arrendatario: ");
                        int indexArrendatario = searchArrendatario(in.next().toUpperCase());

                        Arrendatario a = (Arrendatario) ctrlPersonas.get(indexArrendatario);
                        c.setArrendatario(a);
                        System.out.println("\nArrendatario actualizado");
                }

                System.out.print("¿Desea modificar el código? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nCódigo: ");
                        c.setCodigo(in.next().toUpperCase());
                        System.out.println("\nCódigo actualizado");
                }

                System.out.print("¿Desea modificar la fecha de firma? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nFecha de firma: ");
                        c.setFechaFirma(Helpers.getFecha(in.next()));
                        System.out.println("\nFecha de firma actualizada");
                }

                System.out.print("¿Desea modificar la fecha de inicio? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nFecha de inicio: ");
                        c.setFechaInicio(Helpers.getFecha(in.next()));
                        System.out.println("\nFecha de inicio actualizada");
                }

                System.out.print("¿Desea modificar la fecha de fin? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nFecha de fin: ");
                        c.setFechaFin(Helpers.getFecha(in.next()));
                        System.out.println("\nFecha de fin actualizada");
                }

                System.out.print("¿Desea modificar los términos? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nTérminos: ");
                        c.setTerminos(in.next());
                        System.out.println("\nTérminos actualizados");
                }
        }

        public static void updateContrato() throws Exception {
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();

                        if (!codigo.equals("0")) {
                                int indexContrato = searchContrato(codigo);

                                if (indexContrato == -1) {
                                        System.out.println("Contrato no encontrado");
                                        continue;
                                } else {
                                        Contrato contrato = ctrlContratos.get(indexContrato);
                                        menuContrato(contrato);
                                        ctrlContratos.set(indexContrato, contrato.toJSONObject());
                                        System.out.println("\nActualización terminada");
                                }
                        }

                } while (!codigo.equals("0"));
        }

        private static void menuLocal(Local l) {
                System.out.print("¿Desea modificar el código? SI/NO: ");
                String modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCódigo: ");
                        l.setCodigo(in.next().toUpperCase());
                        System.out.printf("%nCódigo actualizado%n");
                }

                System.out.print("¿Desea modificar el código postal? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCódigo postal: ");
                        l.setCodigoPostal(in.next());
                        System.out.printf("%nCódigo postal actualizado%n");
                }

                System.out.print("¿Desea modificar la dirección? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDirección: ");
                        l.setDireccion(in.next());
                        System.out.printf("%nDirección actualizada%n");
                }

                System.out.print("¿Desea modificar el área? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nÁrea: ");
                        l.setArea(in.nextDouble());
                        System.out.printf("%nÁrea actualizada%n");
                }

                System.out.print("¿Desea modificar el valor arriendo? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nValor arriendo: ");
                        l.setValorArriendo(in.nextDouble());
                        System.out.printf("%nValor arriendo actualizado%n");
                }

                System.out.print("¿Desea modificar si está disponible? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDisponible (true/false): ");
                        l.setDisponible(in.nextBoolean());
                        System.out.printf("%nDisponible actualizado%n");
                }

                System.out.print("¿Desea modificar la descripción? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDescripción: ");
                        l.setDescripcion(in.next());
                        System.out.printf("%nDescripción actualizado%n");
                }

                System.out.print("¿Desea modificar el inmueble? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCódigo inmueble: ");
                        int indexInmueble = searchInmueble(in.next().toUpperCase());

                        Inmueble i = (Inmueble) ctrlInmueble.get(indexInmueble);
                        l.setInmueble(i);
                        System.out.println("\nInmueble actualizado\n");
                }

                System.out.print("¿Desea modificar si hay baño? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nTiene baño (true/false): ");
                        l.setConBanio(in.nextBoolean());
                        System.out.println("\nCon baño actualizado\n");
                }

                System.out.print("¿Desea modificar si tiene bodega? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.print("\nTiene bodega (true/false): ");
                        l.setConBanio(in.nextBoolean());
                        System.out.println("\nBodega actualizada\n");
                }

                System.out.print("¿Desea modificar el tipo local? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nTipo local (local_comercial, oficina): ");
                        l.setTipoLocal(TipoLocal.valueOf(in.next().toUpperCase()));
                        System.out.printf("%nTipo local actualizado%n");
                }
        }

        public static void updateLocal() throws Exception {
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();

                        if (!codigo.equals("0")) {

                                int indexLocal = searchLocal(codigo);

                                if (indexLocal == -1) {
                                        System.out.println("Local no encontrado");
                                        continue;
                                } else {
                                        Local local = (Local) ctrlInmueble.get(indexLocal);
                                        menuLocal(local);
                                        ctrlInmueble.setClase(Local.class);
                                        ctrlInmueble.set(indexLocal, local.toJSONObject());
                                        System.out.println("\nActualización terminada.");
                                }
                        }

                } while (!codigo.equals("0"));
        }

        private static void menuApartamento(Apartamento a) {
                System.out.print("¿Desea modificar el código? SI/NO: ");
                String modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCódigo: ");
                        a.setCodigo(in.next().toUpperCase());
                        System.out.printf("%nCódigo actualizado%n");
                }

                System.out.print("¿Desea modificar el código postal? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCódigo postal: ");
                        a.setCodigoPostal(in.next());
                        System.out.printf("%nCódigo postal actualizado%n");
                }

                System.out.print("¿Desea modificar la dirección? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDirección: ");
                        a.setDireccion(in.next());
                        System.out.printf("%nDirección actualizada%n");
                }

                System.out.print("¿Desea modificar el área? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nÁrea: ");
                        a.setArea(in.nextDouble());
                        System.out.printf("%nÁrea actualizada%n");
                }

                System.out.print("¿Desea modificar el valor arriendo? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nValor arriendo: ");
                        a.setValorArriendo(in.nextDouble());
                        System.out.printf("%nValor arriendo actualizado%n");
                }

                System.out.print("¿Desea modificar si está disponible? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDisponible (true/false): ");
                        a.setDisponible(in.nextBoolean());
                        System.out.printf("%nDisponible actualizado%n");
                }

                System.out.print("¿Desea modificar la descripción? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDescripción: ");
                        a.setDescripcion(in.next());
                        System.out.printf("%nDescripción actualizado%n");
                }

                System.out.print("¿Desea modificar el inmueble? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCódigo inmueble: ");
                        int indexInmueble = searchInmueble(in.next().toUpperCase());

                        Inmueble i = (Inmueble) ctrlInmueble.get(indexInmueble);
                        a.setInmueble(i);
                        System.out.println("\nInmueble actualizado\n");
                }
        }

        public static void updateApartamento() throws Exception {
                ctrlInmueble.setClase(Apartamento.class);
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();

                        if (!codigo.equals("0")) {

                                int indexApartamento = searchApartamento(codigo);

                                if (indexApartamento == -1) {
                                        System.out.println("Apartamento no encontrado");
                                        continue;
                                } else {
                                        Apartamento apto = (Apartamento) ctrlInmueble.get(indexApartamento);
                                        menuApartamento(apto);
                                        ctrlInmueble.set(indexApartamento, apto.toJSONObject());
                                        System.out.println("\nActualización terminada.");
                                }
                        }

                } while (!codigo.equals("0"));
        }

        private static void menuInmueble(Inmueble i) {
                System.out.print("¿Desea modificar el código? SI/NO: ");
                String modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCódigo: ");
                        i.setCodigo(in.next().toUpperCase());
                        System.out.printf("%nCódigo actualizado%n");
                }

                System.out.print("¿Desea modificar el código postal? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCódigo postal: ");
                        i.setCodigoPostal(in.next());
                        System.out.printf("%nCódigo postal actualizado%n");
                }

                System.out.print("¿Desea modificar la dirección? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDirección: ");
                        i.setDireccion(in.next());
                        System.out.printf("%nDirección actualizada%n");
                }

                System.out.print("¿Desea modificar el área? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nÁrea: ");
                        i.setArea(in.nextDouble());
                        System.out.printf("%nÁrea actualizada%n");
                }

                System.out.print("¿Desea modificar el valor arriendo? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nValor arriendo: ");
                        i.setValorArriendo(in.nextDouble());
                        System.out.printf("%nValor arriendo actualizado%n");
                }

                System.out.print("¿Desea modificar si está disponible? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDisponible (true/false): ");
                        i.setDisponible(in.nextBoolean());
                        System.out.printf("%nDisponible actualizado%n");
                }

                System.out.print("¿Desea modificar la descripción? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nDescripción: ");
                        i.setDescripcion(in.next());
                        System.out.printf("%nDescripción actualizado%n");
                }

                System.out.print("¿Desea modificar el administrador? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {

                        List<Persona> lstAdmins = ctrlPersonas.list(Empleado.class);
                        System.out.printf("%nIndice del administrador (0..%d): ", lstAdmins.size() - 1);
                        int indiceAdmin = in.nextInt();
                        Empleado admin = (Empleado) lstAdmins.get(indiceAdmin);

                        i.setAdministrador(admin);
                        System.out.println("\nAdministrador actualizado.\n");
                }

                System.out.print("¿Desea modificar el tipo inmueble? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf(
                                        "%nTipo inmueble (CASA, EDIFICIO, FINCA, CONSTRUCCION, APARTAMENTO, LOCAL): ");
                        i.setTipoInmueble(TipoInmueble.valueOf(in.next().toUpperCase()));
                        System.out.printf("%nTipo inmueble actualizado%n");
                }
        }

        public static void updateInmueble() throws Exception {
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();

                        if (!codigo.equals("0")) {

                                int indexInmueble = searchInmueble(codigo);

                                Inmueble inmueble = (Inmueble) ctrlInmueble.get(indexInmueble);

                                if (indexInmueble == -1) {
                                        System.out.println("Inmueble no encontrado");
                                } else {
                                        menuInmueble(inmueble);
                                        ctrlInmueble.setClase(Inmueble.class);
                                        ctrlInmueble.set(indexInmueble, inmueble.toJSONObject());
                                        System.out.println("\nActualización terminada.");
                                }
                        }
                } while (!codigo.equals("0"));
        }

        private static void menuEmpleado(Empleado e) {
                System.out.print("¿Desea modificar la identificación? SI/NO: ");
                String modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nIdentificación: ");
                        e.setIdentificacion(in.next().toUpperCase());
                        System.out.printf("%nIdentificación actualizada%n");
                }

                System.out.print("¿Desea modificar el nombre? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nNombre: ");
                        e.setNombre(in.next());
                        System.out.printf("%nNombre actualizado%n");
                }

                System.out.print("¿Desea modificar el telefono? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nTelefono: ");
                        e.setTelefono(in.next());
                        System.out.printf("%nTelefono actualizado%n");
                }

                System.out.print("¿Desea modificar el correo? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCorreo: ");
                        e.setCorreo(in.next());
                        System.out.printf("%nCorreo actualizado%n");
                }

                System.out.print("¿Desea modificar el perfil? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nPerfil: ");
                        e.setPerfil(Perfil.valueOf(in.next().toUpperCase()));
                        System.out.printf("%nPerfil actualizado%n");
                }

        }

        public static void updateEmpleado() throws Exception {
                String identificacion;

                do {
                        System.out.print("Identificación (\"0\" para terminar): ");
                        identificacion = in.next().toUpperCase();

                        if (!identificacion.equals("0")) {

                                int indexEmpleado = searchEmpleado(identificacion);

                                if (indexEmpleado == -1) {
                                        System.out.println("Empleado no encontrado");
                                        continue;
                                } else {
                                        Empleado empleado = (Empleado) ctrlPersonas.get(indexEmpleado);
                                        menuEmpleado(empleado);
                                        ctrlPersonas.setSubclase(Empleado.class);
                                        ctrlPersonas.set(indexEmpleado, empleado.toJSONObject());
                                        System.out.println("\nActualización terminada.");
                                }
                        }
                } while (!identificacion.equals("0"));
        }

        private static void menuArrendatario(Arrendatario a) throws Exception {
                System.out.print("¿Desea modificar la identificación? SI/NO: ");
                String modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nIdentificación: ");
                        a.setIdentificacion(in.next().toUpperCase());
                        System.out.printf("%nIdentificación actualizada%n");
                }

                System.out.print("¿Desea modificar el nombre? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nNombre: ");
                        a.setNombre(in.next());
                        System.out.printf("%nNombre actualizado%n");
                }

                System.out.print("¿Desea modificar el telefono? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nTelefono: ");
                        a.setTelefono(in.next());
                        System.out.printf("%nTelefono actualizado%n");
                }

                System.out.print("¿Desea modificar el correo? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nCorreo: ");
                        a.setCorreo(in.next());
                        System.out.printf("%nCorreo actualizado%n");
                }

                System.out.print("¿Desea modificar la fecha de nacimiento? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nFecha de nacimiento: ");
                        a.setFechaNacimiento(Helpers.getFecha(in.next()));
                        System.out.printf("%nFecha de nacimiento actualizada%n");
                }

                System.out.print("¿Desea modificar el sexo? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("%nSexo: ");
                        a.setSexo(in.next().toUpperCase().trim().charAt(0));
                        System.out.printf("%nSexo actualizado%n");
                }

                System.out.print("¿Desea modificar la foto? SI/NO: ");
                modificar = in.next().toUpperCase();

                if (modificar.equals("SI")) {
                        System.out.printf("Foto: ");
                        a.setFoto(in.next());
                        System.out.printf("%nFoto actualizada%n");
                }

        }

        public static void updateArrendatario() throws Exception {
                String identificacion;
                do {
                        System.out.print("Identificación (\"0\" para terminar): ");
                        identificacion = in.next().toUpperCase();

                        if (!identificacion.equals("0")) {

                                int indexArrendatario = searchArrendatario(identificacion);

                                if (indexArrendatario == -1) {
                                        System.out.println("Arrendatario no encontrado.");
                                        continue;
                                } else {
                                        Arrendatario arrendatario = (Arrendatario) ctrlPersonas.get(indexArrendatario);
                                        menuArrendatario(arrendatario);
                                        ctrlPersonas.setSubclase(Arrendatario.class);
                                        ctrlPersonas.set(indexArrendatario, (arrendatario).toJSONObject());
                                        System.out.println("\nActualización terminada.");
                                }
                        }
                } while (!identificacion.equals("0"));
        }

        private static void deleteAval() throws Exception {
                String codigoContrato;

                do {
                        System.out.print("Código del contrato (\"0\" para terminar): ");
                        codigoContrato = in.next().toUpperCase();

                        int indiceAval = searchAval(codigoContrato);

                        if (indiceAval == -1) {
                                System.out.println("Aval no encontrado");
                        } else {
                                ctrlAvales.remove(indiceAval);
                        }

                } while (!codigoContrato.equals("0"));
        }

        private static void deleteContrato() throws Exception {
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();

                        int indiceContrato = searchContrato(codigo);

                        if (indiceContrato == -1) {
                                System.out.println("Contrato no encontrado");
                        } else {
                                ctrlContratos.remove(indiceContrato);
                        }

                } while (!codigo.equals("0"));
        }

        private static void deleteLocal() throws Exception {
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();

                        int indiceLocal = searchLocal(codigo);

                        if (indiceLocal == -1) {
                                System.out.println("Local no encontrado");
                        } else {
                                ctrlInmueble.remove(indiceLocal);
                        }

                } while (!codigo.equals("0"));
        }

        private static void deleteApartamento() throws Exception {
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();

                        int indiceApto = searchApartamento(codigo);

                        if (indiceApto == -1) {
                                System.out.println("Apartamento no encontrado");
                                continue;
                        } else {
                                ctrlInmueble.remove(indiceApto);
                        }

                } while (!codigo.equals("0"));
        }

        private static void deleteInmueble() throws Exception {
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();

                        int indiceInmueble = searchInmueble(codigo);

                        if (indiceInmueble == -1) {
                                System.out.println("Inmueble no encontrado");
                                continue;
                        } else {
                                ctrlInmueble.remove(indiceInmueble);
                        }

                } while (!codigo.equals("0"));
        }

        private static void deleteEmpleado() throws Exception {
                String identificacion;

                do {
                        System.out.print("Identificación (\"0\" para terminar): ");
                        identificacion = in.next().toUpperCase();

                        int indiceEmpleado = searchEmpleado(identificacion);

                        if (indiceEmpleado == -1) {
                                System.out.println("Empleado no encontrado");
                                continue;
                        } else {
                                ctrlPersonas.remove(indiceEmpleado);
                        }

                } while (!identificacion.equals("0"));
        }

        private static void deleteArrendatario() throws Exception {
                String identificacion;

                do {
                        System.out.print("Identificación (\"0\" para terminar): ");
                        identificacion = in.next().toUpperCase();

                        int indiceArrendatario = searchArrendatario(identificacion);

                        if (indiceArrendatario == -1) {
                                System.out.println("Arrendatario no encontrado");
                                continue;
                        } else {
                                ctrlPersonas.remove(indiceArrendatario);
                        }

                } while (!identificacion.equals("0"));
        }

        private static int searchAval(String codigoContrato) {
                for (Aval a : ctrlAvales.list()) {
                        if (a.getContrato().getCodigo().equals(codigoContrato)) {
                                int indiceAval = ctrlAvales.indexOf(a.toJSONObject());
                                System.out.println(a);
                                return indiceAval;
                        }
                }
                return -1;
        }

        private static int searchContrato(String codigo) {
                for (Contrato c : ctrlContratos.list()) {
                        if (c.getCodigo().equals(codigo)) {
                                int indiceContrato = ctrlContratos.indexOf(c.toJSONObject());
                                System.out.println(c);
                                return indiceContrato;
                        }
                }
                return -1;
        }

        private static int searchLocal(String codigo) {
                for (Inmueble l : ctrlInmueble.list(Local.class)) {
                        if (l.getCodigo().equals(codigo)) {
                                int indiceLocal = ctrlInmueble.indexOf(l.toJSONObject());
                                System.out.println(l);
                                return indiceLocal;
                        }
                }
                return -1;
        }

        private static int searchApartamento(String codigo) {
                for (Inmueble apto : ctrlInmueble.list(Apartamento.class)) {
                        if (apto.getCodigo().equals(codigo)) {
                                int indiceApto = ctrlInmueble.indexOf(apto.toJSONObject());
                                System.out.println(apto);
                                return indiceApto;
                        }
                }
                return -1;
        }

        private static int searchInmueble(String codigo) {
                for (Inmueble inm : ctrlInmueble.list(Inmueble.class)) {
                        if (inm.getCodigo().equals(codigo)) {
                                int indiceInmueble = ctrlInmueble.indexOf(inm.toJSONObject());
                                System.out.println(inm);
                                return indiceInmueble;
                        }
                }
                return -1;

        }

        private static int searchEmpleado(String identificacion) {
                for (Persona e : ctrlPersonas.list(Empleado.class)) {
                        if (e.getIdentificacion().equals(identificacion)) {
                                int indiceEmpleado = ctrlPersonas.indexOf(e.toJSONObject());
                                System.out.println(e);
                                return indiceEmpleado;
                        }
                }
                return -1;
        }

        private static int searchArrendatario(String identificacion) {
                for (Persona a : ctrlPersonas.list(Arrendatario.class)) {
                        if (a.getIdentificacion().equals(identificacion)) {
                                int indiceArrendatario = ctrlPersonas.indexOf(a.toJSONObject());
                                System.out.println(ctrlPersonas.get(indiceArrendatario));
                                return indiceArrendatario;
                        }
                }
                return -1;
        }

        private static void agregarAvales() throws Exception {
                String detalle;

                do {
                        System.out.print("Detalles (\"0\" para terminar): ");
                        detalle = in.next();

                        if (!detalle.equals("0")) {
                                System.out.printf("%nIndice del contrato (0..%d): ", ctrlContratos.size() - 1);
                                int indiceContrato = in.nextInt();
                                Contrato contrato = ctrlContratos.get(indiceContrato);

                                System.out.print("Tipo aval (Bancario, tercero, nomina, contrato): ");
                                TipoAval tipoAval = TipoAval.valueOf(in.next().toUpperCase());

                                System.out.print("Fecha del aval (AAAA-MM-dd): ");
                                Calendar fecha = Helpers.getFecha(in.next());

                                ctrlAvales.add(new Aval(fecha, detalle, tipoAval, contrato).toJSONObject());
                        }

                } while (!detalle.equals("0"));
        }

        private static void crearContratos() throws ParseException, Exception {
                JSONObject data = new JSONObject();

                System.out.print("Código del contrato: ");
                data.put("codigo", in.next().toUpperCase());

                System.out.print("Fecha de firma (AAAA-MM-DD): ");
                Calendar fechaFirma = Helpers.getFecha(in.next());
                data.put("fechaFirma", Helpers.strFecha(fechaFirma));

                System.out.print("Fecha de inicio (AAAA-MM-DD): ");
                Calendar fechaInicio = Helpers.getFecha(in.next());
                data.put("fechaInicio", Helpers.strFecha(fechaInicio));

                System.out.print("Fecha de fin (AAAA-MM-DD): ");
                Calendar fechaFin = Helpers.getFecha(in.next());
                data.put("fechaFin", Helpers.strFecha(fechaFin));

                System.out.println("Términos y condiciones: ");
                data.put("terminos", in.next());

                listarInmueblesIndice();
                System.out.printf("%nInmueble que desea alquilar (0..%d): ", ctrlInmueble.size() - 1);
                int indiceInmueble = in.nextInt();
                Inmueble inmueble = ctrlInmueble.get(indiceInmueble);
                data.put("inmueble", inmueble.toJSONObject());

                if (inmueble.getDisponible() == true) {
                        listarArrendatarios();
                        System.out.print("Identificación arrendatario: ");
                        String identificacion = in.next().toUpperCase();

                        int indiceArrendatario = searchArrendatario(identificacion);
                        data.put("arrendatario", ctrlPersonas.get(indiceArrendatario).toJSONObject());

                        // inmueble.setDisponible(false);
                        // ctrlInmueble.set(indiceInmueble, inmueble.toJSONObject());
                        ctrlContratos.add(data);

                } else {
                        System.out.println("Este inmueble no se encuentra disponible");
                }

        }

        private static void agrearApartamentos() throws Exception {
                ctrlInmueble.setClase(Apartamento.class);

                JSONObject data = new JSONObject();

                do {

                        System.out.print("Código (\"0\" para terminar): ");
                        data.put("codigo", in.next().toUpperCase());

                        if (!data.getString("codigo").equals("0")) {

                                listarInmuebles();
                                System.out.print("Código inmueble: ");
                                String codInmueble = in.next().toUpperCase();
                                int indexInmueble = searchInmueble(codInmueble);

                                Inmueble inmueble = ctrlInmueble.get(indexInmueble);
                                data.put("inmueble", inmueble.toJSONObject());

                                System.out.print("Código postal (5 dígitos): ");
                                data.put("codigoPostal", in.next());

                                System.out.print("Dirección: ");
                                data.put("direccion", in.next());

                                System.out.print("Area: ");
                                data.put("area", in.nextDouble());

                                System.out.print("Valor arriendo: ");
                                data.put("valorArriendo", in.nextDouble());

                                System.out.print("Disponible: ");
                                data.put("disponible", in.nextBoolean());

                                System.out.print("Descrpcion: ");
                                data.put("descripcion", in.next());

                                ctrlInmueble.add(data);

                        }
                } while (!data.getString("codigo").equals("0"));

        }

        private static void agregarLocales() throws Exception {
                ctrlInmueble.setClase(Local.class);

                JSONObject data = new JSONObject();

                do {
                        System.out.println("Agregando Locales");
                        System.out.print("Código (\"0\" para terminar): ");
                        data.put("codigo", in.next().toUpperCase());

                        if (!data.getString("codigo").equals("0")) {

                                listarInmuebles();
                                System.out.print("Código inmueble: ");
                                String codInmueble = in.next().toUpperCase();
                                int indexInmueble = searchInmueble(codInmueble);

                                Inmueble inmueble = ctrlInmueble.get(indexInmueble);
                                data.put("inmueble", inmueble.toJSONObject());

                                System.out.print("Código postal (5 dígitos): ");
                                data.put("codigoPostal", in.next());

                                System.out.print("Dirección: ");
                                data.put("direccion", in.next());

                                System.out.print("Area: ");
                                data.put("area", in.nextDouble());

                                System.out.print("Valor arriendo: ");
                                data.put("valorArriendo", in.nextDouble());

                                System.out.print("Disponible: ");
                                data.put("disponible", in.nextBoolean());

                                System.out.print("Descrpcion: ");
                                data.put("descripcion", in.next());

                                System.out.print("Tipo de local(Oficina/Local_comercial): ");
                                TipoLocal tipoLocal = TipoLocal.valueOf(in.next().toUpperCase().trim());
                                data.put("tipoLocal", tipoLocal);

                                System.out.print("Tiene baño(true/false): ");
                                data.put("conBanio", in.nextBoolean());

                                System.out.print("Tiene bodega(true/false): ");
                                data.put("conBodega", in.nextBoolean());

                                ctrlInmueble.add(data);

                        }
                } while (!data.getString("codigo").equals("0"));

        }

        private static void agregarInmueble() throws Exception {
                ctrlInmueble.setClase(Inmueble.class);
                String codigo;

                do {
                        System.out.print("Código (\"0\" para terminar): ");
                        codigo = in.next().toUpperCase();
                        if (!codigo.equals("0")) {

                                System.out.print("Código postal (5 dígitos): ");
                                String codigoPostal = in.next();

                                System.out.print("Dirección: ");
                                String direccion = in.next();

                                System.out.print("Area: ");
                                double area = in.nextDouble();

                                System.out.print("Valor arriendo: ");
                                double valorArriendo = in.nextDouble();

                                System.out.print("Disponible: ");
                                boolean disponible = in.nextBoolean();

                                System.out.print("Descrpcion: ");
                                String descripcion = in.next();

                                listarEmpleados();
                                System.out.print("Identificación del administrador: ");
                                String idAdmin = in.next().toUpperCase();
                                int indexAdmin = searchEmpleado(idAdmin);

                                System.out.print("Tipo de inmueble (CASA, EDIFICIO, CONSTRUCCIÓN, FINCA): ");
                                TipoInmueble tipo = TipoInmueble.valueOf(in.next().toUpperCase());

                                ctrlInmueble.add(new Inmueble(codigo, codigoPostal, direccion, area, valorArriendo,
                                                disponible, descripcion, (Empleado) ctrlPersonas.get(indexAdmin), tipo,
                                                empresa).toJSONObject());

                        }

                } while (!codigo.equals("0"));
        }

        private static void agreagarArrendatario() throws Exception {
                ctrlPersonas.setSubclase(Arrendatario.class);
                JSONObject data = new JSONObject();

                do {
                        System.out.print("Identificación (\"0\" para terminar): ");
                        data.put("identificacion", in.next().toUpperCase());

                        if (!data.getString("identificacion").equals("0")) {

                                System.out.print("Nombre: ");
                                data.put("nombre", in.next());

                                System.out.print("Teléfono: ");
                                data.put("telefono", in.next());

                                System.out.print("Correo: ");
                                data.put("correo", in.next());

                                System.out.print("Fecha de nacimiento (AAAA-MM-dd): ");
                                data.put("fechaNacimiento", in.next());

                                System.out.print("Sexo: ");
                                char sexo = in.next().trim().toLowerCase().charAt(0);
                                data.put("sexo", "" + sexo);

                                System.out.print("Directorio foto: ");
                                data.put("foto", in.next());

                                ctrlPersonas.add(data);

                        }
                } while (!data.getString("identificacion").equals("0"));

        }

        private static void agregarEmpleados() throws Exception {
                ctrlPersonas.setSubclase(Empleado.class);

                JSONObject data = new JSONObject();
                do {

                        System.out.println("Agregar Empleados");
                        System.out.println("Identificación (\"0\" para terminar):");
                        data.put("identificacion", in.next().toUpperCase());

                        if (!data.getString("identificacion").equals("0")) {
                                System.out.print("Nombre: ");
                                data.put("nombre", in.next());

                                System.out.print("Teléfono: ");
                                data.put("telefono", in.next());

                                System.out.print("Correo: ");
                                data.put("correo", in.next());

                                System.out.print("Perfil del empleado (Administrador o Gerente): ");
                                Perfil perfil = Perfil.valueOf(in.next().toUpperCase());
                                data.put("perfil", perfil);

                                ctrlPersonas.add(data);
                        }
                } while (!data.getString("identificacion").equals("0"));

        }

        private static void crearGerente() throws Exception {

                gerente = new Empleado("E00", "Mario Casas", "3014567812", "macasa@gmail.com", Perfil.GERENTE);

        }

        private static void crearEmpresa() {

                empresa = new EmpresaPropietaria("0001111", "INMUEBLES S.A", "Bogotá D.C", "018009945",
                                "inmusa@gmail.com", (Empleado) gerente);

        }

        private static void crearControladores() throws Exception, ParseException {

                String modo = "csv";

                if (modo.equals("json")) {
                        ctrlPersonas = new CtrlPersona(new FileJSON<Persona>());
                        ctrlInmueble = new CtrlInmueble(new FileJSON<Inmueble>(), ctrlPersonas);
                        ctrlContratos = new CtrlContrato(new FileJSON<Contrato>(), ctrlInmueble, ctrlPersonas);
                        ctrlAvales = new CtrlAval(new FileJSON<Aval>(), ctrlContratos);
                } else if (modo.equals("txt")) {
                        ctrlPersonas = new CtrlPersona(new FileText<Persona>());
                        ctrlInmueble = new CtrlInmueble(new FileText<Inmueble>(), ctrlPersonas);
                        ctrlContratos = new CtrlContrato(new FileText<Contrato>(), ctrlInmueble, ctrlPersonas);
                        ctrlAvales = new CtrlAval(new FileText<Aval>(), ctrlContratos);
                } else if (modo.equals("csv")) {
                        ctrlPersonas = new CtrlPersona(new FileCSV<Persona>());
                        ctrlInmueble = new CtrlInmueble(new FileCSV<Inmueble>(), ctrlPersonas);
                        ctrlContratos = new CtrlContrato(new FileCSV<Contrato>(), ctrlInmueble, ctrlPersonas);
                        ctrlAvales = new CtrlAval(new FileCSV<Aval>(), ctrlContratos);
                }

        }

        private static void listarInmueblesIndice() {
                System.out.println("-".repeat(150));
                System.out.println("Listado de inmuebles: ");
                System.out.println("-".repeat(150));
                System.out.println(
                                "            COD.            DIRECC.                   DIS.        AREA     PREC.           TIPO");
                for (int i = 0; i < ctrlInmueble.size(); i++) {
                        String disponible = ctrlInmueble.get(i).getDisponible() ? "Sí" : "No";
                        System.out.printf("Indice: %d - %-13s  %-25s  %-10s  %3.0f  %10.0f  %15s%n", i,
                                        ctrlInmueble.get(i).getCodigo(), ctrlInmueble.get(i).getDireccion(), disponible,
                                        ctrlInmueble.get(i).getArea(), ctrlInmueble.get(i).getValorArriendo(),
                                        ctrlInmueble.get(i).getTipoInmueble());
                }
                System.out.println("-".repeat(150));
        }

        private static void precioPromedio() {
                double totalLocales = 0, totalOficina = 0, totalApartamento = 0;
                System.out.println("-".repeat(50));
                for (Inmueble inmueble : ctrlInmueble.list()) {
                        if (inmueble instanceof Local) {
                                Local local = (Local) inmueble;
                                if (local.getTipoLocal().equals(TipoLocal.LOCAL_COMERCIAL)) {
                                        cantidadLocales++;
                                        totalLocales += local.getValorArriendo();
                                        promedioLocal = totalLocales / cantidadLocales;

                                } else if (local.getTipoLocal().equals(TipoLocal.OFICINA)) {
                                        cantidadOficinas++;
                                        totalOficina += local.getValorArriendo();
                                        promedioOficina = totalOficina / cantidadOficinas;
                                }

                        } else if (inmueble instanceof Apartamento) {
                                Apartamento apartamento = (Apartamento) inmueble;
                                cantidadApartamentos++;
                                totalApartamento += apartamento.getValorArriendo();
                                promedioApartamento = totalApartamento / cantidadApartamentos;

                        }

                }

                System.out.printf("El valor promedio de local comercial es: %s", decimalFormat.format(promedioLocal));
                System.out.printf("%nEl valor promedio de las oficinas es: %s", decimalFormat.format(promedioOficina));
                System.out.printf("%nEl valor promedio de los apartamentos es: %s%n",
                                decimalFormat.format(promedioApartamento));
                System.out.println("-".repeat(50));

        }

        private static void informeDisponibilidad() {
                System.out.println("-".repeat(40));
                System.out.println("LISTADO DE INMUEBLES DISPONIBLES");
                System.out.println("-".repeat(40));
                System.out.println("CÓDIGO");
                for (Inmueble inmueble : ctrlInmueble.list()) {
                        if (inmueble instanceof Local || inmueble instanceof Apartamento) {
                                if (inmueble.getDisponible() == true) {
                                        System.out.printf("%s%n", inmueble.getCodigo());
                                } else {
                                        if (inmueble instanceof Local) {
                                                Local oficina = (Local) inmueble;
                                                if (oficina.getTipoLocal().equals(TipoLocal.OFICINA)) {
                                                        cantidadArrendandoOficina++;

                                                }
                                                if (oficina.getTipoLocal().equals(TipoLocal.LOCAL_COMERCIAL)) {
                                                        cantidadArrendadoLocal++;
                                                }

                                        } else if (inmueble instanceof Apartamento) {
                                                cantidadArrendadoApartamento++;
                                        }
                                }
                        }

                }
                precioPromedio();
                System.out.printf("Cantidad de oficinas arrendadas: %d", cantidadArrendandoOficina);
                System.out.printf("%nCantidad de locales arrendados: %d", cantidadArrendadoLocal);
                System.out.printf("%nCantidad de apartamentos arrendados: %d%n", cantidadArrendadoApartamento);
                System.out.println("-".repeat(50));
        }

        private static void informe() {
                double total = 0;
                System.out.println("-".repeat(130));
                System.out.println(
                                "                                                           INFORME                        ");
                System.out.println("-".repeat(130));
                System.out.println(
                                "ID.ARR   COD.IN          TIPO IN.            $V.ARR        FECH.FIR         FECH.INICIO          FECH.FIN  TIPO AVAL  MESES   TOTAL");
                for (Aval aval : ctrlAvales.list()) {
                        for (Contrato contrato : ctrlContratos.list()) {
                                for (Inmueble inmueble : ctrlInmueble.list()) {
                                        if (inmueble.getCodigo().equals(contrato.getInmueble().getCodigo())) {
                                                System.out.printf(
                                                                "%n %-8s   %-8s   %-15s   %10s   %15s   %15s   %15s %10s   %2d   %-16s%n ",
                                                                contrato.getArrendatario().getIdentificacion(),
                                                                contrato.getInmueble().getCodigo(),
                                                                contrato.getInmueble().getTipoInmueble(),
                                                                decimalFormat.format(contrato.getInmueble()
                                                                                .getValorArriendo()),
                                                                contrato.getFechaFirma(), contrato.getFechaInicio(),
                                                                contrato.getFechaFin(), aval.getTipoAval(),
                                                                contrato.mesesArriendo(),
                                                                decimalFormat.format(contrato.valorArriendo()));
                                                total += aval.getContrato().valorArriendo();
                                        }

                                }
                        }
                }
                System.out.println("-".repeat(130));
                System.out.printf("%nTotal de ingresos hasta ahora: %s%n", decimalFormat.format(total));
                System.out.println("-".repeat(130));
        }

        public static void listarAvales() {
                System.out.println("-".repeat(40));
                System.out.println("Listado de avales: ");
                System.out.println("-".repeat(40));
                System.out.println("   COD.  TIPO          FECHA       DETALLES");
                for (Aval a : ctrlAvales.list()) {
                        System.out.println(a);
                }
                System.out.println("-".repeat(40));
        }

        public static void listarContratos() {
                System.out.println(
                                "   COD. F.FIRMA    F.INICIO     F.FIN        TÉRMINOS              INMUEBLE   ARREND.");
                for (Contrato c : ctrlContratos.list()) {
                        System.out.println(c);

                }
                System.out.println("-".repeat(90));

        }

        public static void listarLocales() {
                System.out.println("-".repeat(140));
                System.out.println("Listado de locales: ");
                System.out.println("-".repeat(140));
                System.out.println(
                                "INM.    COD.   C.POS            DIRECCIÓN             AREA    PRECIO DIS         DESCRIPCIÓN          TIPO               BAN. BOD.");
                for (Inmueble local : ctrlInmueble.list(Local.class)) {
                        System.out.println(local);
                }
                System.out.println("-".repeat(140));
        }

        public static void listarAptos() {
                System.out.println("-".repeat(120));
                System.out.println("Listado de apartamentos: ");
                System.out.println("-".repeat(120));
                System.out.println(
                                "INM.    COD.   C.POS            DIRECCIÓN           AREA  PRECIO     DIS         DESCRIPCIÓN");
                for (Inmueble apto : ctrlInmueble.list(Apartamento.class)) {
                        System.out.println(apto);
                }
                System.out.println("-".repeat(120));
        }

        public static void listarInmuebles() {
                System.out.println("-".repeat(120));
                System.out.println("Listado de inmuebles: ");
                System.out.println("-".repeat(120));
                System.out.println(
                                "COD.   C.POSTAL      DIRECCIÓN       AREA   PRECIO    DIS.                   DESCRIP.       ADMI                      TIPO");
                for (Inmueble inmueble : ctrlInmueble.list(Inmueble.class)) {
                        System.out.println(inmueble);
                }
                System.out.println("-".repeat(120));

        }

        public static void listarArrendatarios() {
                System.out.println("-".repeat(120));
                System.out.println("Listado de arrendatarios: ");
                System.out.println("-".repeat(120));
                System.out.println(
                                "    COD.    NOMBRE                                   TELEFONO       CORREO                FECH.NAC        S   FOTO");
                for (Persona arrendatario : ctrlPersonas.list(Arrendatario.class)) {
                        System.out.println(arrendatario);
                }
                System.out.println("-".repeat(120));
        }

        public static void listarEmpleados() {
                System.out.println("-".repeat(100));
                System.out.println("Listado de empleados: ");
                System.out.println("-".repeat(100));
                System.out.println(
                                "    COD    NOMBRE                                   TELÉFONO      CORREO                  PERFIL");
                for (Persona empleado : ctrlPersonas.list(Empleado.class)) {
                        System.out.println(empleado);
                }
                System.out.println("-".repeat(100));

        }

        private static int leerOpcion() {
                String opciones = "\nMenú de opciones:\n"
                                + " 1 - Informe de arriendos            11 - Listar locales        \n"
                                + " 2 - Informe disponibilidad          12 - Listar inmuebles      \n"
                                + " 3 - Agregar empleados               13 - Listar arrendatarios  \n"
                                + " 4 - Registrar arrendatarios         14 - Listar empleados      \n"
                                + " 5 - Agregar inmuebles               15 - Listar avales         \n"
                                + " 6 - Agregar locales                 16 - Listar contratos      \n"
                                + " 7 - Agregar apartamentos            17 - Buscar datps          \n"
                                + " 8 - Realizar un contrato            18 - Eliminar datos        \n"
                                + " 9 - Agregar avales                  19 - Actualizar datos      \n"
                                + "10 - Listar apartamentos                                        \n"
                                + "                                                                \n"
                                + "\nElija una opción (99 para salir) > ";
                System.out.print(opciones);
                int opcion = in.nextInt();
                System.out.println();
                return opcion;
        }

}