package FicherosJava;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class GaleriaImagenes {
    
    private static JLabel etiquetaImagen;
    private static JFrame ventanaPrincipal = new JFrame("Gestor de imágenes");
    private static JDialog jDialogVisor;
    
    private static List<String> rutasImagenes = new ArrayList<>();
    private static int indiceActual = 0;
   
    public static void main(String[] args) {
    	
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	ventanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaPrincipal.setLayout(new BorderLayout());  
        
        ventanaPrincipal.setPreferredSize(new Dimension(400, 100));
       
        JMenuBar barraMenu = crearBarraMenu();
       
        ventanaPrincipal.setJMenuBar(barraMenu);	 
        ventanaPrincipal.pack(); 
        ventanaPrincipal.setLocationRelativeTo(null);
        ventanaPrincipal.setVisible(true);
    }
    
    private static JMenuBar crearBarraMenu() {
        JMenuBar barraMenu = new JMenuBar(); 
        JMenu menuArchivo = new JMenu("Archivo");  
        JMenu menuImagenes = new JMenu("Imágenes");
        JMenu menuAcercaDe = new JMenu("Ayuda");
        
        JMenuItem itemAbrirExplorador = new JMenuItem("Abrir el explorador de archivos");
        JMenuItem itemSalir = new JMenuItem("Salir");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de ...");
        JMenuItem itemVisor = new JMenuItem("Ver imágenes");   
             
        itemVisor.addActionListener(e -> mostrarVisor());
        itemSalir.addActionListener(e -> System.exit(0));
        itemAcercaDe.addActionListener(e -> mostrarAcercaDe());
        itemAbrirExplorador.addActionListener(e -> abrirExplorador());
    
        menuArchivo.add(itemAbrirExplorador);
        menuArchivo.add(itemSalir);
        menuImagenes.add(itemVisor);
        menuAcercaDe.add(itemAcercaDe);
        
        barraMenu.add(menuArchivo);
        barraMenu.add(menuImagenes);
        barraMenu.add(menuAcercaDe);
     
        return barraMenu;
    } 
   
    private static void mostrarVisor() {
        if (!rutasImagenes.isEmpty()) {
            indiceActual = 0;
            if (jDialogVisor == null) {
                crearVisorImagenes();
            }
            mostrarImagen(rutasImagenes.get(indiceActual));
            jDialogVisor.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(ventanaPrincipal, "Primero selecciona imágenes desde 'Archivo -> Abrir el explorador de archivos'.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private static void crearVisorImagenes() {
        jDialogVisor = new JDialog(ventanaPrincipal, "Imágenes", true);
        jDialogVisor.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jDialogVisor.setLayout(new BorderLayout());
        jDialogVisor.setSize(1000, 700);
		
        etiquetaImagen = new JLabel("", SwingConstants.CENTER);

        JButton botonAnterior = new JButton("Anterior");
        JButton botonSiguiente = new JButton("Siguiente");
		 
        botonAnterior.addActionListener(e -> {
            if (indiceActual > 0) {
                indiceActual--;
                mostrarImagen(rutasImagenes.get(indiceActual));
            }
        });

        botonSiguiente.addActionListener(e -> {
            if (indiceActual < rutasImagenes.size() - 1) {
                indiceActual++;
                mostrarImagen(rutasImagenes.get(indiceActual));
            }
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonAnterior);
        panelBotones.add(botonSiguiente);

        jDialogVisor.add(etiquetaImagen, BorderLayout.CENTER);
        jDialogVisor.add(panelBotones, BorderLayout.SOUTH);
        jDialogVisor.setLocationRelativeTo(ventanaPrincipal);
    }
    
    private static void abrirExplorador() {
        String rutaPredeterminada = "C:\\Users\\usuario\\Pictures";
        File directorioInicial = new File(rutaPredeterminada);
        
        JFileChooser fileChooser = new JFileChooser(directorioInicial);
        // La selección se ha vuelto a cambiar a solo directorios
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        if (fileChooser.showOpenDialog(ventanaPrincipal) == JFileChooser.APPROVE_OPTION) {
            File carpetaSeleccionada = fileChooser.getSelectedFile();
            rutasImagenes.clear();
            
            if (carpetaSeleccionada.isDirectory()) {
                File[] archivosEnCarpeta = carpetaSeleccionada.listFiles();
                if (archivosEnCarpeta != null) {
                    for (File archivo : archivosEnCarpeta) {
                        if (archivo.isFile() && esImagen(archivo.getName())) {
                            rutasImagenes.add(archivo.getAbsolutePath());
                        }
                    }
                }
            }
            
            if (!rutasImagenes.isEmpty()) {
                indiceActual = 0;
                if (jDialogVisor == null) {
                    crearVisorImagenes();
                }
                mostrarImagen(rutasImagenes.get(indiceActual));
                jDialogVisor.setVisible(true);
            }
        }
    }

    private static boolean esImagen(String nombreArchivo) {
        String nombreMinusculas = nombreArchivo.toLowerCase();
        return nombreMinusculas.endsWith(".jpg") || nombreMinusculas.endsWith(".jpeg") || nombreMinusculas.endsWith(".png") || nombreMinusculas.endsWith(".gif");
    }
    
    private static void mostrarImagen(String ruta) {
        ImageIcon iconoOriginal = new ImageIcon(ruta);
        
        int anchoVentana = 1000;
        int altoVentana = 700;
        
        Image imagenOriginal = iconoOriginal.getImage();
        
        int anchoImagen = imagenOriginal.getWidth(null);
        int altoImagen = getHeight(iconoOriginal);
        
        double proporcionAncho = (double) anchoVentana / anchoImagen;
        double proporcionAlto = (double) altoVentana / altoImagen;
        double proporcionFinal = Math.min(proporcionAncho, proporcionAlto);
        
        int nuevoAncho = (int) (anchoImagen * proporcionFinal);
        int nuevoAlto = (int) (altoImagen * proporcionFinal);
        
        Image imagenEscalada = imagenOriginal.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
        etiquetaImagen.setIcon(iconoEscalado);
    }
    
    private static int getHeight(ImageIcon icono) {
        if (icono.getIconHeight() == -1) {
            return 0;
        } else {
            return icono.getIconHeight();
        }
    }

    public static void mostrarAcercaDe() {
        JOptionPane.showMessageDialog(ventanaPrincipal,"Un visor de imágenes simple.\n"
                + "Version 3.0\n" + "Creado por Bamba",
                "Acerca de Galeria de imágenes",
                JOptionPane.INFORMATION_MESSAGE);
    }
}