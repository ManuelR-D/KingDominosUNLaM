package reyes;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
	private static final int LIMITE_CONSTRUCCION = 5;
	protected Ficha[][] tablero;
	private int tamTablero;
	private int cantTerrenoColocado = 0;
	private int centro;
	private int xMin, xMax, yMin, yMax;

	public Tablero(int tamTablero) {
		this.tamTablero = tamTablero;
		centro = tamTablero;
		this.tablero = new Ficha[(tamTablero*2)-1][(tamTablero*2)-1];
		this.tablero[centro][centro] = new Ficha("Castillo", 0, centro, centro);
		xMin = xMax = yMin = yMax = centro;
	}

	public Ficha[][] getTablero() {
		return tablero;
	}

	public List<String> puntajeTotal() {
		List<String> puntajesParcialesYTotal = new ArrayList<String>();
		int acumPuntos = 0;
		int contRegiones = 0;
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++) {

				int puntajeParcialPorRegion = contarPuntajeParcial(i, j);
				acumPuntos += puntajeParcialPorRegion;
				if (puntajeParcialPorRegion > 0) {
					contRegiones++;
					String tipo = tablero[i][j].getTipo();
					puntajesParcialesYTotal
							.add(contRegiones + "-" + tipo + "=" + puntajeParcialPorRegion + " puntos.\n");
				}
			}
		}
		puntajesParcialesYTotal.add("PUNTAJE TOTAL:" + acumPuntos);
		return puntajesParcialesYTotal;
	}

	private int contarPuntajeParcial(int x, int y) {
		Ficha ficha = tablero[x][y];

		if (ficha == null)
			return 0;

		List<Integer> puntosYCoronas = new ArrayList<Integer>(2);

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
	public boolean ponerCarta(Carta carta, int yRelativa, int xRelativa) {
		Ficha[] fichas = carta.getFichas();
		int xReal = centro + xRelativa;
		int yReal = centro + yRelativa; // este eje esta invertido

		carta.moverCarta(xReal, yReal);

		if (esPosibleInsertar(carta)) {
			
			int f1X = fichas[0].getX();
			int f1Y = fichas[0].getY();
			int f2X = fichas[1].getX();
			int f2Y = fichas[1].getY();
			tablero[f1X][f1Y] = fichas[0];
			tablero[f2X][f2Y] = fichas[1];
			cantTerrenoColocado++;
			return true;
		} else {
			carta.moverCarta(-xReal, -yReal);
			return false;
		}
	}

	private boolean esPosibleInsertar(Carta carta) {
		Ficha[] fichas = carta.getFichas();
		int f1X = fichas[0].getX();
		int f1Y = fichas[0].getY();
		int f2X = fichas[1].getX();
		int f2Y = fichas[1].getY();
		// System.out.println(f1X + "," + f1Y + ";" + f2X + "," + f2Y);
		if (f1X >= tablero.length || f1X < 0 || f1Y >= tablero.length || f1Y < 0 || f2X >= tablero.length || f2X < 0
				|| f2Y >= tablero.length || f2Y < 0) {
			return false;
		}
		// Si ya hay alguna "ficha" colocada en la posicion de la carta entonces
		// devuelvo false
		if (tablero[f1X][f1Y] != null || tablero[f2X][f2Y] != null) {
			return false;
		}
		// Si no hay tipos adyacentes compatibles, no se puede poner la carta
		if (!tipoAdyacenteCompatible(carta)) {
			return false;
		}

		// comprobamos que no salga del limite
		if (!comprobarLimite(f1X, f1Y, f2X, f2Y, LIMITE_CONSTRUCCION)) {
			return false;
		}

		return true;
	}

	private boolean comprobarLimite(int f1X, int f1Y, int f2X, int f2Y, int limite) {
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

	private boolean tipoAdyacenteCompatible(Carta carta) {
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

	private boolean compararFichas(String f1T, Ficha fComparacion) {
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
		for (int i=xMin;i<=xMax;i++) {
			for (int j=yMin;j<=yMax;j++) {
				Ficha ficha=tablero[i][j];
				if (ficha != null)
					ret += String.format("%8s/%s|", ficha.getTipo(), ficha.getCantCoronas());
				else
					ret += String.format("%10s|", " ");
			}
			ret += "\n";
		}
		return ret;
	}

	public boolean esPosibleInsertarEnTodoElTablero(Carta cartaElegida) {
		int x = -(tamTablero - 1);
		int y = -(tamTablero - 1);
		int rotaciones = 0;
		while (ponerCarta(cartaElegida, x, y) == false && y < (tamTablero * 2)) {
			// probamos todas las rotaciones posibles
			cartaElegida.rotarCarta();
			rotaciones++;
			if (rotaciones == 3) {
				rotaciones = 0;
				// si fallamos en todas las rotaciones, cambiamos de posicion
				if (x < (tamTablero - 1))
					x++;
				else {
					x = -(tamTablero - 1);
					y++;
				}
				cartaElegida.rotarCarta();
			}
		}
		quitarCarta(cartaElegida);
		return y < (tamTablero * 2);
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
}
