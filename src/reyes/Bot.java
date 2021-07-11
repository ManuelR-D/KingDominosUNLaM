package reyes;

import java.util.List;

import SwingApp.VentanaJueguito;

public class Bot extends Jugador {
	int puntajeMax = 0;
	int xPuntajeMax;
	int yPuntajeMax;
	int rotacionPuntajeMax;

	public Bot(String nombre, int tamTablero) {
		super(nombre, tamTablero);
		this.tablero = new TableroBot(tamTablero);
	}

	public Bot(String nombre, String contrasenia) {
		super(nombre, contrasenia);
	}

	@Override
	public void setTablero(int tamanioTablero) {
		this.tablero = new TableroBot(tamanioTablero);
	}
	@Override
	public void crearPaquete(int numeroElegido, int coordenadaX, int coordenadaY, boolean pudoInsertar, int rotacion) {
		// TODO Auto-generated method stub
		//Bots no crean paquetes.
	}
	@Override
	public int eligeCarta(List<Carta> cartasAElegir, VentanaJueguito ventana) {
		int numeroElegido = -1;
		int[] puntajeMaxYCoordenadas = new int[4];

		puntajeMaxYCoordenadas[0] = -2;// puntaje max
		puntajeMaxYCoordenadas[1] = 0;// x del puntaje max
		puntajeMaxYCoordenadas[2] = 0;// y del puntaje max
		puntajeMaxYCoordenadas[3] = 0;// rotacion del puntaje max

		for (int i = 0; i < cartasAElegir.size(); i++) {
			Carta carta = cartasAElegir.get(i);
			if (carta != null) {
				// si no la eligieron ya...
				int[] puntajeMaxYCoordenadasActual = maximoPuntajePosible(carta);
				int puntajeActual = puntajeMaxYCoordenadasActual[0];
				if (puntajeActual > puntajeMaxYCoordenadas[0]) {
					puntajeMaxYCoordenadas = puntajeMaxYCoordenadasActual;
					numeroElegido = i;
				}
				carta.setDefault();
			}
		}
		xPuntajeMax = puntajeMaxYCoordenadas[1];
		yPuntajeMax = puntajeMaxYCoordenadas[2];
		rotacionPuntajeMax = puntajeMaxYCoordenadas[3];
		return numeroElegido;
	}

	private int[] maximoPuntajePosible(Carta carta) {
		int fMin = tablero.getFMin();
		int fMax = tablero.getFMax();
		int cMin = tablero.getCMin();
		int cMax = tablero.getCMax();
		int puntajeMax = -1;
		int xPuntajeMax = 0;
		int yPuntajeMax = 0;
		int rotacion = 0;
		int[] puntajeYCoordenadas = new int[4];

		for (int y = fMin - 2; y <= fMax + 2; y++) {
			for (int x = cMin - 2; x <= cMax + 2; x++) {
				for (int i = 0; i < 4; i++) {
					if (tablero.ponerCarta(carta, x, y, false,null) == true) {
						int puntaje = tablero.puntajeTotal(false);
						if (puntaje > puntajeMax) {
							puntajeMax = puntaje;
							xPuntajeMax = x;
							yPuntajeMax = y;
							rotacion = i;
						}
						tablero.quitarCarta(carta);
						carta.setDefault();
					}
					for (int j = 0; j < i + 1; j++) {
						carta.rotarCarta();
					}
				}
			}
		}

		puntajeYCoordenadas[0] = puntajeMax;
		puntajeYCoordenadas[1] = xPuntajeMax;
		puntajeYCoordenadas[2] = yPuntajeMax;
		puntajeYCoordenadas[3] = rotacion;
		return puntajeYCoordenadas;
	}

	@Override
	public boolean insertaEnTablero(Carta cartaElegida, VentanaJueguito ventana) {
		for (int i = 0; i < rotacionPuntajeMax; i++) {
			cartaElegida.rotarCarta();
		}
		boolean pudoInsertar=tablero.ponerCarta(cartaElegida, xPuntajeMax, yPuntajeMax, false,null);
		if(!pudoInsertar) {
			reinoCompletamenteOcupado=false;
		}
		return pudoInsertar;
	}

}
