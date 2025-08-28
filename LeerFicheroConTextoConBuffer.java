package FicherosJava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.io.File;

public class LeerFicheroConTextoConBuffer {

    public static void main(String[] args) {
        String archivoOriginal = "C:\\Users\\usuario\\Desktop\\nombres.txt";
        String nuevoArchivo = "C:\\Users\\usuario\\Desktop\\nombresSinRepetir.txt";
        String nombreABuscar = "Carmen";
        int contador = 0;

        // Usamos un Set para almacenar los nombres únicos y evitar duplicados
        Set<String> nombresUnicos = new HashSet<>();

        // Leemos el archivo original para procesar los datos
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoOriginal))) {
            String linea;
            System.out.println("--- Contenido del archivo original ---");
            while ((linea = lector.readLine()) != null) {
                // Contar las veces que aparece 'Carmen'
                if (linea.trim().equalsIgnoreCase(nombreABuscar)) {
                    contador++;
                }
                // Añadir el nombre al Set para asegurar que sea único
                nombresUnicos.add(linea);
                System.out.println(linea); // Opcional: para ver las líneas leídas
            }
            System.out.println("\n--- Análisis de datos ---");
            System.out.println("El nombre '" + nombreABuscar + "' aparece " + contador + " veces en el archivo original.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo original: " + e.getMessage());
            return; // Salimos si hay un error de lectura
        }

        // Escribimos los nombres únicos en el nuevo archivo
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nuevoArchivo))) {
            for (String nombre : nombresUnicos) {
                escritor.write(nombre);
                escritor.newLine();
            }
            System.out.println("El fichero con nombres sin repetir se ha creado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir el nuevo archivo: " + e.getMessage());
        }

        // Mostrar el número de líneas y el tamaño del nuevo fichero
        File nuevoFichero = new File(nuevoArchivo);
        if (nuevoFichero.exists()) {
            int numLineas = 0;
            try (BufferedReader lectorNuevo = new BufferedReader(new FileReader(nuevoFichero))) {
                while (lectorNuevo.readLine() != null) {
                    numLineas++;
                }
                long tamanoBytes = nuevoFichero.length();
                System.out.println("\n--- Información del nuevo fichero ---");
                System.out.println("El fichero '" + nuevoArchivo + "' se ha creado con éxito.");
                System.out.println("Número de líneas: " + numLineas);
                System.out.println("Tamaño del fichero: " + tamanoBytes + " bytes.");
            } catch (IOException e) {
                System.err.println("Error al leer el nuevo archivo para obtener su información: " + e.getMessage());
            }
        }
    }
}