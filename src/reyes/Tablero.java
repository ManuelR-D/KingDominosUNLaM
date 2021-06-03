package reyes;

import java.util.ArrayList;
import java.util.List;

import SwingApp.VentanaJueguito;

public class Tablero {
	protected Ficha[][] tablero;
	protected int tamTablero;
	private int cantTerrenoColocado = 0;
	protected int centro;
	private int xMin, xMax, yMin, yMax;

	public Tablero(int tamTablero) {
		this.tamTablero = tamTablero;
		centro = tamTablero - 1;
		this.tablero = new Ficha[(tamTablero * 2) - 1][(tamTablero * 2) - 1];
		this.tablero[centro][centro] = new Ficha("Castillo", 0, centro, centro,-1, null);
		xMin = xMax = yMin = yMax = centro;
	}

	public Ficha[][] getTablero() {
		return tablero;
	}

	public int puntajeTotal(boolean mostrarRegiones) {
		int acumPuntos = 0;
		int contRegiones = 0;
		for (int i = xMin; i <= xMax; i++) {
			for (int j = yMin; j <= yMax; j++) {

				int puntajeParcialPorRegion = contarPuntajeParcial(i, j);
				acumPuntos += puntajeParcialPorRegion;
				if (mostrarRegiones && puntajeParcialPorRegion > 0) {
					contRegiones++;
					String tipo = tablero[i][j].getTipo();
					System.out.println(contRegiones + "-" + tipo + "=" + puntajeParcialPorRegion + " puntos.\n");
				}
			}
		}
		if (mostrarRegiones) {
			System.out.println("PUNTAJE TOTAL:" + acumPuntos);
		}
		return acumPuntos;
	}

	private int contarPuntajeParcial(int x, int y) {
		Ficha ficha = tablero[x][y];

		if (ficha == null)
			return 0;

		List<Integer> puntosYCoronas = new ArrayList<Integer>(2);
		// En la primer posicion del ArrayList se guardan los puntos por region
		// En la segunda posicion se guarda la cantidad de coronas por region

		puntosYCoronas.add(0);
		puntosYCoronas.add(0);

		puntajeRecursivo(ficha.getTipo(), x, y, puntosYCoronas);

		return puntosYCoronas.get(0) * puntosYCoronas.get(1);
	}

	private void puntajeRecursivo(String tipoRegion, int x, int y, List<Integer> puntosYCoronas) {

		if (!(x >= 0 && x < tablero.length && y >= 0 && y < tablero[x].length))
			return;

		Ficha fichaActual = tablero[x][y];

		if (fichaActual == null)
			return;

		if (!fichaActual.isPuntajeContado()) {
			if (fichaActual.getTipo().equals(tipoRegion)) {
				fichaActual.setPuntajeContado(true);
				int acumPuntos = puntosYCoronas.get(0) + 1;
				puntosYCoronas.set(0, acumPuntos);
				int cantCoronas = puntosYCoronas.get(1) + fichaActual.getCantCoronas();
				puntosYCoronas.set(1, cantCoronas);
				puntajeRecursivo(tipoRegion, x + 1, y, puntosYCoronas);
				puntajeRecursivo(tipoRegion, x, y + 1, puntosYCoronas);
				puntajeRecursivo(tipoRegion, x - 1, y, puntosYCoronas);
				puntajeRecursivo(tipoRegion, x, y - 1, puntosYCoronas);
			}
		} else
			return;

	}

	/*
	 * Las coordenadas x,y ya vienen con centro relativo al castillo del jugador.
	 */
	public boolean ponerCarta(Carta carta, int columnaRelativa, int filaRelativa, boolean mostrarMensaje) {
		Ficha[] fichas = carta.getFichas();
		carta.moverCarta(centro, centro);
		carta.moverCarta(-filaRelativa, columnaRelativa);

		if (esPosibleInsertar(carta, mostrarMensaje)) {

			int f1X = fichas[0].getX();
			int f1Y = fichas[0].getY();
			int f2X = fichas[1].getX();
			int f2Y = fichas[1].getY();
			actualizarLimites(f1X, f1Y, f2X, f2Y, tamTablero);
			tablero[f1X][f1Y] = fichas[0];
			tablero[f2X][f2Y] = fichas[1];
			cantTerrenoColocado++;
			return true;
		} else {
			carta.setDefault();
			return false;
		}
	}

	public boolean ponerCarta(Carta carta, int columna, int fila, boolean mostrarMensaje,VentanaJueguito ventana) {
		Ficha[] fichas = carta.getFichas();
		carta.moverCarta(fila, columna);

		if (esPosibleInsertar(carta, mostrarMensaje,ventana)) {

			int f1X = fichas[0].getX();
			int f1Y = fichas[0].getY();
			int f2X = fichas[1].getX();
			int f2Y = fichas[1].getY();
			actualizarLimites(f1X, f1Y, f2X, f2Y, tamTablero);
			tablero[f1X][f1Y] = fichas[0];
			tablero[f2X][f2Y] = fichas[1];
			cantTerrenoColocado++;
			return true;
		} else {
			carta.setDefault();
			return false;
		}
	}


	protected boolean esPosibleInsertar(Carta carta, boolean mostrarMensaje) {
		Ficha[] fichas = carta.getFichas();
		int f1X = fichas[0].getX();
		int f1Y = fichas[0].getY();
		int f2X = fichas[1].getX();
		int f2Y = fichas[1].getY();

		if (f1X >= tablero.length || f1X < 0 || f1Y >= tablero.length || f1Y < 0 || f2X >= tablero.length || f2X < 0
				|| f2Y >= tablero.length || f2Y < 0) {
			return false;
		}
		// Si ya hay alguna "ficha" colocada en la posicion de la carta entonces
		// devuelvo false
		if (tablero[f1X][f1Y] != null || tablero[f2X][f2Y] != null) {
			if (mostrarMensaje) {
				System.out.println("ERROR- YA HAY UNA CARTA EN ESA POSICION");
			}
			return false;
		}
		// Si no hay tipos adyacentes compatibles, no se puede poner la carta
		if (!tipoAdyacenteCompatible(carta)) {
			if (mostrarMensaje) {
				System.out.println("ERROR- NO HAY TIPOS ADYACENTES COMPATIBLES");
			}
			return false;
		}

		// comprobamos que no salga del limite
		if (!comprobarLimiteSinModificar(f1X, f1Y, f2X, f2Y, tamTablero)) {
			if (mostrarMensaje) {
				System.out.println("ERROR- SE EXCEDE EL LIMITE DE CONSTRUCCION");
			}
			return false;
		}

		return true;
	}
	/*
	 * Sobrecarga del metodo para que reciba la ventana como argumento
	 */
	protected boolean esPosibleInsertar(Carta carta, boolean mostrarMensaje, VentanaJueguito ventana) {
		Ficha[] fichas = carta.getFichas();
		int f1X = fichas[0].getX();
		int f1Y = fichas[0].getY();
		int f2X = fichas[1].getX();
		int f2Y = fichas[1].getY();

		if (f1X >= tablero.length || f1X < 0 || f1Y >= tablero.length || f1Y < 0 || f2X >= tablero.length || f2X < 0
				|| f2Y >= tablero.length || f2Y < 0) {
			return false;
		}
		// Si ya hay alguna "ficha" colocada en la posicion de la carta entonces
		// devuelvo false
		if (tablero[f1X][f1Y] != null || tablero[f2X][f2Y] != null) {
			if (mostrarMensaje) {
				ventana.mostrarMensaje("ERROR- YA HAY UNA CARTA EN ESA POSICION");
			}
			return false;
		}
		// Si no hay tipos adyacentes compatibles, no se puede poner la carta
		if (!tipoAdyacenteCompatible(carta)) {
			if (mostrarMensaje) {
				ventana.mostrarMensaje("ERROR- NO HAY TIPOS ADYACENTES COMPATIBLES");
			}
			return false;
		}

		// comprobamos que no salga del limite
		if (!comprobarLimiteSinModificar(f1X, f1Y, f2X, f2Y, tamTablero)) {
			if (mostrarMensaje) {
				ventana.mostrarMensaje("ERROR- SE EXCEDE EL LIMITE DE CONSTRUCCION");
			}
			return false;
		}

		return true;
	}

	private boolean comprobarLimiteSinModificar(int f1X, int f1Y, int f2X, int f2Y, int limite) {
		int xMinAux = xMin;
		int xMaxAux = xMax;
		int yMinAux = yMin;
		int yMaxAux = yMax;

		if (f1X < xMinAux)
			xMinAux = f1X;

		if (f1X > xMaxAux)
			xMaxAux = f1X;

		if (f1Y < yMinAux)
			yMinAux = f1Y;

		if (f1Y > yMaxAux)
			yMaxAux = f1Y;

		if (f2X < xMinAux)
			xMinAux = f2X;

		if (f2X > xMaxAux)
			xMaxAux = f2X;

		if (f2Y < yMinAux)
			yMinAux = f2Y;

		if (f2Y > yMaxAux)
			yMaxAux = f2Y;

		if (xMaxAux - xMinAux >= limite || yMaxAux - yMinAux >= limite) {
			return false;
		} else {
			return true;
		}
	}

	protected boolean actualizarLimites(int f1X, int f1Y, int f2X, int f2Y, int limite) {
		int xMinAux = xMin;
		int xMaxAux = xMax;
		int yMinAux = yMin;
		int yMaxAux = yMax;

		if (f1X < xMinAux)
			xMinAux = f1X;

		if (f1X > xMaxAux)
			xMaxAux = f1X;

		if (f1Y < yMinAux)
			yMinAux = f1Y;

		if (f1Y > yMaxAux)
			yMaxAux = f1Y;

		if (f2X < xMinAux)
			xMinAux = f2X;

		if (f2X > xMaxAux)
			xMaxAux = f2X;

		if (f2Y < yMinAux)
			yMinAux = f2Y;

		if (f2Y > yMaxAux)
			yMaxAux = f2Y;

		if (xMaxAux - xMinAux >= limite || yMaxAux - yMinAux >= limite) {
			return false;
		} else {
			xMin = xMinAux;
			xMax = xMaxAux;
			yMin = yMinAux;
			yMax = yMaxAux;
			return true;
		}
	}

	protected boolean tipoAdyacenteCompatible(Carta carta) {
		Ficha[] fichas = carta.getFichas();
		int f1X = fichas[0].getX();
		int f1Y = fichas[0].getY();
		String f1T = fichas[0].getTipo();
		int f2X = fichas[1].getX();
		int f2Y = fichas[1].getY();
		String f2T = fichas[1].getTipo();

		Ficha fComparacion = f1Y == tablero.length - 1 ? null : tablero[f1X][f1Y + 1];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f1X == tablero.length - 1 ? null : tablero[f1X + 1][f1Y];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f1Y == 0 ? null : tablero[f1X][f1Y - 1];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f1X == 0 ? null : tablero[f1X - 1][f1Y];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f2Y == tablero.length - 1 ? null : tablero[f2X][f2Y + 1];
		if (compararFichas(f2T, fComparacion))
			return true;

		fComparacion = f2X == tablero.length - 1 ? null : tablero[f2X + 1][f2Y];
		if (compararFichas(f2T, fComparacion))
			return true;

		fComparacion = f2Y == 0 ? null : tablero[f2X][f2Y - 1];
		if (compararFichas(f2T, fComparacion))
			return true;

		fComparacion = f2X == 0 ? null : tablero[f2X - 1][f2Y];
		if (compararFichas(f2T, fComparacion))
			return true;

		return false;
	}

	protected boolean compararFichas(String f1T, Ficha fComparacion) {
		if (fComparacion == null)
			return false;
		String fTipo = fComparacion.getTipo();
		return (fTipo.equals("Castillo") || fTipo.equals(f1T));
	}

	public int getTamanio() {
		return tamTablero;
	}

	@Override
	public String toString() {
		String ret = "";
		for (int i = xMin; i <= xMax; i++) {
			for (int j = yMin; j <= yMax; j++) {
				Ficha ficha = tablero[i][j];
				if (ficha != null)
					ret += String.format("%8s/%s/%s|", ficha.getTipo(), ficha.getCantCoronas(),ficha.getId());
				else
					ret += String.format("%10s|", " ");
			}
			ret += "\n";
		}
		return ret;
	}

	public boolean esPosibleInsertarEnTodoElTablero(Carta carta) {
		int yMinAux = yMin - 1;
		int xMinAux = xMin - 1;
		boolean noMostrarMensaje = false;

		carta.moverCarta(xMin - 1, yMin - 1);

		for (int i = xMin - 1; i <= xMax + 1; i++) {
			for (int j = yMin - 1; j <= yMax + 1; j++) {
				for (int r = 0; r < 4; r++) {
					if (esPosibleInsertar(carta, noMostrarMensaje)) {
						carta.setDefault();
						return true;
					} else {
						carta.rotarCarta();
					}

				}
				carta.moverCarta(0, 1);
			}
			carta.setDefault();
			carta.moverCarta(++xMinAux, yMinAux);

		}
		carta.setDefault();
		return false;
	}

	public void quitarCarta(Carta cartaElegida) {

		Ficha f1 = cartaElegida.getFichas()[0];
		Ficha f2 = cartaElegida.getFichas()[1];

		tablero[f1.getX()][f1.getY()] = null;
		tablero[f2.getX()][f2.getY()] = null;
		cartaElegida.setDefault();

	}

	public int getCantTerrenoColocado() {
		return cantTerrenoColocado;
	}
	public int getxMin() {
		return xMin;
	}
	public int getxMax() {
		return xMax;
	}
	public int getyMin() {
		return yMin;
	}
	public int getyMax() {
		return yMax;
	}
}
