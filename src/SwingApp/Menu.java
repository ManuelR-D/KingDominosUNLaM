package SwingApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
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

import reyes.Bot;
import reyes.Jugador;
import reyes.KingDominoExcepcion;
import reyes.Partida;

public class Menu extends JDialog {

	private static final long serialVersionUID = -7979669249255750493L;
	private JPanel contentPane;
	private JTextField txtJugador1;
	private JTextField txtJugador2;
	private JTextField txtJugador3;
	private JTextField txtJugador4;
	private JComboBox<Integer> cantJugadores;
	private JRadioButton rdbtnElGranDuelo;
	private JRadioButton rdbtnBot1;
	private JRadioButton rdbtnBot2;
	private JRadioButton rdbtnBot3;
	private JRadioButton rdbtnBot4;
	private JLabel lblNewLabel;
	private JComboBox<String> texturas;
	private JRadioButton rdbtnDinastia;
	private JRadioButton rdbtnElReinoMedio;
	private JRadioButton rdbtnArmonia;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);
		
		lblNewLabel = new JLabel("Texturas");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		texturas = new JComboBox<String>();
		texturas.setModel(new DefaultComboBoxModel<String>(new String[] {"airbrush", "highTest", "test", "testPixAir", "testPixelated"}));
		texturas.setSelectedIndex(1);
		GridBagConstraints gbc_texturas = new GridBagConstraints();
		gbc_texturas.insets = new Insets(0, 0, 5, 5);
		gbc_texturas.fill = GridBagConstraints.HORIZONTAL;
		gbc_texturas.gridx = 2;
		gbc_texturas.gridy = 0;
		panel.add(texturas, gbc_texturas);

		JLabel lblNewLabel_3 = new JLabel("Cantidad de jugadores:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 1;
		panel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		cantJugadores = new JComboBox<Integer>();
		cantJugadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cantJugadoresSeleccionado();
			}
		});
		cantJugadores.setModel(new DefaultComboBoxModel<Integer>(new Integer[] { 2, 3, 4 }));
		GridBagConstraints gbc_cantJugadores = new GridBagConstraints();
		gbc_cantJugadores.insets = new Insets(0, 0, 5, 5);
		gbc_cantJugadores.fill = GridBagConstraints.HORIZONTAL;
		gbc_cantJugadores.gridx = 2;
		gbc_cantJugadores.gridy = 1;
		panel.add(cantJugadores, gbc_cantJugadores);

		rdbtnElGranDuelo = new JRadioButton("El gran duelo");
		GridBagConstraints gbc_rdbtnElGranDuelo = new GridBagConstraints();
		gbc_rdbtnElGranDuelo.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnElGranDuelo.gridx = 4;
		gbc_rdbtnElGranDuelo.gridy = 1;
		panel.add(rdbtnElGranDuelo, gbc_rdbtnElGranDuelo);

		txtJugador1 = new JTextField();
		txtJugador1.setText("Jugador 1");
		GridBagConstraints gbc_txtJugador1 = new GridBagConstraints();
		gbc_txtJugador1.insets = new Insets(0, 0, 5, 5);
		gbc_txtJugador1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtJugador1.gridx = 1;
		gbc_txtJugador1.gridy = 3;
		panel.add(txtJugador1, gbc_txtJugador1);
		txtJugador1.setColumns(10);

		rdbtnBot1 = new JRadioButton("Bot");
		GridBagConstraints gbc_rdbtnBot1 = new GridBagConstraints();
		gbc_rdbtnBot1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnBot1.gridx = 2;
		gbc_rdbtnBot1.gridy = 3;
		panel.add(rdbtnBot1, gbc_rdbtnBot1);
		
		rdbtnDinastia = new JRadioButton("Dinastia");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 4;
		gbc_rdbtnNewRadioButton.gridy = 3;
		panel.add(rdbtnDinastia, gbc_rdbtnNewRadioButton);

		txtJugador2 = new JTextField();
		txtJugador2.setText("Jugador 2");
		GridBagConstraints gbc_txtJugador2 = new GridBagConstraints();
		gbc_txtJugador2.insets = new Insets(0, 0, 5, 5);
		gbc_txtJugador2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtJugador2.gridx = 1;
		gbc_txtJugador2.gridy = 4;
		panel.add(txtJugador2, gbc_txtJugador2);
		txtJugador2.setColumns(10);

		rdbtnBot2 = new JRadioButton("Bot");
		GridBagConstraints gbc_rdbtnBot2 = new GridBagConstraints();
		gbc_rdbtnBot2.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnBot2.gridx = 2;
		gbc_rdbtnBot2.gridy = 4;
		panel.add(rdbtnBot2, gbc_rdbtnBot2);
		
		rdbtnElReinoMedio = new JRadioButton("El reino medio");
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 4;
		gbc_rdbtnNewRadioButton_1.gridy = 4;
		panel.add(rdbtnElReinoMedio, gbc_rdbtnNewRadioButton_1);

		txtJugador3 = new JTextField();
		txtJugador3.setEnabled(false);
		txtJugador3.setEditable(false);
		txtJugador3.setText("Jugador 3");
		GridBagConstraints gbc_txtJugador3 = new GridBagConstraints();
		gbc_txtJugador3.insets = new Insets(0, 0, 5, 5);
		gbc_txtJugador3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtJugador3.gridx = 1;
		gbc_txtJugador3.gridy = 5;
		panel.add(txtJugador3, gbc_txtJugador3);
		txtJugador3.setColumns(10);

		rdbtnBot3 = new JRadioButton("Bot");
		rdbtnBot3.setEnabled(false);
		GridBagConstraints gbc_rdbtnBot3 = new GridBagConstraints();
		gbc_rdbtnBot3.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnBot3.gridx = 2;
		gbc_rdbtnBot3.gridy = 5;
		panel.add(rdbtnBot3, gbc_rdbtnBot3);
		
		rdbtnArmonia = new JRadioButton("Armonia");
		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_2.gridx = 4;
		gbc_rdbtnNewRadioButton_2.gridy = 5;
		panel.add(rdbtnArmonia, gbc_rdbtnNewRadioButton_2);

		txtJugador4 = new JTextField();
		txtJugador4.setEnabled(false);
		txtJugador4.setEditable(false);
		txtJugador4.setText("Jugador 4");
		GridBagConstraints gbc_txtJugador4 = new GridBagConstraints();
		gbc_txtJugador4.insets = new Insets(0, 0, 0, 5);
		gbc_txtJugador4.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtJugador4.gridx = 1;
		gbc_txtJugador4.gridy = 6;
		panel.add(txtJugador4, gbc_txtJugador4);
		txtJugador4.setColumns(10);

		rdbtnBot4 = new JRadioButton("Bot");
		rdbtnBot4.setEnabled(false);
		GridBagConstraints gbc_rdbtnBot4 = new GridBagConstraints();
		gbc_rdbtnBot4.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnBot4.gridx = 2;
		gbc_rdbtnBot4.gridy = 6;
		panel.add(rdbtnBot4, gbc_rdbtnBot4);
	}

	protected void cantJugadoresSeleccionado() {
		int cant = (int) cantJugadores.getSelectedItem();
		switch (cant) {
		case 2:
			rdbtnElGranDuelo.setEnabled(true);
			rdbtnBot3.setEnabled(false);
			rdbtnBot4.setEnabled(false);
			txtJugador3.setEditable(false);
			txtJugador4.setEditable(false);
			txtJugador3.setEnabled(false);
			txtJugador4.setEnabled(false);
			break;
		case 3:
			rdbtnElGranDuelo.setSelected(false);
			rdbtnElGranDuelo.setEnabled(false);
			rdbtnBot3.setEnabled(true);
			rdbtnBot4.setEnabled(false);
			txtJugador3.setEditable(true);
			txtJugador4.setEditable(false);
			txtJugador3.setEnabled(true);
			txtJugador4.setEnabled(false);

			break;
		case 4:
			rdbtnElGranDuelo.setSelected(false);
			rdbtnElGranDuelo.setEnabled(false);
			rdbtnBot3.setEnabled(true);
			rdbtnBot4.setEnabled(true);
			txtJugador3.setEditable(true);
			txtJugador4.setEditable(true);
			txtJugador3.setEnabled(true);
			txtJugador4.setEnabled(true);
			break;
		}
	}

	protected void enviarDatos() {
		int cantidadCartas = 48;
		int tamTablero = rdbtnElGranDuelo.isSelected() ? 7 : 5;
		int cant = (int) cantJugadores.getSelectedItem();
		List<Jugador> jugadores = new ArrayList<Jugador>(cant);

		if (rdbtnBot1.isSelected()) {
			jugadores.add(new Bot(txtJugador1.getText(), tamTablero));
		} else {
			jugadores.add(new Jugador(txtJugador1.getText(), tamTablero));
		}
		if (rdbtnBot2.isSelected()) {
			jugadores.add(new Bot(txtJugador2.getText(), tamTablero));
		} else {
			jugadores.add(new Jugador(txtJugador2.getText(), tamTablero));
		}
		if (cant > 2) {
			if (rdbtnBot3.isSelected()) {
				jugadores.add(new Bot(txtJugador3.getText(), tamTablero));
			} else {
				jugadores.add(new Jugador(txtJugador3.getText(), tamTablero));
			}
			if (cant > 3) {
				if (rdbtnBot4.isSelected()) {
					jugadores.add(new Bot(txtJugador4.getText(), tamTablero));
				} else {
					jugadores.add(new Jugador(txtJugador4.getText(), tamTablero));
				}

			}

		}
		String textura=(String) texturas.getSelectedItem();
		this.setVisible(false);

		Thread thread =new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String modo = "";
					Partida p = new Partida(jugadores, tamTablero, cantidadCartas,textura);
					if(rdbtnDinastia.isSelected())
						modo = modo + "Dinastia|";
					if(rdbtnElReinoMedio.isSelected())
						modo = modo + "ReinoMedio|";
					if(rdbtnArmonia.isSelected())
						modo = modo + "Armonia|";
					p.iniciarPartida(modo);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (KingDominoExcepcion e) {
					e.printStackTrace();
				}
				
			}
		});
		thread.start();
		this.dispose();
	}

}
