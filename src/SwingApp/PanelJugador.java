package SwingApp;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import reyes.Jugador;
import reyes.Tablero;

public class PanelJugador extends JPanel {
	private PanelTablero t;
	Jugador j;

	public PanelJugador(Jugador j){
//		setLayout(null);
		this.j = j;
//		Label nombre = new Label(j.getNombre());
//		nombre.setBounds(50, 0, 100, 25);
//		this.add(nombre);
		PanelTablero tablero = new PanelTablero(j.tablero);
		tablero.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Tablero tableroJugador=j.tablero;
		int filas=(tableroJugador.getxMax()+2)-(tableroJugador.getxMin()-2);
		int columnas=(tableroJugador.getyMax()+2)-(tableroJugador.getyMin()-2);
		tablero.setLayout(new GridLayout(filas+1,columnas+1));
		this.t = tablero;
		t.setBounds(0,0,PanelFicha.LARGO_FICHA*j.tablero.getTamanio(),
							   PanelFicha.ALTO_FICHA*j.tablero.getTamanio());
		this.add(t);
	}
	
	
	public void actualizarPanel(){
		t.actualizarTablero();
	}
	

}
