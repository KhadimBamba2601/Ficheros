package FicherosJava;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> crearYMostrarGUI());
    }

    private static void crearYMostrarGUI() {
        // 1. Crear la ventana principal (JFrame)
        JFrame frame = new JFrame("Demostración de Funcionalidades Dinámicas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 350); // Aumentamos el tamaño de la ventana
        frame.setLocationRelativeTo(null); // Centra la ventana
        frame.getContentPane().setBackground(new Color(240, 240, 240)); // Un gris claro para el fondo

        // 2. Usar un JPanel para organizar los componentes
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Aumentamos el espacio entre componentes
        panel.setBackground(new Color(255, 255, 255)); // Fondo blanco para el panel

        // 3. Crear y añadir componentes con estilos mejorados

        // JLabel: Le damos un color y una fuente más grande
        JLabel label = new JLabel("¡Bienvenido! Ingresa tu nombre:");
        label.setForeground(new Color(0, 102, 204)); // Color azul
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Fuente más grande y negrita
        panel.add(label);

        // JTextField: Le damos un estilo más moderno
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("SansSerif", Font.BOLD, 26));
        textField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 3)); // Borde gris
        panel.add(textField);

        // JButton: Le damos un color de fondo y de texto
        JButton botonSaludar = new JButton("Saludar");
        botonSaludar.setBackground(new Color(75, 175, 75)); // Verde
        botonSaludar.setForeground(Color.WHITE); // Texto blanco
        botonSaludar.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(botonSaludar);

        // JCheckBox: Estilizado
        JCheckBox checkBox = new JCheckBox("¿Te gusta este diseño?");
        checkBox.setBackground(panel.getBackground()); // Para que el fondo sea el mismo que el del panel
        checkBox.setFont(new Font("Arial", Font.ITALIC, 24));
        panel.add(checkBox);

        // JComboBox: Estilizado
        String[] opciones = {"Opción A", "Opción B", "Opción C"};
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBackground(new Color(240, 240, 240)); // Fondo gris claro
        panel.add(comboBox);

        // 4. Manejar el evento del botón
        botonSaludar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textField.getText();
                boolean leGustaElDiseno = checkBox.isSelected();
                String opcionSeleccionada = (String) comboBox.getSelectedItem();
                
                String mensaje = "¡Hola, " + nombre + "!\n";
                mensaje += leGustaElDiseno ? "Me alegra que te guste el diseño.\n" : "¡Podemos mejorarlo!\n";
                mensaje += "Has elegido: " + opcionSeleccionada;

                // Muestra un cuadro de diálogo con un icono personalizado y el mensaje
                JOptionPane.showMessageDialog(frame, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 5. Añadir el panel a la ventana y hacerla visible
        frame.add(panel);
        frame.setVisible(true);
    }
}