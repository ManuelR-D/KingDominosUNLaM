package SwingApp;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JPanel;

import reyes.Jugador;

public class TablerosJugadores extends JPanel{

	private static final long serialVersionUID = -5711758328587102246L;
	int tamTablero;
	int tamTableros=VentanaJueguito.TAM_TABLEROS;
	List<PanelJugador> tableros;

	public TablerosJugadores(List<Jugador> jugadores) {
		setLayout(null);
		tableros=new ArrayList<PanelJugador>(jugadores.size());
		tamTablero=tamTableros/2;
		posicionarTableros(jugadores);
	}

	private void posicionarTableros(List<Jugador> jugadores) {
		int cantJugadores=jugadores.size();
		int x = 0, y = 0;
		for(int i=0;i<jugadores.size();i++) {
			PanelJugador panelJugador=new PanelJugador(jugadores.get(i),tamTablero);
			tableros.add(panelJugador);
			switch (i) {
			case 0:
				x = 0;
				y = cantJugadores!=2 ? 0 : tamTablero/2;
				break;
			case 1:
				x = tamTablero;
				y = cantJugadores!=2 ? 0 : tamTablero/2;
				break;
			case 2:
				y = tamTablero;					
				x = cantJugadores!=3? 0 : tamTablero/2;
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
		VentanaJueguito.setLatchCartaElegida(new CountDownLatch(1));
		VentanaJueguito.coordenadasElegidas[0] = 0;
		VentanaJueguito.coordenadasElegidas[1] = 0;
		System.out.println("Se eligió: " + coordenadasElegidas[0] +";"+ coordenadasElegidas[1] );
		Sonido.playPonerCarta();
		return coordenadasElegidas;
	}

	public void actualizarTableros() {
		Thread[] ths = new Thread[tableros.size()];
		int i = 0;
		for(PanelJugador tableroIndividual:tableros) {
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					tableroIndividual.actualizarTablero();
				}
			});
			//Preparamos los hilos
			ths[i++] = th;
		}
		long tiempoInicial = System.currentTimeMillis();
		//Lanzamos los hilos
		for(i = 0; i < tableros.size(); ths[i++].start());
		//Esperamos que terminen
		try {
			for(i = 0; i < tableros.size(); ths[i++].join());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Render tableros: " + (System.currentTimeMillis() - tiempoInicial));
	}
	/*public void actualizarTableros() {
		for(PanelJugador tableroIndividual:tableros) {
			tableroIndividual.actualizarTablero();
		}
	}*/
	public void actualizarTablero(int indice,int fila,int columna) {
		tableros.get(indice).actualizarTablero(fila,columna);
	}

	public void pintarFicha(int fila, int columna,int indice, int acumPuntos, int cantCoronas) {
		tableros.get(indice).pintarFicha(fila,columna,acumPuntos,cantCoronas,indice);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color cAnterior=g.getColor();
		g.setColor(new Color(0xAB7632));
		g.fillRect(0, 0, tamTableros, tamTableros);
		g.setColor(cAnterior);
	}
}
