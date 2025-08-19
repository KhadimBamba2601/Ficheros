package Ficheros;

import java.io.FileWriter;
import java.io.IOException;

public class CrearFichero {
	public static void main(String[] args) {
		String nombreArchivo = "C:\\\\Users\\\\usuario\\\\Desktop\\\\NuevoDocumentoTexto2.txt";
		
		try (FileWriter escritor = new FileWriter(nombreArchivo)) {
			escritor.write("Estoy escribiendo en un fichero de texto.\n");
			escritor.write("En un lugar de la Manche.\n");
			escritor.write("Es el comienzo del Quijote\n");
			
			escritor.write("Otros datos: " + 123 + ", " + 4.56 + "\n");
			escritor.write(123);
			System.out.println("Se ha escrito en el fichero correctamente.");
		} catch (IOException e) {
			System.out.println("Error al escribir en el fichero: " + e.getMessage());
		}
	}

}
