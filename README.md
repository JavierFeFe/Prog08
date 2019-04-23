# Tarea para PROG08.
## Enunciado.
Imagina que te proporcionan, en el siguiente formato, los datos de un cliente, con todos sus teléfonos y todos sus direcciones de correo electrónico:

DNI, "nombre", "apellidos", teléfono 1, teléfono 2, email 1, teléfono 3, email 2,...

En una misma línea se encuentran, separados por comas, todos los datos del cliente: DNI (o NIE), nombre, apellidos, teléfonos y direcciones de correo electrónico. Fijate que los teléfonos y los correos electrónicos pueden aparecer desordenados, y que pueden ser más de uno. La idea es meter dichos datos en un documento XML que contenga los datos de contacto del cliente. No es necesario leer los datos de un archivo, basta con que se capturen del teclado. Además, el documento XML solo contendrá los datos de un cliente, por lo que no debes preocuparte de procesar múltiples líneas (solo una).

NOTA:

Alternativamente, puedes generar la salida del programa en formato JSON (en lugar de formato XML).

En el apartado de recursos de esta tarea, encontrará un enlace a la descripción del formato y otro enlace a un pequeño tutorial de GSON (la librería de Google para el procesado de archivos JSON en Java) que explica brevemente la lectura y escritura de archivos JSON desde Java.

A la hora de procesar los datos tienes que tener en cuenta los siguientes aspectos:

* El DNI o NIE siempre aparecerá en primer lugar.
* El nombre siempre aparecerá en segundo lugar, entre comillas. Las comillas habrá que quitarlas, no pueden aparecer en el documento XML.
* Los apellidos siempre aparecerán en tercer lugar, entre comillas. Nuevamente, las comillas habrá que quitarlas.
* Los teléfonos y los correos electrónicos aparecerán desordenados y mezclados entre sí, pero en el XML resultante deben aparecer separados en dos partes: teléfonos y correos electrónicos por separado.
* Los teléfonos pueden tener paréntesis para simbolizar el prefijo ("(91)2345678") o en el código del país. Estos paréntesis habrá que eliminarlos al almacenarlo en el documento XML.
* Los teléfonos además pueden tener el símbolo "+" delante. Este hay que dejarlo, dado que simboliza que el número es un número internacional.
* Los teléfonos deben aparecer ordenados. Primero aparecerán ordenados de mayor a menor los números locales (sin símbolo "+" delante) y después los internacionales, también ordenados de mayor a menor.
* Los correos electrónicos deben almacenarse en minúsculas, aunque en la entrada vayan en mayúsculas.
* La lista de teléfonos y de correos no debería tener duplicados (tomando mayúsculas y minúsculas como lo mismo). Ten en cuenta que los paréntesis sobre un mismo número de teléfono no deben provocar la existencia de teléfonos diferentes en el XML.
* Es importante eliminar espacios sobrantes antes y después de cada trozo de texto extraído para procesarlo adecuadamente.
  
Las características del documento XML a generar son las siguientes:  
  
* El elemento raíz se llamará "datos_cliente".
* El DNI o el NIE se almacenará en un elemento llamado "id".
* El nombre siempre se almacenará en un elemento llamado "nombre".
* Los apellidos siempre se almacenarán en un elemento llamado "apellidos".
* Los teléfonos se almacenarán todos en un elemento llamado "telefonos" (sin acento). Dentro de dicho elemento, habrá sub-elementos, llamados "telefono" (sin acento), cada uno de los cuales contendrá un único teléfono. Los teléfonos deberán aparecer ordenados tal y como se comentó en el párrafo anterior.
* La etiqueta "telefonos" deberá tener un atributo llamado "total" que contendrá el número total de teléfonos de la lista. <- *NO VEO FORMA DE HACER ESTO EN UN JSON.*
* Los correos electrónicos se almacenarán en un elemento llamado "mails". Dentro de dicho elemento, habrá sub-elementos llamados "mail" destinados a almacenar cada uno de los correos electrónicos del usuario.
* Puedes usar los siguientes datos de prueba en tu aplicación (cada línea debería generar un documento XML):
  
```  
X12345678F,"nombre","apellidos",+(82)12345678, 612345678,test@TEST.com,(91)23456789 ,prueba@prueba.com
12345678Z,"nombre","apellidos", prueba@prueba.com,(952)333333,test@test.com ,952333333,test@TEST.com
```
```Java
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
        Scanner teclado = new Scanner(System.in); //Capturo una línea de texto
        System.out.print("Introduce el texto separado por comas: ");
        String texto = teclado.nextLine();
        //String texto="X12345678F,\"nombre\",\"apellidos\",+(82)12345678, 612345678,test@TEST.com,(91)23456789 ,prueba@prueba.com, prueba@prueba.com, adsfasdf"; //Texto de ejemplo
        List<String> cadena = new ArrayList<String>(Arrays.asList(texto.split(",")));
        if (cadena.size() >= 3) { //Si la cadena contiene al menos los 3 primeros campos

            //La detección de los 3 primeros campos se podría mejorar mediante los regex adecuados, pero preferí omitirlo ya que
            //aumentaría considerablemente el código y en las instrucciones del ejercio no especifica que sea necesaria esta comprobación.

            String nif = cadena.get(0).trim(); //Establezco el valor del campo nif
            String nombre = cadena.get(1).replaceAll("\"", "").trim(); //Establezco el valor del campo nombre, eliminando espacios al principio y final y las comillas
            String apellidos = cadena.get(2).replaceAll("\"", "").trim(); //Establezco el valor del campo apellidos
            List<String> telefonos = new ArrayList(); //Creo un listado para los teléfonos
            List<String> telefonosInternacionales = new ArrayList(); //Creo un listado para los teléfonos internacionales
            List<String> correos = new ArrayList(); //Creo un listado para los correos
            List<String> errores = new ArrayList<>();
            datosCliente.addProperty("id", nif); //Almaceno el nif en el json
            datosCliente.addProperty("nombre", nombre); //Almaceno el nombre en el json
            datosCliente.addProperty("apellidos", apellidos); //Almaceno los apellidos en el json
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
                } else if (campo.trim().matches("^.+@.+\\..+$")) { //Interpreto mediante un regex el campo de correo electrónico
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
```
