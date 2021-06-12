package SwingApp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import reyes.Jugador;

public class TablerosJugadores extends JPanel{
	int tamTablero;
	int tamTableros=VentanaJueguito.TAM_TABLEROS;
	List<PanelJugador> tableros;

	public TablerosJugadores(List<Jugador> jugadores) {
		setLayout(null);
		tableros=new ArrayList<PanelJugador>(jugadores.size());
		tamTablero=tamTableros/2;
		for(int i=0;i<jugadores.size();i++) {
			PanelJugador panelJugador=new PanelJugador(jugadores.get(i),tamTablero);
			tableros.add(panelJugador);
			int x = 0, y = 0;
			switch (i) {
			case 0:
				x = 0;
				y = 0;
				break;
			case 1:
				x = tamTablero;
				y = 0;
				break;
			case 2:
				x = 0;
				y = tamTablero;
				break;
			case 3:
				x = tamTablero;
				y = tamTablero;
				break;

			default:
				break;
			}
			panelJugador.setBounds(x,y,tamTablero,tamTablero);
			this.add(panelJugador);
		}
	}
	
	public synchronized int[] obtenerInputCoordenadas(Jugador jugador) {
		int[] coordenadasElegidas = new int[2];
		for (PanelJugador panelJugador : tableros) {
			if (panelJugador.jugador.equals(jugador)) {
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
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d=(Graphics2D) g;
		tamTableros=VentanaJueguito.TAM_TABLEROS;
		g2d.fillRect(0, 0, tamTableros, tamTableros);
	}

	public void actualizarTableros() {
		for(PanelJugador tableroIndividual:tableros) {
			tableroIndividual.actualizarTablero();
		}
	}
}
