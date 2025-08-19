package Ficheros;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeerFicheroConTextoConBuffer {

	public static void main(String[] args) {
		String nombreArchivo = "C:\\Users\\usuario\\Desktop\\NuevoDocumentoTexto.txt";
		
		try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				System.out.println(linea);
			}
		} catch (IOException e) {
			System.err.println("Error al leer el archivo: " + e.getMessage());
		}
	}
}
