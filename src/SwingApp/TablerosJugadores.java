package SwingApp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import reyes.Jugador;

public class TablerosJugadores extends JPanel{
	List<PanelJugador> tableros;
	
	
	
	public TablerosJugadores(List<Jugador> jugadores) {
		this.tableros = new ArrayList<PanelJugador>(jugadores.size());
//		this.setLayout(new GridLayout(2,2));
		for (Jugador jugador : jugadores) {
			PanelJugador panel;
			panel = new PanelJugador(jugador);
			panel.setBorder(BorderFactory.createLineBorder(Color.black));
			panel.setLayout(new FlowLayout());
			this.add(panel);
			this.tableros.add(panel);
		}
		
	}
	
	public void actualizarTableros() {
		for (PanelJugador pJ : tableros) {
			pJ.actualizarPanel();
		}
		this.repaint();
	}
	
	public synchronized int[] obtenerInputCoordenadas(Jugador jugador) {
		int[] coordenadasElegidas = new int[2];
		for (PanelJugador panelJugador : tableros) {
			if (panelJugador.j.equals(jugador)) {
				while (VentanaJueguito.coordenadasElegidas[0] == 0 && VentanaJueguito.coordenadasElegidas[1] == 0) {
					try {
						VentanaJueguito.getLatchCartaElegida().await();
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("Error obteniendo coordenadas, clase TablerosJugador");
					}
				}
			}
		}
		coordenadasElegidas[0] = VentanaJueguito.coordenadasElegidas[0];
		coordenadasElegidas[1] = VentanaJueguito.coordenadasElegidas[1];
		VentanaJueguito.coordenadasElegidas[0] = 0;
		VentanaJueguito.coordenadasElegidas[1] = 0;
		VentanaJueguito.setLatchCartaElegida(new CountDownLatch(1));
		return coordenadasElegidas;
	}
}
