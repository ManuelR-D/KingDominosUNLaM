package SwingApp;

import java.awt.Label;
import java.io.IOException;

import javax.swing.JPanel;

import reyes.Jugador;

public class PanelJugador extends JPanel {
	private PanelTablero t;
	Jugador j;
	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public PanelJugador(Jugador j) throws IOException {
		setLayout(null);
		this.j = j;
		Label nombre = new Label(j.nombre);
		nombre.setBounds(50, 0, 100, 25);
		this.add(nombre);
		PanelTablero tablero = new PanelTablero(j.tablero);
		this.t = tablero;
		t.setBounds(0,0,PanelFicha.LARGO_CARTA*j.tablero.getTamanio(),
							   PanelFicha.ALTO_FICHA*j.tablero.getTamanio());
		this.add(t);
	}
	
	
	public void actualizarPanel() throws IOException {
		t.actualizarTablero();
	}
	

}
