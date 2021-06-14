package SwingApp;

import javax.swing.JPanel;

import reyes.Jugador;

public class PanelJugador extends JPanel {
	private PanelTablero pTablero;
	Jugador jugador;

	public PanelJugador(Jugador jugador,int tamTablero){
		setLayout(null);
		this.jugador=jugador;
		pTablero=new PanelTablero(jugador.getTablero(),tamTablero);
		pTablero.setBounds(0,0,tamTablero,tamTablero);
		this.add(pTablero);
	}
	

	public void actualizarTablero() {
		pTablero.reCrearTablero(jugador.getNombre());
	}

	


	public void pintarFicha(int i,int j) {
		pTablero.pintarFicha(i,j);
	}


}
