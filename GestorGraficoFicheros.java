package Ficheros;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class GestorGraficoFicheros extends JFrame {

	// componentes de la interfaz
	private JTextField campoRuta;
	private JTextArea areaContenido;
	private JButton botonCrearEscribir;
	private JButton botonLeer;
	private JButton botonInfo;

	public GestorGraficoFicheros() {
		// configuracion de la ventana principal
		setTitle("Gestor Básico de Ficheros");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// panel superior para la entrada de ruta
		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new FlowLayout());
		panelSuperior.add(new JLabel("Ruta del archivo:"));
		campoRuta = new JTextField(30);
		panelSuperior.add(campoRuta);

		// panel de botones
		JPanel panelBotones = new JPanel();
		botonCrearEscribir = new JButton("Crear y Escribir");
		botonLeer = new JButton("Leer Contenido");
		botonInfo = new JButton("Mostrar Información");
		panelBotones.add(botonCrearEscribir);
		panelBotones.add(botonLeer);
		panelBotones.add(botonInfo);

		// area de texto para mostrar resultados
		areaContenido = new JTextArea();
		areaContenido.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(areaContenido);

		// añadir los paneles y componentes a la ventana
		add(panelSuperior, BorderLayout.NORTH);
		add(panelBotones, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);

		// Logica de los botones(Listener)
		botonCrearEscribir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				crearYEscribirFichero();
			}
		});

		botonLeer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leerFichero();
			}
		});

		botonInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarInfoFichero();
			}
		});
		// hacer la ventana visible
		setVisible(true);
	}

	private void crearYEscribirFichero() {
		String ruta = campoRuta.getText();
		if (ruta.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, introduce una ruta.");
			return;
		}

		File archivo = new File(ruta);
		File directorio = archivo.getParentFile();

		// crear directorio si no existe
		if (directorio != null && !directorio.exists()) {
			directorio.mkdirs();
		}

		try (FileWriter writer = new FileWriter(archivo)) {
			writer.write("Este es el contenido de prueba del archivo. \n");
			writer.write("¡Hemos creado y escrito en un fichero desde Swing!");
			areaContenido.setText("Archivo creado y escrito con éxito en:\n" + archivo.getAbsolutePath());
		} catch (IOException ex) {
			areaContenido.setText("Error al crear o escribir en el archivo: " + ex.getMessage());
		}
	}

	private void leerFichero() {
		String ruta = campoRuta.getText();
		if (ruta.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, introduce una ruta.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		File archivo = new File(ruta);
		if (!archivo.exists()) {
			JOptionPane.showMessageDialog(this, "El archivo no existe.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		StringBuilder contenido = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = reader.readLine()) != null) {
				contenido.append(linea).append("\n");
			}
			areaContenido.setText(contenido.toString());
		} catch (IOException ex) {
			areaContenido.setText("Error al leer el archivo: " + ex.getMessage());
		}
	}

	private void mostrarInfoFichero() {
		String ruta = campoRuta.getText();
		if (ruta.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, introduce una ruta.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		File archivo = new File(ruta);
		if (!archivo.exists()) {
			JOptionPane.showMessageDialog(this, "El archivo no existe.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String info = "--- Información del Fichero ---\n" + "Nombre: " + archivo.getName() + "\n" + "Ruta absoluta: "
				+ archivo.getAbsolutePath() + "\n" + "Tamaño: " + archivo.length() + " bytes\n" + "Es un directorio: "
				+ archivo.isDirectory() + "\n" + "Es un fichero: " + archivo.isFile();

		areaContenido.setText(info);
	}

	public static void main(String[] args) {
		new GestorGraficoFicheros();
	}
}