package SwingApp;

import javax.swing.JPanel;

import reyes.Jugador;

public class PanelJugador extends JPanel {

	private static final long serialVersionUID = 1226652105748390999L;
	private PanelTablero pTablero;
	Jugador jugador;

	public PanelJugador(Jugador jugador,int tamTablero){
		setLayout(null);
		this.jugador=jugador;
		pTablero=new PanelTablero(jugador.getTablero(),tamTablero);
		pTablero.setBounds(0,0,tamTablero,tamTablero);
		this.add(pTablero);
	}
	public void actualizarTablero(int fila, int columna) {
		pTablero.actualizarTablero(jugador.getNombre(),fila,columna);
	}
	public void pintarFicha(int fila,int columna, int acumPuntos, int cantCoronas, int indice) {
		pTablero.pintarFicha(fila,columna,acumPuntos,cantCoronas,indice);
	}
	public void actualizarTablero() {
		pTablero.reCrearTablero(jugador.getNombre());
	}

}
