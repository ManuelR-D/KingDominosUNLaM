package SwingApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import reyes.Carta;
import reyes.Ficha;
import reyes.Mazo;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextField;

public class CreadorDeMazo extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreadorDeMazo frame = new CreadorDeMazo();
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
	JComboBox<String> modelos;
	JComboBox<String> tiposFicha;
	JComboBox<Integer> cantidadCoronas;
	private Map<String, List<Integer>> textureMap = new HashMap<String,List<Integer>>();
	private JPanel panelPreviewCarta;
	private JPanel panelPreviewMazo;
	public CreadorDeMazo() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("./assets/original.txt"));
		int i = 0;
		while(sc.hasNext()) {
			String tipo =  sc.nextLine();
			if(!textureMap.containsKey(tipo)) {
				List<Integer> indices = new LinkedList<Integer>();
				textureMap.put(tipo, indices);
			}else {
				textureMap.get(tipo).add(i);
			}
			sc.nextLine();
			i++;
		}
		VentanaJueguito.cargarTexturas();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JPanel panelControl = new JPanel();
		contentPane.add(panelControl);
		GridBagLayout gbl_panelControl = new GridBagLayout();
		gbl_panelControl.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelControl.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panelControl.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelControl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelControl.setLayout(gbl_panelControl);
		
		JButton btnNewButton = new JButton("Agregar ficha");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onAgregarFichaClick();
			}
		});
		
		txtNombreDelMazo = new JTextField();
		txtNombreDelMazo.setText("Nombre del mazo");
		GridBagConstraints gbc_txtNombreDelMazo = new GridBagConstraints();
		gbc_txtNombreDelMazo.insets = new Insets(0, 0, 5, 5);
		gbc_txtNombreDelMazo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNombreDelMazo.gridx = 3;
		gbc_txtNombreDelMazo.gridy = 0;
		panelControl.add(txtNombreDelMazo, gbc_txtNombreDelMazo);
		txtNombreDelMazo.setColumns(10);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 1;
		panelControl.add(btnNewButton, gbc_btnNewButton);
		
		tiposFicha = new JComboBox<String>();
		tiposFicha.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				onTipoSeleccionado();
			}
		});
		tiposFicha.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Campo", "Bosque", "Pradera", "Oasis", "Agua", "Mina" }));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 2;
		panelControl.add(tiposFicha, gbc_comboBox);
		
		panelPreviewCarta = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridheight = 3;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 6;
		gbc_panel.gridy = 1;
		panelPreviewCarta.setLayout(null);
		panelControl.add(panelPreviewCarta, gbc_panel);
		
		cantidadCoronas = new JComboBox<Integer>();
		cantidadCoronas.setModel(new DefaultComboBoxModel<Integer>(
				new Integer[] { 0, 1, 2, 3 }));
		cantidadCoronas.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				onCoronasSeleccionadas();
			}
		});
		
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 3;
		gbc_comboBox_1.gridy = 3;
		panelControl.add(cantidadCoronas, gbc_comboBox_1);
		
		modelos = new JComboBox<String>();
		modelos.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				onModeloSeleccionado();
			}
		});
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_2.gridx = 3;
		gbc_comboBox_2.gridy = 4;
		panelControl.add(modelos, gbc_comboBox_2);
		
		panelPreviewMazo = new JPanel();
		panelPreviewMazo.setLayout(null);
		GridBagConstraints gbc_panelPreviewMazo = new GridBagConstraints();
		gbc_panelPreviewMazo.gridwidth = 7;
		gbc_panelPreviewMazo.fill = GridBagConstraints.BOTH;
		gbc_panelPreviewMazo.gridx = 0;
		gbc_panelPreviewMazo.gridy = 5;
		panelControl.add(panelPreviewMazo, gbc_panelPreviewMazo);
	}

	protected void onCoronasSeleccionadas() {
		onModeloSeleccionado();
		
	}
	
	protected void onModeloSeleccionado() {

		panelPreviewCarta.removeAll();
		String auxIdFicha = ((String)modelos.getSelectedItem());
		
		if(auxIdFicha == null)
			return;
		
		
		int idFicha = Integer.valueOf(auxIdFicha.substring(auxIdFicha.length()-2).trim())+2;
		
		Ficha f = new Ficha((String)tiposFicha.getSelectedItem(),cantidadCoronas.getSelectedIndex(),0,0,idFicha,null);
		System.out.println("idFicha: " + f.getId() + " cantidadCoronas: " + f.getCantCoronas());
		
		PanelFicha pF = new PanelFicha(f,0,0,1);
		if(fichasActuales[0] == null)
			pF.setBounds(0,0,80,80);
		else {
			PanelFicha pF2 = new PanelFicha(fichasActuales[0],0,0,1);
			pF2.setBounds(0,0,80,80);
			pF.setBounds(80,0,80,80);
			panelPreviewCarta.add(pF2);
		}
		panelPreviewCarta.add(pF);
		invalidate();
		validate();
		repaint();
		
	}

	protected void onTipoSeleccionado() {
		modelos.removeAllItems();
		for (Integer numeroModelo: textureMap.get((String)tiposFicha.getSelectedItem())) {
			modelos.addItem("Modelo: " + tiposFicha.getSelectedItem() + " " +numeroModelo);
		}
		//modelos.setSelectedIndex(1);
	}
	
	private Carta cartaActual;
	private Ficha[] fichasActuales = new Ficha[2];
	private Mazo mazo = new Mazo(48);
	private int idCarta;
	private JTextField txtNombreDelMazo;
	
	protected void onAgregarFichaClick() {
		String auxIdFicha = ((String)modelos.getSelectedItem());
		int idFicha = Integer.valueOf(auxIdFicha.substring(auxIdFicha.length()-2).trim());
		String tipo = (String)tiposFicha.getSelectedItem();
		if(fichasActuales[0] == null) {
			fichasActuales[0] = new Ficha(tipo, (int)cantidadCoronas.getSelectedItem(),0,0,idFicha+2, cartaActual);
		}else {
			fichasActuales[1] = new Ficha(tipo, (int)cantidadCoronas.getSelectedItem(),0,0,idFicha+2, cartaActual);
			//cartaActual = new Carta(fichasActuales[0].getId()/2,fichasActuales[0].getTipo(),fichasActuales[0].getCantCoronas(),fichasActuales[1].getTipo(),fichasActuales[1].getCantCoronas());
			cartaActual = new Carta(idCarta++,fichasActuales[0],fichasActuales[1]);
			mazo.agregarCarta(cartaActual);
			if(mazo.getCartas().size() == 48) {
				PrintWriter pW = null;
				try {
					pW = new PrintWriter(new File("./assets/"+txtNombreDelMazo.getText() + ".txt"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pW.print(mazo.toString());
				pW.close();
				this.dispose();
				return;
			}
			fichasActuales[0] = null;
			fichasActuales[1] = null;
			cartaActual = null;

			visualizarMazo();
		}
		System.out.println(mazo.getCartas().size());
	}
	protected void visualizarMazo() {
		
		
		Ficha f = null;
		PanelFicha pF = null;
		VentanaJueguito.cargarTexturas();
		List<Carta> cartas = mazo.getCartas();
		int i = 0;
		panelPreviewMazo.removeAll();
		for (Carta carta : cartas) {
			Ficha[] fichas = carta.getFichas();
			System.out.println(fichas[0].getId());
			pF = new PanelFicha(fichas[0],0,0,1);
			pF.setBounds(i%16*PanelFicha.LARGO_FICHA,(i/16)*PanelFicha.ALTO_FICHA,PanelFicha.LARGO_FICHA,PanelFicha.ALTO_FICHA);
			panelPreviewMazo.add(pF);
			i++;
			pF = new PanelFicha(fichas[1],0,0,1);
			pF.setBounds(i%16*PanelFicha.LARGO_FICHA,(i/16)*PanelFicha.ALTO_FICHA,PanelFicha.LARGO_FICHA,PanelFicha.ALTO_FICHA);
			i++;
			panelPreviewMazo.add(pF);
			
		}
		panelPreviewMazo.repaint();
		
	}
}
