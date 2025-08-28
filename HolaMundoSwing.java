package FicherosJava;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class HolaMundoSwing {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Crear un objeto JFrame, que representa la ventana principal.
                JFrame ventana = new JFrame("Ventana con Swing");
                
                // Configurar qué pasa cuando el usuario cierra la ventana.
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Establecer el tamaño de la ventana.
                ventana.setSize(400, 200); // 400 píxeles de ancho por 200 de alto

                // Centrar la ventana en la pantalla.
                ventana.setLocationRelativeTo(null);
                
                // Crear un objeto JLabel, que es una etiqueta de texto.
                JLabel etiqueta = new JLabel("Hola Mundo");
                
                // Centrar el texto en la etiqueta.
                etiqueta.setHorizontalAlignment(JLabel.CENTER);
                
                // Agregar la etiqueta a la ventana.
                ventana.add(etiqueta);
                
                // Hacer que la ventana sea visible.
                ventana.setVisible(true);
            }
        });
    }
}