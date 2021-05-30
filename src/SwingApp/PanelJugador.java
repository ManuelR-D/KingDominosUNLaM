package SwingApp;

import java.awt.Color;
import java.awt.Label;
import java.io.IOException;

import javax.swing.JPanel;

import reyes.Jugador;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class PanelJugador extends JPanel {

	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public PanelJugador(Jugador j) throws IOException {
		setLayout(null);
		Label nombre = new Label(j.nombre);
		nombre.setBounds(50, 0, 100, 25);
		this.add(nombre);
		PanelTablero tablero = new PanelTablero(j.tablero);
		tablero.setBounds(0,20,PanelFicha.LARGO_CARTA*j.tablero.getTamanio(),
							   PanelFicha.ALTO_FICHA*j.tablero.getTamanio());
		this.add(tablero);
	}

}
