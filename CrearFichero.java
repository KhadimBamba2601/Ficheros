package FicherosJava; 

import java.io.FileWriter; 
import java.io.IOException; 

public class CrearFichero {
	public static void main(String[] args) {
		// Define la ruta completa del archivo que se va a crear o modificar.
		// Las dobles barras invertidas (\\) son necesarias en Java para escapar la barra simple (\) en las rutas de Windows.
		String nombreArchivo = "C:\\Users\\usuario\\Desktop\\NuevoDocumentoTexto2.txt";
		
		// El bloque 'try-with-resources' asegura que el 'FileWriter' se cierre automáticamente,
		// incluso si ocurre una excepción. Es la forma recomendada de trabajar con recursos.
		try (FileWriter escritor = new FileWriter(nombreArchivo)) {
			// El método 'write()' de FileWriter escribe el texto en el archivo.
			// Cada llamada agrega el contenido al final del archivo.
			escritor.write("Estoy escribiendo en un fichero de texto.\n");
			escritor.write("En un lugar de la Manche.\n");
			escritor.write("Es el comienzo del Quijote\n");
			
			// Se pueden concatenar diferentes tipos de datos (como números) a una cadena de texto para escribirlos.
			escritor.write("Otros datos: " + 123 + ", " + 4.56 + "\n");
			
			// ATENCIÓN: Cuando se escribe un número entero (int) sin concatenarlo a un String,
			// FileWriter lo interpreta como el código Unicode de un carácter. En este caso,
			// el número 123 corresponde al carácter '{'. Si quieres escribir "123", debes
			// convertirlo a cadena de texto.
			escritor.write(123);
			
			System.out.println("Se ha escrito en el fichero correctamente.");
		} catch (IOException e) {
			// Si ocurre un error al intentar escribir en el archivo (por ejemplo, si la ruta no existe,
			// o no se tienen los permisos necesarios), el programa entra en este bloque 'catch'.
			// Muestra un mensaje de error detallado para ayudar a la depuración.
			System.out.println("Error al escribir en el fichero: " + e.getMessage());
		}
	}
}
