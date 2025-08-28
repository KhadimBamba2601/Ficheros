package FicherosJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class GestorGraficoFicheros extends JFrame {

    // Componentes de la interfaz
    private JComboBox<String> comboRutas;
    private JTextArea areaContenido;
    private JButton botonGuardar;
    private JButton botonLeer;
    private JButton botonInfo;
    private JButton botonRenombrar;
    private JButton botonEliminar;
    private JButton botonBuscar;
    private JButton botonLimpiar;
    private JTree treeExplorador;
    private DefaultTreeModel treeModel;

    // Historial de rutas
    private final LinkedList<String> historialRutas = new LinkedList<>();
    private static final int MAX_HISTORIAL = 10;

    public GestorGraficoFicheros() {
        setTitle("Gestor de Ficheros");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Espacio entre componentes
        getContentPane().setBackground(new Color(240, 240, 240));

        // --- Panel Superior (Norte) ---
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelSuperior.setBackground(new Color(220, 220, 220));

        panelSuperior.add(new JLabel("Ruta del archivo:"));
        comboRutas = new JComboBox<>();
        comboRutas.setEditable(true);
        comboRutas.setPreferredSize(new Dimension(400, 25));

        // Añadir listener para la validación en tiempo real
        JTextField campoRuta = (JTextField) comboRutas.getEditor().getEditorComponent();
        campoRuta.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { validarRuta(); }
            public void removeUpdate(DocumentEvent e) { validarRuta(); }
            public void insertUpdate(DocumentEvent e) { validarRuta(); }
        });

        panelSuperior.add(comboRutas);
        
        botonBuscar = new JButton("...");
        botonBuscar.setToolTipText("Buscar archivo...");
        panelSuperior.add(botonBuscar);

        botonLimpiar = new JButton("X");
        botonLimpiar.setToolTipText("Limpiar campo de ruta");
        panelSuperior.add(botonLimpiar);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central (Centro) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);

        // Explorador de Archivos (Izquierda)
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Mi PC");
        treeModel = new DefaultTreeModel(raiz);
        treeExplorador = new JTree(treeModel);
        
        treeExplorador.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) treeExplorador.getLastSelectedPathComponent();
            if (nodo != null && nodo.getUserObject() instanceof File) {
                File archivoSeleccionado = (File) nodo.getUserObject();
                comboRutas.setSelectedItem(archivoSeleccionado.getAbsolutePath());
            }
        });

        // Cargar los drives/unidades iniciales
        File[] drives = File.listRoots();
        for (File drive : drives) {
            DefaultMutableTreeNode driveNode = new DefaultMutableTreeNode(drive);
            raiz.add(driveNode);
            cargarNodos(driveNode);
        }

        JScrollPane scrollExplorador = new JScrollPane(treeExplorador);
        scrollExplorador.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Explorador de Archivos"));
        splitPane.setLeftComponent(scrollExplorador);

        // Panel de Contenido y Botones (Derecha)
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Acciones y Contenido"));

        areaContenido = new JTextArea();
        areaContenido.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaContenido.setBackground(Color.WHITE);
        
        JScrollPane scrollContenido = new JScrollPane(areaContenido);
        panelDerecho.add(scrollContenido, BorderLayout.CENTER);

        // Panel de Botones de Acciones
        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 10, 0));
        botonLeer = new JButton("Leer");
        botonGuardar = new JButton("Guardar");
        botonInfo = new JButton("Info");
        botonRenombrar = new JButton("Renombrar");
        botonEliminar = new JButton("Eliminar");

        panelBotones.add(botonLeer);
        panelBotones.add(botonGuardar);
        panelBotones.add(botonInfo);
        panelBotones.add(botonRenombrar);
        panelBotones.add(botonEliminar);
        
        panelDerecho.add(panelBotones, BorderLayout.SOUTH);

        splitPane.setRightComponent(panelDerecho);
        add(splitPane, BorderLayout.CENTER);
        
        // Cargar historial y listeners
        cargarHistorial();
        addListeners();
        validarRuta();

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Valida la ruta en tiempo real y cambia el color del campo de texto.
     */
    private void validarRuta() {
        String ruta = (String) comboRutas.getEditor().getItem();
        File archivo = new File(ruta);
        Color colorFondo;
        
        if (ruta.trim().isEmpty()) {
            colorFondo = Color.WHITE;
        } else if (archivo.exists() && archivo.isDirectory()) {
            colorFondo = new Color(255, 255, 204); // Amarillo claro
        } else if (archivo.exists() && archivo.isFile()) {
            colorFondo = new Color(204, 255, 204); // Verde claro
        } else {
            colorFondo = new Color(255, 204, 204); // Rojo claro
        }
        
        ((JTextField) comboRutas.getEditor().getEditorComponent()).setBackground(colorFondo);
    }
    
    /**
     * Carga los nodos hijos de un directorio para el JTree.
     */
    private void cargarNodos(DefaultMutableTreeNode nodoPadre) {
        File directorio = (File) nodoPadre.getUserObject();
        File[] hijos = directorio.listFiles();
        if (hijos == null) return;
        
        for (File hijo : hijos) {
            if (hijo.isHidden() || !hijo.canRead()) continue;
            DefaultMutableTreeNode nuevoNodo = new DefaultMutableTreeNode(hijo);
            if (hijo.isDirectory()) {
                nuevoNodo.add(new DefaultMutableTreeNode("Cargando...")); // Nodo dummy
            }
            nodoPadre.add(nuevoNodo);
        }
    }
    
    /**
     * Mantiene un historial de las últimas rutas seleccionadas.
     */
    private void actualizarHistorial(String ruta) {
        if (!historialRutas.contains(ruta)) {
            historialRutas.addFirst(ruta);
            if (historialRutas.size() > MAX_HISTORIAL) {
                historialRutas.removeLast();
            }
            comboRutas.removeAllItems();
            for (String item : historialRutas) {
                comboRutas.addItem(item);
            }
        }
        comboRutas.setSelectedItem(ruta);
    }
    
    /**
     * Carga el historial inicial del JComboBox.
     */
    private void cargarHistorial() {
        historialRutas.add("C:\\Users\\usuario\\Desktop\\NuevoDocumentoTexto.txt");
        historialRutas.add("C:\\Windows\\system32");
        actualizarHistorial(historialRutas.getFirst());
    }

    /**
     * Configura los ActionListeners para todos los botones.
     */
    private void addListeners() {
        botonLeer.addActionListener(e -> leerFichero());
        botonGuardar.addActionListener(e -> guardarFichero());
        botonInfo.addActionListener(e -> mostrarInfoFichero());
        botonRenombrar.addActionListener(e -> renombrarFichero());
        botonEliminar.addActionListener(e -> eliminarFichero());
        botonLimpiar.addActionListener(e -> ((JTextField) comboRutas.getEditor().getEditorComponent()).setText(""));
        botonBuscar.addActionListener(e -> buscarFichero());
        
        comboRutas.addActionListener(e -> {
            if (comboRutas.getEditor().getItem() instanceof String) {
                String ruta = (String) comboRutas.getEditor().getItem();
                actualizarHistorial(ruta);
            }
        });
        
        treeExplorador.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeExpanded(javax.swing.event.TreeExpansionEvent event) {
                DefaultMutableTreeNode nodoExpandido = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                if (nodoExpandido.getChildCount() == 1 && nodoExpandido.getFirstChild().toString().equals("Cargando...")) {
                    nodoExpandido.removeAllChildren();
                    cargarNodos(nodoExpandido);
                    treeModel.nodeStructureChanged(nodoExpandido);
                }
            }
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent event) {}
        });
    }

    private void buscarFichero() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            actualizarHistorial(archivoSeleccionado.getAbsolutePath());
        }
    }

    private void leerFichero() {
        String ruta = (String) comboRutas.getEditor().getItem();
        if (ruta.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una ruta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File archivo = new File(ruta);
        if (!archivo.exists() || !archivo.isFile()) {
            JOptionPane.showMessageDialog(this, "La ruta no es un archivo válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            areaContenido.setText(contenido.toString());
            areaContenido.setCaretPosition(0);
            areaContenido.setEditable(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarFichero() {
        String ruta = (String) comboRutas.getEditor().getItem();
        if (ruta.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una ruta para guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File archivo = new File(ruta);
        if (archivo.isDirectory()) {
            JOptionPane.showMessageDialog(this, "No se puede guardar en un directorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int opcion = JOptionPane.YES_OPTION;
        if (archivo.exists()) {
             opcion = JOptionPane.showConfirmDialog(this, "¿Deseas sobreescribir el archivo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        }

        if (opcion == JOptionPane.YES_OPTION) {
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(areaContenido.getText());
                areaContenido.setText("¡Éxito! Archivo guardado con éxito en:\n" + archivo.getAbsolutePath());
                areaContenido.setEditable(false);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarInfoFichero() {
        String ruta = (String) comboRutas.getEditor().getItem();
        if (ruta.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una ruta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("--- Información del Fichero ---\n");
        info.append("Nombre: ").append(archivo.getName()).append("\n");
        info.append("Ruta absoluta: ").append(archivo.getAbsolutePath()).append("\n");
        info.append("Tamaño: ").append(archivo.length()).append(" bytes\n");
        info.append("Es un directorio: ").append(archivo.isDirectory() ? "Sí" : "No").append("\n");
        info.append("Es un fichero: ").append(archivo.isFile() ? "Sí" : "No").append("\n");
        info.append("Legible: ").append(archivo.canRead() ? "Sí" : "No").append("\n");
        info.append("Escribible: ").append(archivo.canWrite() ? "Sí" : "No").append("\n");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        info.append("Última modificación: ").append(sdf.format(new java.util.Date(archivo.lastModified()))).append("\n");

        try {
            Path filePath = archivo.toPath();
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            info.append("Fecha de creación: ").append(attrs.creationTime()).append("\n");
        } catch (IOException e) {
            info.append("Fecha de creación: No disponible\n");
        }
        
        areaContenido.setEditable(false);
        areaContenido.setText(info.toString());
    }
    
    private void renombrarFichero() {
        String rutaActual = (String) comboRutas.getEditor().getItem();
        if (rutaActual.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una ruta para renombrar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File archivoActual = new File(rutaActual);
        if (!archivoActual.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nuevoNombre = JOptionPane.showInputDialog(this, "Introduce el nuevo nombre:");
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            File nuevoArchivo = new File(archivoActual.getParent(), nuevoNombre);
            if (archivoActual.renameTo(nuevoArchivo)) {
                actualizarHistorial(nuevoArchivo.getAbsolutePath());
                areaContenido.setText("Archivo renombrado con éxito a:\n" + nuevoArchivo.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, "Error al renombrar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarFichero() {
        String ruta = (String) comboRutas.getEditor().getItem();
        if (ruta.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una ruta para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Estás seguro de que quieres eliminar este archivo/directorio?\n" + ruta, 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            if (archivo.delete()) {
                comboRutas.setSelectedItem("");
                areaContenido.setText("Archivo/Directorio eliminado con éxito: " + ruta);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el archivo/directorio.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestorGraficoFicheros::new);
    }
}