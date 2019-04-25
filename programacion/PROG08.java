/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programacion;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author duche
 */
public class PROG08 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JsonObject root = new JsonObject(); //Creo el elemento raiz
        JsonObject datosCliente = new JsonObject(); //Creo el subelemento datosCliente
        root.add("datos_cliente", datosCliente); //Añado el elemento al elemento raiz
        //Scanner teclado = new Scanner(System.in); //Capturo una línea de texto
        //System.out.print("Introduce el texto separado por comas: ");
        //String texto = teclado.nextLine();
        String texto="X12345678F,\"nombre\",\"apellidos\",+(82)12345678, 612345678,test@TEST.com,(91)23456789 ,prueba@prueba.com, prueba@prueba.com, adsfasdf"; //Texto de ejemplo
        List<String> cadena = new ArrayList<String>(Arrays.asList(texto.split(",")));
        if (cadena.size() >= 3) { //Si la cadena contiene al menos los 3 primeros campos
            List<String> errores = new ArrayList<>(); //Creo un listado para los errores
            List<String> telefonos = new ArrayList(); //Creo un listado para los teléfonos
            List<String> telefonosInternacionales = new ArrayList(); //Creo un listado para los teléfonos internacionales
            List<String> correos = new ArrayList(); //Creo un listado para los correos
            String nif = cadena.get(0).trim(); //Establezco el valor del campo nif
            if (nif.matches("^[XYxy]?\\d{8}[A-Za-z]$")){//Regex para identificar un DNI o NIE válido
                datosCliente.addProperty("id", nif); //Almaceno el nif en el json
            }else{
                errores.add("NIF Inválido: " + nif);
            }
            String nombre = cadena.get(1).replaceAll("\"", "").trim(); //Establezco el valor del campo nombre, eliminando espacios al principio y final y las comillas
            if (nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÜü\\s]{2,}$")){ //Regex para identificar una cadena de texto válida para un nombre
                datosCliente.addProperty("nombre", nombre); //Almaceno el nombre en el json
            }else{
                errores.add("Nombre Inválido: " + nombre);
            }
            String apellidos = cadena.get(2).replaceAll("\"", "").trim(); //Establezco el valor del campo apellidos
            if (apellidos.matches("^[A-Za-zÁÉÍÓÚáéíóúÜü\\s]{2,}+$")){//Mismo regex que para el nombre
                datosCliente.addProperty("apellidos", apellidos); //Almaceno el nombre en el json
            }else{
                errores.add("Apellidos Inválidos: " + apellidos);
            }
            for (int i = 3; i < cadena.size() ; i++) { //Recorro todos los campos de la cadena
                String campo = cadena.get(i);
                if (campo.trim().matches("^\\+?(\\(\\d+\\))?\\d{5}\\d*$")) { //Interpreto mediante un regex un nº de teléfono válido
                    if (campo.contains("+")) { //Si contiene el símbolo + se considera internacional
                        String textoTelefonoInter = campo.trim().replaceAll("\\(", "").replaceAll("\\)", "");
                        if (!telefonosInternacionales.contains(textoTelefonoInter)) { //Si el teléfono no está en la lista
                            telefonosInternacionales.add(textoTelefonoInter);
                        } else{
                            errores.add("Teléfono duplicado: " + textoTelefonoInter);
                        }
                    } else { //En otro caso es un teléfono local
                        String textoTelefono = campo.trim().replaceAll("\\(", "").replaceAll("\\)", "");
                        if (!telefonos.contains(textoTelefono)) {//Si el telefono no está en la lista
                            telefonos.add(textoTelefono);
                        }else{
                            errores.add("Teléfono duplicado: " + textoTelefono);
                        }
                    }
                    //Podría hacer simplemente un else si elimino los 3 primeros campos de la cadena, pero mediante este regex me aseguro de su correcta interpretación
                } else if (campo.trim().matches("^\\w+@\\w+\\.\\w{1,4}(\\.\\w{1,4})?$")) { //Interpreto mediante un regex el campo de correo electrónico
                    String correo = campo.trim().toLowerCase();
                    if (!correos.contains(correo)) {
                        correos.add(correo);
                    }else{
                        errores.add("Correo duplicado: " + correo);
                    }
                } else {
                    errores.add("Campo no identificado: " + campo.trim());
                }
            }
            Collections.sort(telefonos); //Ordenos los teléfonos
            Collections.sort(telefonosInternacionales); //Ordeno los teléfonos internacionales
            telefonos.addAll(telefonosInternacionales); //Unifico la lista de teléfonos con los internacionales para simplificar el bucle
            JsonArray coleccionTelefonos = new JsonArray(); //Creo un array de telefonos para añadir al json
            for (String tlfn : telefonos) { //Recorro los teléfonos ordenados y los añado al json
                JsonObject telefono = new JsonObject();
                telefono.addProperty("telefono", tlfn);
                coleccionTelefonos.add(telefono);
            }
            datosCliente.add("telefonos", coleccionTelefonos); //Añado el array al json
            JsonArray coleccionCorreos = new JsonArray(); //Misma operación pero para los correos
            for (String mail : correos) {
                JsonObject mail2 = new JsonObject();
                mail2.addProperty("mail", mail);
                coleccionCorreos.add(mail2);
            }
            datosCliente.add("correos", coleccionCorreos);
            JsonArray coleccionErrores = new JsonArray(); //Misma operación pero para los errores
            for (String error : errores) {
                JsonObject error2 = new JsonObject();
                error2.addProperty("error", error);
                coleccionErrores.add(error2);
            }
            datosCliente.add("errores", coleccionErrores);
            //Gson gson = new GsonBuilder().create(); //Builder sin formatear, funciona perfectamente pero pone todo en una línea
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create(); //Creo el builder
            System.out.println(gson.toJson(root)); //Muestro el resultado por consola
            try (FileWriter writer = new FileWriter("datos_cliente.json")) { //Almaceno en un archivo
                gson.toJson(root, writer);
                System.out.println("Archivo json creado: datos_cliente.json");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("La cadena de texto no tiene el mínimo de campos necesarios para generar un archivo Json");
        }
    }
}

