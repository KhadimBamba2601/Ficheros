package FicherosJava;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BinarioImagenLeerMostrar {

    private static final String RUTA_CARPETA = "C:\\Users\\usuario\\Pictures\\Screenshots";
    private static final int MAX_WIDTH = 1040;
    private static final int MAX_HEIGHT = 860;
    private static List<File> listaImagenes;
    private static int indiceImagenActual = 0;
    private static JLabel etiquetaImagen;
    private static JLabel etiquetaNombreArchivo;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                cargarImagenes();
                if (!listaImagenes.isEmpty()) {
                    mostrarVentana();
                } else {
                    System.err.println("No se encontraron imágenes en la carpeta: " + RUTA_CARPETA);
                }
            } catch (IOException e) {
                System.err.println("Error al cargar las imágenes: " + e.getMessage());
            }
        });
    }

    private static void cargarImagenes() {
        listaImagenes = new ArrayList<>();
        File carpeta = new File(RUTA_CARPETA);
        if (carpeta.isDirectory()) {
            for (File archivo : carpeta.listFiles()) {
                String nombre = archivo.getName().toLowerCase();
                if (nombre.endsWith(".png") || nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".gif") || nombre.endsWith(".bmp")) {
                    listaImagenes.add(archivo);
                }
            }
        }
    }

    private static void mostrarVentana() throws IOException {
        JFrame ventana = new JFrame("Visor de Imágenes");
        ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        etiquetaImagen = new JLabel();
        etiquetaNombreArchivo = new JLabel("", JLabel.CENTER);

        actualizarImagen();

        JButton botonAnterior = new JButton("Anterior");
        botonAnterior.addActionListener(e -> {
            if (indiceImagenActual > 0) {
                indiceImagenActual--;
                try {
                    actualizarImagen();
                } catch (IOException ex) {
                    System.err.println("Error al mostrar la imagen anterior: " + ex.getMessage());
                }
            }
        });

        JButton botonSiguiente = new JButton("Siguiente");
        botonSiguiente.addActionListener(e -> {
            if (indiceImagenActual < listaImagenes.size() - 1) {
                indiceImagenActual++;
                try {
                    actualizarImagen();
                } catch (IOException ex) {
                    System.err.println("Error al mostrar la siguiente imagen: " + ex.getMessage());
                }
            }
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonAnterior);
        panelBotones.add(botonSiguiente);

        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.getContentPane().add(etiquetaImagen, BorderLayout.CENTER);
        ventana.getContentPane().add(panelBotones, BorderLayout.SOUTH);
        ventana.getContentPane().add(etiquetaNombreArchivo, BorderLayout.NORTH);

        ventana.setLocationRelativeTo(null);
        ventana.pack();
        ventana.setVisible(true);
    }

    private static void actualizarImagen() throws IOException {
        if (!listaImagenes.isEmpty()) {
            File imagenActual = listaImagenes.get(indiceImagenActual);
            byte[] bytesImagen = leerBytesDesdeFichero(imagenActual.getAbsolutePath());
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(bytesImagen));

            BufferedImage resizedImage = resizeImage(originalImage, MAX_WIDTH, MAX_HEIGHT);

            ImageIcon iconoImagen = new ImageIcon(resizedImage);
            etiquetaImagen.setIcon(iconoImagen);
            etiquetaNombreArchivo.setText(imagenActual.getName());
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        double ratio = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);

        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    private static byte[] leerBytesDesdeFichero(String nombreArchivo) throws IOException {
        try (FileInputStream lectura = new FileInputStream(nombreArchivo)) {
            return lectura.readAllBytes();
        }
    }
}