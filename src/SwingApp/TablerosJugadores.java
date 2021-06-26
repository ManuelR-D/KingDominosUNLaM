package SwingApp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import reyes.Jugador;

public class TablerosJugadores extends JPanel {

	private static final long serialVersionUID = -5711758328587102246L;
	int tamTablero;
	int tamTableros = VentanaJueguito.TAM_TABLEROS;
	List<PanelJugador> tableros;
	int[][] matrizCoordenadas;// Tiene 2 coordenadas, x del tablero e y del tablero;
	int ajusteX;
	int ajusteY;

	public TablerosJugadores(List<Jugador> jugadores) {
		setLayout(null);
		tableros = new ArrayList<PanelJugador>(jugadores.size());
		tamTablero = tamTableros / 2;
		matrizCoordenadas = new int[jugadores.size()][2];
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // obtiene la resolucion de la pantalla
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		// las coordenadas x e y del tablero son respecto al panel padre, entonces le
		// agrego la x e y reales de la pantalla
		// estos calculos son posibles ya que la ventana esta centrada y no se puede
		// desplazar
		ajusteX = (int) (width / 2 - VentanaJueguito.LARGO_VENTANA / 2);
		ajusteY = (int) (height / 2 - VentanaJueguito.ALTO_VENTANA / 2);
		posicionarTableros(jugadores);
	}

	private void posicionarTableros(List<Jugador> jugadores) {
		int cantJugadores = jugadores.size();
		int x = 0, y = 0;
		for (int i = 0; i < jugadores.size(); i++) {
			PanelJugador panelJugador = new PanelJugador(jugadores.get(i), tamTablero,i);
			tableros.add(panelJugador);
			switch (i) {
			case 0:
				x = 0;
				y = cantJugadores != 2 ? 0 : tamTablero / 2;
				break;
			case 1:
				x = tamTablero;
				y = cantJugadores != 2 ? 0 : tamTablero / 2;
				break;
			case 2:
				y = tamTablero;
				x = cantJugadores != 3 ? 0 : tamTablero / 2;
				break;
			case 3:
				x = tamTablero;
				y = tamTablero;
				break;

			default:
				break;
			}


			matrizCoordenadas[i][0] = x + ajusteX;
			matrizCoordenadas[i][1] = y + ajusteY;
			panelJugador.setBounds(x, y, tamTablero, tamTablero);
			this.add(panelJugador);
		}
	}

	public synchronized int[] obtenerInputCoordenadas() {
		int turno=VentanaJueguito.getTurnoJugador();
		int[] coordenadasElegidas = new int[2];
		while (VentanaJueguito.coordenadasElegidas[0] == 0 && VentanaJueguito.coordenadasElegidas[1] == 0) {
			try {
				VentanaJueguito.getLatchCartaElegida().await();
				int xMouse = VentanaJueguito.coordenadasElegidas[2];
				int yMouse = VentanaJueguito.coordenadasElegidas[3];
				int xTablero = matrizCoordenadas[turno][0];
				int yTablero = matrizCoordenadas[turno][1];
				if (!((xMouse >= xTablero && xMouse <= xTablero + tamTablero)
						&& (yMouse >= yTablero && yMouse <= yTablero + tamTablero))) {
					VentanaJueguito.coordenadasElegidas[0] = 0;
					VentanaJueguito.coordenadasElegidas[1] = 0;
					JOptionPane.showMessageDialog(this, "Tablero incorrecto", "Movimiento no permitido",
							JOptionPane.ERROR_MESSAGE);
					VentanaJueguito.setLatchCartaElegida(new CountDownLatch(1));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Error obteniendo coordenadas, clase TablerosJugador");
			}
		}
		coordenadasElegidas[0] = VentanaJueguito.coordenadasElegidas[0];
		coordenadasElegidas[1] = VentanaJueguito.coordenadasElegidas[1];
		VentanaJueguito.setLatchCartaElegida(new CountDownLatch(1));
		VentanaJueguito.coordenadasElegidas[0] = 0;
		VentanaJueguito.coordenadasElegidas[1] = 0;
		Sonido.playPonerCarta();
		return coordenadasElegidas;
	}

	public void actualizarTableros() {

		long tiempoInicial = System.currentTimeMillis();
		for (PanelJugador tableroIndividual : tableros) {
			tableroIndividual.actualizarTablero();
		}
		System.out.println("Render tableros: " + (System.currentTimeMillis() - tiempoInicial));
	}

	public void actualizarTablero(int indice, int fila, int columna) {
		tableros.get(indice).actualizarTablero(fila, columna);
	}

	public void pintarFicha(int fila, int columna, int indice, int acumPuntos, int cantCoronas) {
		tableros.get(indice).pintarFicha(fila, columna, acumPuntos, cantCoronas, indice);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color cAnterior = g.getColor();
		g.setColor(new Color(0xAB7632));
		g.fillRect(0, 0, tamTableros, tamTableros);
		g.setColor(cAnterior);
		int turno=VentanaJueguito.getTurnoJugador();
		if(turno!=-1) {
			int x=matrizCoordenadas[turno][0];
			int y=matrizCoordenadas[turno][1];
			g.drawRect(x-ajusteX, y-ajusteY, tamTablero, tamTablero);			
		}
	}
}
