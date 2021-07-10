package SwingMenu;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import SwingApp.PanelFicha;
import SwingApp.VentanaJueguito;
import netcode.Cliente;
import netcode.MensajeAServidor;
import netcode.Sala;
import reyes.Bot;
import reyes.Carta;
import reyes.Ficha;
import reyes.Jugador;
import reyes.KingDominoExcepcion;
import reyes.Mazo;
import reyes.Partida;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Menu extends JDialog {

	private static final long serialVersionUID = -7979669249255750493L;
	private JPanel contentPane;
	private List<JTextField> txtJugadores = new ArrayList<JTextField>(4);
	private JComboBox<Integer> cantJugadores;
	private JRadioButton rdbtnElGranDuelo;
	private List<JRadioButton> rdbtns;
	private JLabel lblNewLabel;
	private JComboBox<String> texturas;
	private JRadioButton rdbtnDinastia;
	private JRadioButton rdbtnElReinoMedio;
	private JRadioButton rdbtnArmonia;
	private JComboBox<String> mazos;
	private JLabel lblNewLabel_1;
	private JButton btnNewButton;
	private Cliente cliente;
	private Sala sala;
	private List<String> usuarios;

	public Menu(Cliente cliente, Sala sala, SalaDeEspera salaDeEspera, String nombresUsuarios) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salaDeEspera.setEnabledBtnIniciarPartida(true);
			}
		});
		this.cliente = cliente;
		this.sala = sala;
		usuarios = new ArrayList<String>();
		String[] arrayNombresUsuarios = nombresUsuarios.split("\\n");
		for (String nombre : arrayNombresUsuarios) {
			usuarios.add(nombre);
		}
		setVisible(true);
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JButton btnComenzar = new JButton("Comenzar partida");
		btnComenzar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarDatos();
			}
		});
		contentPane.add(btnComenzar, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 50, 0, 0));
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		lblNewLabel_1 = new JLabel("Mazo");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 0;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		mazos = new JComboBox<String>();
		File f = new File("./assets");

		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		};

		File[] files = f.listFiles(textFilter);

		for (File file : files) {
			if (file.isDirectory()) {
				System.out.print("directory:");
			} else {
				System.out.print("     file:");
			}
			try {
				System.out.println(file.getCanonicalPath());
				System.out.println(file.getName().substring(0, file.getName().length() - 4));
				mazos.addItem(file.getName().substring(0, file.getName().length() - 4));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		mazos.setSelectedIndex(0);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 0;
		panel.add(mazos, gbc_comboBox);

		btnNewButton = new JButton("Visualizar mazo");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onClickVisualizarMazo();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 0;
		panel.add(btnNewButton, gbc_btnNewButton);

		lblNewLabel = new JLabel("Texturas");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		texturas = new JComboBox<String>();

		textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".png");
			}
		};
		f = new File("./assets/mazos/");
		files = f.listFiles(textFilter);

		for (File file : files) {
			if (file.isDirectory()) {
				System.out.print("directory:");
			} else {
				System.out.print("     file:");
			}
			try {
				System.out.println(file.getCanonicalPath());
				System.out.println(file.getName().substring(0, file.getName().length() - 4));
				texturas.addItem(file.getName().substring(0, file.getName().length() - 4));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		texturas.setSelectedIndex(1);
		GridBagConstraints gbc_texturas = new GridBagConstraints();
		gbc_texturas.insets = new Insets(0, 0, 5, 5);
		gbc_texturas.fill = GridBagConstraints.HORIZONTAL;
		gbc_texturas.gridx = 2;
		gbc_texturas.gridy = 1;
		panel.add(texturas, gbc_texturas);

		JLabel lblNewLabel_3 = new JLabel("Cantidad de jugadores:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 2;
		panel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		cantJugadores = new JComboBox<Integer>();
		cantJugadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cantJugadoresSeleccionado(usuarios.size());
			}
		});

		GridBagConstraints gbc_cantJugadores = new GridBagConstraints();
		gbc_cantJugadores.insets = new Insets(0, 0, 5, 5);
		gbc_cantJugadores.fill = GridBagConstraints.HORIZONTAL;
		gbc_cantJugadores.gridx = 2;
		gbc_cantJugadores.gridy = 2;
		panel.add(cantJugadores, gbc_cantJugadores);

		rdbtnElGranDuelo = new JRadioButton("El gran duelo");
		GridBagConstraints gbc_rdbtnElGranDuelo = new GridBagConstraints();
		gbc_rdbtnElGranDuelo.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnElGranDuelo.gridx = 4;
		gbc_rdbtnElGranDuelo.gridy = 2;
		panel.add(rdbtnElGranDuelo, gbc_rdbtnElGranDuelo);

		JTextField txtJugador1 = new JTextField();
		txtJugadores.add(txtJugador1);
		txtJugador1.setEnabled(false);
		txtJugador1.setText("Jugador 1");
		GridBagConstraints gbc_txtJugador1 = new GridBagConstraints();
		gbc_txtJugador1.insets = new Insets(0, 0, 5, 5);
		gbc_txtJugador1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtJugador1.gridx = 1;
		gbc_txtJugador1.gridy = 4;
		panel.add(txtJugador1, gbc_txtJugador1);
		txtJugador1.setColumns(10);

		rdbtns = new ArrayList<JRadioButton>();
		JRadioButton rdbtnBot1 = new JRadioButton("Bot");
		rdbtns.add(rdbtnBot1);
		rdbtnBot1.setEnabled(false);
		GridBagConstraints gbc_rdbtnBot1 = new GridBagConstraints();
		gbc_rdbtnBot1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnBot1.gridx = 2;
		gbc_rdbtnBot1.gridy = 4;
		panel.add(rdbtnBot1, gbc_rdbtnBot1);

		rdbtnDinastia = new JRadioButton("Dinastia");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 4;
		gbc_rdbtnNewRadioButton.gridy = 4;
		panel.add(rdbtnDinastia, gbc_rdbtnNewRadioButton);

		JTextField txtJugador2 = new JTextField();
		txtJugadores.add(txtJugador2);
		txtJugador2.setText("Jugador 2");
		GridBagConstraints gbc_txtJugador2 = new GridBagConstraints();
		gbc_txtJugador2.insets = new Insets(0, 0, 5, 5);
		gbc_txtJugador2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtJugador2.gridx = 1;
		gbc_txtJugador2.gridy = 5;
		panel.add(txtJugador2, gbc_txtJugador2);
		txtJugador2.setColumns(10);

		JRadioButton rdbtnBot2 = new JRadioButton("Bot");
		rdbtns.add(rdbtnBot2);
		rdbtnBot2.setEnabled(false);
		GridBagConstraints gbc_rdbtnBot2 = new GridBagConstraints();
		gbc_rdbtnBot2.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnBot2.gridx = 2;
		gbc_rdbtnBot2.gridy = 5;
		panel.add(rdbtnBot2, gbc_rdbtnBot2);

		rdbtnElReinoMedio = new JRadioButton("El reino medio");
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 4;
		gbc_rdbtnNewRadioButton_1.gridy = 5;
		panel.add(rdbtnElReinoMedio, gbc_rdbtnNewRadioButton_1);

		JTextField txtJugador3 = new JTextField();
		txtJugadores.add(txtJugador3);
		txtJugador3.setEnabled(false);
		txtJugador3.setEditable(false);
		txtJugador3.setText("Jugador 3");
		GridBagConstraints gbc_txtJugador3 = new GridBagConstraints();
		gbc_txtJugador3.insets = new Insets(0, 0, 5, 5);
		gbc_txtJugador3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtJugador3.gridx = 1;
		gbc_txtJugador3.gridy = 6;
		panel.add(txtJugador3, gbc_txtJugador3);
		txtJugador3.setColumns(10);

		JRadioButton rdbtnBot3 = new JRadioButton("Bot");
		rdbtns.add(rdbtnBot3);
		rdbtnBot3.setEnabled(false);

		GridBagConstraints gbc_rdbtnBot3 = new GridBagConstraints();
		gbc_rdbtnBot3.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnBot3.gridx = 2;
		gbc_rdbtnBot3.gridy = 6;
		panel.add(rdbtnBot3, gbc_rdbtnBot3);

		rdbtnArmonia = new JRadioButton("Armonia");
		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_2.gridx = 4;
		gbc_rdbtnNewRadioButton_2.gridy = 6;
		panel.add(rdbtnArmonia, gbc_rdbtnNewRadioButton_2);

		JTextField txtJugador4 = new JTextField();
		txtJugadores.add(txtJugador4);
		txtJugador4.setEnabled(false);
		txtJugador4.setEditable(false);
		txtJugador4.setText("Jugador 4");
		GridBagConstraints gbc_txtJugador4 = new GridBagConstraints();
		gbc_txtJugador4.insets = new Insets(0, 0, 0, 5);
		gbc_txtJugador4.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtJugador4.gridx = 1;
		gbc_txtJugador4.gridy = 7;
		panel.add(txtJugador4, gbc_txtJugador4);
		txtJugador4.setColumns(10);

		JRadioButton rdbtnBot4 = new JRadioButton("Bot");
		rdbtns.add(rdbtnBot4);
		rdbtnBot4.setEnabled(false);
		GridBagConstraints gbc_rdbtnBot4 = new GridBagConstraints();
		gbc_rdbtnBot4.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnBot4.gridx = 2;
		gbc_rdbtnBot4.gridy = 7;
		panel.add(rdbtnBot4, gbc_rdbtnBot4);
		actualizarComboBox(usuarios.size());
		actualizarNombresJugadores(usuarios);
	}

	private void actualizarNombresJugadores(List<String> usuarios) {

		int i = 0;
		while (i < usuarios.size()) {
			JTextField jTextField = txtJugadores.get(i);
			jTextField.setText(usuarios.get(i));
			jTextField.setEnabled(false);
			jTextField.setEditable(false);
			i++;
		}
		while (i < 4) {
			JTextField jTextField = txtJugadores.get(i);
			jTextField.setText("Jugador " + (i + 1));
			jTextField.setEnabled(false);
			jTextField.setEditable(false);
			i++;
		}
		cantJugadoresSeleccionado(usuarios.size());
	}

	private void actualizarComboBox(int cantJugadoresActualizado) {
		List<Integer> jugadores = new ArrayList<Integer>(cantJugadoresActualizado);
		for (int i = Math.max(cantJugadoresActualizado, 2); i <= 4; i++) {
			jugadores.add(i);
		}
		if (jugadores.isEmpty()) {
			cantJugadores.setEnabled(false);
		} else {
			cantJugadores.setEnabled(true);
		}
		cantJugadores.setModel(new DefaultComboBoxModel<Integer>(jugadores.toArray(new Integer[jugadores.size()])));
		cantJugadores.setSelectedIndex(0);
	}

	protected void onClickVisualizarMazo() {
		JFrame tempFrame = new JFrame();
		JPanel tempContentPane = new JPanel();
		tempFrame.setSize(1340, 540);
		tempContentPane.setLayout(null);
		tempFrame.setContentPane(tempContentPane);

		PanelFicha pF = null;
		VentanaJueguito.cargarTexturas();
		VentanaJueguito.cargarTexturaMazo((String) texturas.getSelectedItem());
		Mazo mazo = new Mazo(48, (String) mazos.getSelectedItem());
		List<Carta> cartas = mazo.getCartas();
		int i = 0;
		for (Carta carta : cartas) {
			Ficha[] fichas = carta.getFichas();
			pF = new PanelFicha(fichas[0], 0, 0, 1, 1);
			int tamFicha = PanelFicha.getTamFicha();
			pF.setBounds(i % 16 * tamFicha, (i / 16) * tamFicha, tamFicha, tamFicha);
			tempContentPane.add(pF);
			i++;
			pF = new PanelFicha(fichas[1], 0, 0, 1, 1);
			pF.setBounds(i % 16 * tamFicha, (i / 16) * tamFicha, tamFicha, tamFicha);
			i++;
			tempContentPane.add(pF);
		}
		tempFrame.setVisible(true);

	}

	protected void cantJugadoresSeleccionado(int cantUsuarios) {
		JTextField txtJugador2 = txtJugadores.get(1);
		JTextField txtJugador3 = txtJugadores.get(2);
		JTextField txtJugador4 = txtJugadores.get(3);
		JRadioButton rdbtnBot2 = rdbtns.get(1);
		JRadioButton rdbtnBot3 = rdbtns.get(2);
		JRadioButton rdbtnBot4 = rdbtns.get(3);
		int cant = (int) cantJugadores.getSelectedItem();
		switch (cant) {
		case 2:
			rdbtnElGranDuelo.setEnabled(true);
			if (cantUsuarios< 2) {
				txtJugador2.setEditable(true);
				txtJugador2.setEnabled(true);
				rdbtnBot2.setSelected(true);
			} else {
				txtJugador2.setEditable(false);
				txtJugador2.setEnabled(false);
				rdbtnBot2.setSelected(false);
			}
			rdbtnBot3.setSelected(false);
			rdbtnBot4.setSelected(false);
			txtJugador3.setEditable(false);
			txtJugador4.setEditable(false);
			txtJugador3.setEnabled(false);
			txtJugador4.setEnabled(false);
			break;
		case 3:
			rdbtnElGranDuelo.setSelected(false);
			rdbtnElGranDuelo.setEnabled(false);
			if (cantUsuarios < 3) {
				txtJugador3.setEditable(true);
				txtJugador3.setEnabled(true);
				rdbtnBot3.setSelected(true);
			} else {
				txtJugador3.setEditable(false);
				txtJugador3.setEnabled(false);
				rdbtnBot3.setSelected(false);
			}
			rdbtnBot4.setSelected(false);
			txtJugador4.setEditable(false);
			txtJugador4.setEnabled(false);

			break;
		case 4:
			rdbtnElGranDuelo.setSelected(false);
			rdbtnElGranDuelo.setEnabled(false);
			if (cantUsuarios < 3) {
				txtJugador3.setEditable(true);
				txtJugador3.setEnabled(true);
				rdbtnBot3.setSelected(true);
			} else {
				txtJugador3.setEditable(false);
				txtJugador3.setEnabled(false);
				rdbtnBot3.setSelected(false);
			}
			if (cantUsuarios < 4) {
				txtJugador4.setEditable(true);
				txtJugador4.setEnabled(true);
				rdbtnBot4.setSelected(true);
			} else {
				txtJugador4.setEditable(false);
				txtJugador4.setEnabled(false);
				rdbtnBot4.setSelected(false);
			}
			break;
		}
		rdbtnBot2.setEnabled(false);
		rdbtnBot3.setEnabled(false);
		rdbtnBot4.setEnabled(false);
	}

	protected void enviarDatos() {
		int tamTablero = rdbtnElGranDuelo.isSelected() ? 7 : 5;
		int cant = (int) cantJugadores.getSelectedItem();
		JTextField txtJugador1 = txtJugadores.get(0);
		JTextField txtJugador2 = txtJugadores.get(1);
		JTextField txtJugador3 = txtJugadores.get(2);
		JTextField txtJugador4 = txtJugadores.get(3);
		JRadioButton rdbtnBot1 = rdbtns.get(0);
		JRadioButton rdbtnBot2 = rdbtns.get(1);
		JRadioButton rdbtnBot3 = rdbtns.get(2);
		JRadioButton rdbtnBot4 = rdbtns.get(3);
		String nombreJugadores = "";
		String mensajeCrearPartida = "";

		if (rdbtnBot1.isSelected()) {
			mensajeCrearPartida += "B";
		} else {
			mensajeCrearPartida += "J";
		}
		nombreJugadores += txtJugador1.getText() + "|";
		if (rdbtnBot2.isSelected()) {
			mensajeCrearPartida += "B";
		} else {
			mensajeCrearPartida += "J";
		}
		nombreJugadores += txtJugador2.getText() + "|";
		if (cant > 2) {
			if (rdbtnBot3.isSelected()) {
				mensajeCrearPartida += "B";
			} else {
				mensajeCrearPartida += "J";
			}
			nombreJugadores += txtJugador3.getText() + "|";
			if (cant > 3) {
				if (rdbtnBot4.isSelected()) {
					mensajeCrearPartida += "B";
				} else {
					mensajeCrearPartida += "J";
				}
				nombreJugadores += txtJugador4.getText() + "|";
			}

		}
		mensajeCrearPartida += "," + nombreJugadores;
		mensajeCrearPartida += "," + tamTablero;

		String textura = (String) texturas.getSelectedItem();
		mensajeCrearPartida += "," + textura;
		this.setVisible(false);
		String mazo = (String) mazos.getSelectedItem();
		mensajeCrearPartida += "," + mazo;
		String modoDeJuego = "";
		if (rdbtnDinastia.isSelected())
			modoDeJuego += ",D";
		if (rdbtnElReinoMedio.isSelected())
			modoDeJuego += ",R";
		if (rdbtnArmonia.isSelected())
			modoDeJuego += ",A";
		if (modoDeJuego == "") {
			modoDeJuego = "N";
		}
		mensajeCrearPartida += "," + modoDeJuego;
		MensajeAServidor msj = new MensajeAServidor(mensajeCrearPartida, sala, 13);
		cliente.enviarMensaje(msj);

		this.dispose();
	}

}
