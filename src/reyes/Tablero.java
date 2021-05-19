package reyes;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
	private Ficha[][] tablero;
	private int tamTablero;
	public Tablero(int tamTablero) {
		this.tamTablero = tamTablero;
		this.tablero = new Ficha[tamTablero * 2 - 1][tamTablero * 2 - 1];
		this.tablero[4][4] = new Ficha("Castillo",0,4,4);
	}

	public Ficha[][] getTablero() {
		return tablero;
	}

	public int puntajeTotal() {
		int acum = 0;
		int contRegiones = 0;
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++) {
				int puntajeParcial = contarPuntajeParcial(i, j);
				acum += puntajeParcial;
				if (puntajeParcial > 0) {
					contRegiones++;
					String tipo = tablero[i][j].getTipo();
					System.out.println(contRegiones + "-" + tipo + "=" + puntajeParcial + " puntos.");
				}
			}
		}
		System.out.println("PUNTAJE TOTAL:" + acum);
		return acum;
	}

	private int contarPuntajeParcial(int x, int y) {
		Ficha ficha = tablero[x][y];

		if (ficha == null)
			return 0;

		List<Integer> acumTotal = new ArrayList<Integer>(2);

		acumTotal.add(0);
		acumTotal.add(0);

		puntajeRecursivo(ficha.getTipo(), x, y, acumTotal);

		return acumTotal.get(0) * acumTotal.get(1);
	}

	private void puntajeRecursivo(String tipo, int x, int y, List<Integer> acumTotal) {
		if (!(x >= 0 && x < tablero.length && y >= 0 && y < tablero[x].length))
			return;
		Ficha ficha = tablero[x][y];

		if (ficha == null)
			return;

		if (!ficha.isPuntajeContado()) {
			if (ficha.getTipo().compareTo(tipo) == 0) {
				ficha.setPuntajeContado(true);
				int acumPuntos = acumTotal.get(0) + 1;
				acumTotal.set(0, acumPuntos);
				int cantCoronas = acumTotal.get(1) + ficha.getCantCoronas();
				acumTotal.set(1, cantCoronas);
				puntajeRecursivo(tipo, x + 1, y, acumTotal);
				puntajeRecursivo(tipo, x, y + 1, acumTotal);
				puntajeRecursivo(tipo, x - 1, y, acumTotal);
				puntajeRecursivo(tipo, x, y - 1, acumTotal);
			}
		} else
			return;

	}
	/*
	 * Las coordenadas x,y ya vienen con centro relativo al castillo del jugador.
	 */
	public boolean ponerCarta(Carta carta, int x, int y) {
		Ficha[] fichas = carta.getFichas();
		int filaAbs  	= tablero.length/2 - y;  //este eje esta invertido
		int columnaAbs  = tablero.length/2 + x;
		carta.moverCarta(filaAbs,columnaAbs);
		//mostrarTablero();
		
		
		int f1X = fichas[0].getX();
		int f1Y = fichas[0].getY();
		int f2X = fichas[1].getX();
		int f2Y = fichas[1].getY();
		//System.out.println(f1X + "," + f1Y + ";" + f2X + "," + f2Y);
		if(f1X >= tablero.length || f1X < 0 ||
		   f1Y >= tablero.length || f1Y < 0 ||
		   f2X >= tablero.length || f2X < 0 ||
		   f2Y >= tablero.length || f2Y < 0) {
			carta.moverCarta(-filaAbs,-columnaAbs);
			return false;
		}
		// Si ya hay alguna "ficha" colocada en la posicion de la carta entonces
		// devuelvo false
		if (tablero[f1X][f1Y] != null || tablero[f2X][f2Y] != null) {
			carta.moverCarta(-filaAbs,-columnaAbs);
			return false;
		}
		// Si no hay tipos adyacentes compatibles, no se puede poner la carta
		if (!tipoAdyacenteCompatible(carta)) {
			carta.moverCarta(-filaAbs,-columnaAbs);
			return false;
		}
		
		//TODO COMPROBAR LIMITE DE 5X5

		tablero[f1X][f1Y] = fichas[0];
		tablero[f2X][f2Y] = fichas[1];

		return true;
	}

	private boolean tipoAdyacenteCompatible(Carta carta) {
		Ficha[] fichas = carta.getFichas();
		int f1X = fichas[0].getX();
		int f1Y = fichas[0].getY();
		String f1T = fichas[0].getTipo();
		int f2X = fichas[1].getX();
		int f2Y = fichas[1].getY();
		String f2T = fichas[1].getTipo();

		Ficha fComparacion = f1Y == tablero.length-1 ? null : tablero[f1X][f1Y + 1];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f1X == tablero.length-1 ? null : tablero[f1X + 1][f1Y];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f1Y == 0 ? null : tablero[f1X][f1Y - 1];
		if (compararFichas(f1T, fComparacion))
			return true;
		
		fComparacion = f1X == 0 ? null : tablero[f1X - 1][f1Y];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f2Y == tablero.length-1 ? null : tablero[f2X][f2Y + 1];
		if (compararFichas(f2T, fComparacion))
			return true;

		fComparacion = f2X == tablero.length-1 ? null : tablero[f2X + 1][f2Y];
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
		if(fComparacion == null)
			return false;
		String fTipo = fComparacion.getTipo();
		if (fTipo.compareTo("Castillo") == 0 || fTipo.compareTo(f1T) == 0) {
			return true;
		} else
			return false;
	}
	
	public int getTamanio() {
		return tamTablero;
	}
	@Override
	public String toString() {
		String ret = "";
		for (Ficha[] fichas : tablero) {
			for (Ficha ficha : fichas) {
				if(ficha != null)
					ret+=String.format("%8s|",ficha.getTipo());
				else
					ret+=String.format("%8s|"," ");
			}
			ret+="\n";
		}
		return ret;
	}
}
