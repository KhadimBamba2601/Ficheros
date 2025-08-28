package FicherosJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArrayAFichero {
	public static void main (String[] args) {
		String nombres[] = {"Marcos", "Laura", "Carmen", "Lucas", "Ana"};
		int numElementos = nombres.length;
		
		String nombreArchivo = "C:\\Users\\usuario\\Desktop\\NuevoDocumentoTexto2.txt";
		try (BufferedWriter buferEscritura = new BufferedWriter(new FileWriter(nombreArchivo))) {
			//Recorrer el Array y añadir cada elemento
			for (int i =0; i<numElementos; i++) {
				buferEscritura.write(nombres[i]+"\n");
			}
		} catch (IOException e) {
			System.err.println("Error al escribir en el fichero: " + e.getMessage());
		}
		//Leemos el fichero
		try (BufferedReader buferLectura = new BufferedReader(new FileReader(nombreArchivo))) {
			String linea;
			while ((linea = buferLectura.readLine()) != null) {
				System.out.println(linea);
			}
		} catch (IOException e) {
			System.err.println("Error al leer el archivo: " + e.getMessage());
		}

	}
}