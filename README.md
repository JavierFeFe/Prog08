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
* La etiqueta "telefonos" deberá tener un atributo llamado "total" que contendrá el número total de teléfonos de la lista.
* Los correos electrónicos se almacenarán en un elemento llamado "mails". Dentro de dicho elemento, habrá sub-elementos llamados "mail" destinados a almacenar cada uno de los correos electrónicos del usuario.
* Puedes usar los siguientes datos de prueba en tu aplicación (cada línea debería generar un documento XML):
  
```  
X12345678F,"nombre","apellidos",+(82)12345678, 612345678,test@TEST.com,(91)23456789 ,prueba@prueba.com
12345678Z,"nombre","apellidos", prueba@prueba.com,(952)333333,test@test.com ,952333333,test@TEST.com
```

Para ahorrate trabajo, puedes poner los errores detectados como comentarios en el documento XML.

Encontrar un teléfono o un correo duplicado no debería detener la ejecución del programa, sino simplemente no insertarlo en el documento XML final. Si encuentras un duplicado, es conveniente insertar un comentario en el documento XML con la incidencia ocurrida, te servirá para depurar tu código.

La solución ronda las 200 líneas de código, aunque se puede hacer en menos de forma elegante. Si tu código supera esa cantidad de líneas, antes de estar completamente operativo, posiblemente te estés complicando en sobremanera. Puedes usar JOptionPane para ahorrar trabajo de entrada y salida de información.

Usa la clase DOMUtil facilitada en los contenidos para que te sea más fácil manejar el archivo XML.

Clase DOMUtil.(0.01 MB)

Indicaciones de entrega.
Una vez realizada la tarea elaborarás un único documento donde figuren las respuestas correspondientes. El envío se realizará a través de la plataforma de la forma establecida para ello, y el archivo se nombrará siguiendo las siguientes pautas:

apellido1_apellido2_nombre_SIGxx_Tarea

Asegúrate que el nombre no contenga la letra ñ, tildes ni caracteres especiales extraños. Así por ejemplo la alumna Begoña Sánchez Mañas para la octava unidad del MP de PROG, debería nombrar esta tarea como...

sanchez_manas_begona_PROG08_Tarea
