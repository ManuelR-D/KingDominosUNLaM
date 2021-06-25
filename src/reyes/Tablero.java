package reyes;

import java.util.ArrayList;
import java.util.List;
import SwingApp.PanelInformacion;
import SwingApp.VentanaJueguito;

public class Tablero {
	protected Ficha[][] tablero;
	protected int tamTablero;
	private int cantTerrenoColocado = 0;
	protected int centro;
	private int fMin, fMax, cMin, cMax;

	public Tablero(int tamTablero) {
		this.tamTablero = tamTablero;
		centro = tamTablero - 1;
		this.tablero = new Ficha[(tamTablero * 2) - 1][(tamTablero * 2) - 1];
		this.tablero[centro][centro] = new Ficha("Castillo", 0, centro, centro, -1, null);
		fMin = fMax = cMin = cMax = centro;
	}

	public void setIdCastilloCentro(int numCastillo) {
		this.tablero[centro][centro] = new Ficha("Castillo", 0, centro, centro, -numCastillo, null);
	}
	public Ficha[][] getTablero() {
		return tablero;
	}

	public int puntajeTotal(boolean mostrarRegiones) {
		int acumPuntos = 0;
		for (int i = fMin; i <= fMax; i++) {
			for (int j = cMin; j <= cMax; j++) {

				int puntajeParcialPorRegion = contarPuntajeParcial(i, j, null, 0);
				acumPuntos += puntajeParcialPorRegion;
			}
		}
		resetearContadoFichas();
		return acumPuntos;
	}

	private void resetearContadoFichas() {
		for (Ficha[] fichas : tablero) {
			for (Ficha ficha : fichas) {
				if (ficha != null) {
					ficha.setPuntajeContado(false);
				}
			}
		}
	}

	public int puntajeTotal(boolean mostrarRegiones, VentanaJueguito ventana, int indice) {
		int acumPuntos = 0;
		for (int i = fMin; i <= fMax; i++) {
			for (int j = cMin; j <= cMax; j++) {

				int puntajeParcialPorRegion = contarPuntajeParcial(i, j, ventana, indice);
				acumPuntos += puntajeParcialPorRegion;
			}
		}
		resetearContadoFichas();
		return acumPuntos;
	}

	private int contarPuntajeParcial(int fila, int columna, VentanaJueguito ventana, int indice) {
		Ficha ficha = tablero[fila][columna];

		if (ficha == null)
			return 0;
		if (ficha.getTipo().equals("Castillo"))
			return 0;
		if (ficha.isPuntajeContado())
			return 0;
		List<Integer> puntosYCoronas = new ArrayList<Integer>(2);
		// En la primer posicion del ArrayList se guardan los puntos por region
		// En la segunda posicion se guarda la cantidad de coronas por region

		puntosYCoronas.add(0);
		puntosYCoronas.add(0);

		puntajeRecursivo(ficha.getTipo(), fila, columna, puntosYCoronas, ventana, indice);

		Integer acumPuntos = puntosYCoronas.get(0);
		Integer acumCoronas = puntosYCoronas.get(1);
		if (ventana != null) {
			ventana.pintarFicha(fila, columna, indice, acumPuntos, acumCoronas);
		}
		return acumPuntos * acumCoronas;

	}

	private void puntajeRecursivo(String tipoRegion, int fila, int columna, List<Integer> puntosYCoronas,
			VentanaJueguito ventana, int indice) {

		if (!(fila >= 0 && fila < tablero.length) || !(columna >= 0 && columna < tablero.length))
			return;

		Ficha fichaActual = tablero[fila][columna];

		if (fichaActual == null)
			return;
		if (fichaActual.getTipo().equals("Castillo"))
			return;

		if (!fichaActual.isPuntajeContado()) {
			if (fichaActual.getTipo().equals(tipoRegion)) {
				fichaActual.setPuntajeContado(true);

				int acumPuntos = puntosYCoronas.get(0) + 1;
				puntosYCoronas.set(0, acumPuntos);
				int cantCoronas = puntosYCoronas.get(1) + fichaActual.getCantCoronas();
				puntosYCoronas.set(1, cantCoronas);
				puntajeRecursivo(tipoRegion, fila + 1, columna, puntosYCoronas, ventana, indice);
				puntajeRecursivo(tipoRegion, fila, columna + 1, puntosYCoronas, ventana, indice);
				puntajeRecursivo(tipoRegion, fila - 1, columna, puntosYCoronas, ventana, indice);
				puntajeRecursivo(tipoRegion, fila, columna - 1, puntosYCoronas, ventana, indice);
			}
		}
		return;

	}

	public boolean ponerCarta(Carta carta, int columna, int fila, boolean mostrarMensaje, VentanaJueguito ventana) {
		Ficha[] fichas = carta.getFichas();
		carta.moverCarta(fila, columna);

		if (esPosibleInsertar(carta, mostrarMensaje, ventana)) {

			int f1f = fichas[0].getFila();
			int f1c = fichas[0].getColumna();
			int f2f = fichas[1].getFila();
			int f2c = fichas[1].getColumna();
			comprobarLimite(f1f, f1c, f2f, f2c, tamTablero,true);
			tablero[f1f][f1c] = fichas[0];
			tablero[f2f][f2c] = fichas[1];
			cantTerrenoColocado++;
			return true;
		} else {
			carta.setDefault();
			return false;
		}
	}

	/*
	 * Sobrecarga del metodo para que reciba la ventana como argumento
	 */
	protected boolean esPosibleInsertar(Carta carta, boolean mostrarMensaje, VentanaJueguito ventana) {
		Ficha[] fichas = carta.getFichas();
		int f1f = fichas[0].getFila();
		int f1c = fichas[0].getColumna();
		int f2f = fichas[1].getFila();
		int f2c = fichas[1].getColumna();

		if (f1f >= tablero.length || f1f < 0 || f1c >= tablero.length || f1c < 0 || f2f >= tablero.length || f2f < 0
				|| f2c >= tablero.length || f2c < 0) {
			if (mostrarMensaje) {
				ventana.mostrarError("ERROR- COORDENADA FUERA DE RANGO");
			}
			return false;
		}
		// Si ya hay alguna "ficha" colocada en la posicion de la carta entonces
		// devuelvo false
		if (tablero[f1f][f1c] != null || tablero[f2f][f2c] != null) {
			if (mostrarMensaje) {
				ventana.mostrarError("ERROR- YA HAY UNA CARTA EN ESA POSICION");
			}
			return false;
		}
		// Si no hay tipos adyacentes compatibles, no se puede poner la carta
		if (!tipoAdyacenteCompatible(carta)) {
			if (mostrarMensaje) {
				ventana.mostrarError("ERROR- NO HAY TIPOS ADYACENTES COMPATIBLES");
			}
			return false;
		}

		// comprobamos que no salga del limite
		if (!comprobarLimite(f1f, f1c, f2f, f2c, tamTablero,false)) {
			if (mostrarMensaje) {
				ventana.mostrarError("ERROR- SE EXCEDE EL LIMITE DE CONSTRUCCION");
			}
			return false;
		}

		return true;
	}

	private boolean comprobarLimite(int f1f, int f1c, int f2f, int f2c, int limite,boolean modificarLimite) {
		int fMinAux = fMin;
		int fMaxAux = fMax;
		int cMinAux = cMin;
		int cMaxAux = cMax;

		if (f1f < fMinAux)
			fMinAux = f1f;

		if (f1f > fMaxAux)
			fMaxAux = f1f;

		if (f1c < cMinAux)
			cMinAux = f1c;

		if (f1c > cMaxAux)
			cMaxAux = f1c;

		if (f2f < fMinAux)
			fMinAux = f2f;

		if (f2f > fMaxAux)
			fMaxAux = f2f;

		if (f2c < cMinAux)
			cMinAux = f2c;

		if (f2c > cMaxAux)
			cMaxAux = f2c;

		if (fMaxAux - fMinAux >= limite || cMaxAux - cMinAux >= limite) {
			return false;
		} else {
			if(modificarLimite) {
				fMin = fMinAux;
				fMax = fMaxAux;
				cMin = cMinAux;
				cMax = cMaxAux;
			}
			return true;
		}
	}

	protected boolean tipoAdyacenteCompatible(Carta carta) {
		Ficha[] fichas = carta.getFichas();
		int f1f = fichas[0].getFila();
		int f1c = fichas[0].getColumna();
		String f1T = fichas[0].getTipo();
		int f2f = fichas[1].getFila();
		int f2c = fichas[1].getColumna();
		String f2T = fichas[1].getTipo();

		Ficha fComparacion = f1c == tablero.length - 1 ? null : tablero[f1f][f1c + 1];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f1f == tablero.length - 1 ? null : tablero[f1f + 1][f1c];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f1c == 0 ? null : tablero[f1f][f1c - 1];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f1f == 0 ? null : tablero[f1f - 1][f1c];
		if (compararFichas(f1T, fComparacion))
			return true;

		fComparacion = f2c == tablero.length - 1 ? null : tablero[f2f][f2c + 1];
		if (compararFichas(f2T, fComparacion))
			return true;

		fComparacion = f2f == tablero.length - 1 ? null : tablero[f2f + 1][f2c];
		if (compararFichas(f2T, fComparacion))
			return true;

		fComparacion = f2c == 0 ? null : tablero[f2f][f2c - 1];
		if (compararFichas(f2T, fComparacion))
			return true;

		fComparacion = f2f == 0 ? null : tablero[f2f - 1][f2c];
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
		for (int i = fMin; i <= fMax; i++) {
			for (int j = cMin; j <= cMax; j++) {
				Ficha ficha = tablero[i][j];
				if (ficha != null)
					ret += String.format("%8s/%s/%s|", ficha.getTipo(), ficha.getCantCoronas(), ficha.getId());
				else
					ret += String.format("%10s|", " ");
			}
			ret += "\n";
		}
		return ret;
	}

	public boolean esPosibleInsertarEnTodoElTablero(Carta carta) {
		int cMinAux = cMin - 1;
		int fMinAux = fMin - 1;
		boolean noMostrarMensaje = false;

		carta.moverCarta(fMin - 1, cMin - 1);

		for (int i = fMin - 1; i <= fMax + 1; i++) {
			for (int j = cMin - 1; j <= cMax + 1; j++) {
				for (int r = 0; r < 4; r++) {
					if (esPosibleInsertar(carta, noMostrarMensaje,null)) {
						carta.setDefault();
						return true;
					} else {
						carta.rotarCarta();
					}

				}
				carta.moverCarta(0, 1);
			}
			carta.setDefault();
			carta.moverCarta(++fMinAux, cMinAux);

		}
		carta.setDefault();
		return false;
	}

	public void quitarCarta(Carta cartaElegida) {

		Ficha f1 = cartaElegida.getFichas()[0];
		Ficha f2 = cartaElegida.getFichas()[1];

		tablero[f1.getFila()][f1.getColumna()] = null;
		tablero[f2.getFila()][f2.getColumna()] = null;
		cartaElegida.setDefault();

	}

	public int getCantTerrenoColocado() {
		return cantTerrenoColocado;
	}

	public int getFMin() {
		return fMin;
	}

	public int getFMax() {
		return fMax;
	}

	public int getCMin() {
		return cMin;
	}

	public int getCMax() {
		return cMax;
	}

	public int getCentro() {
		return centro;
	}

	public String puntajeTotal(PanelInformacion panelInformacion) {
		int acumPuntos = 0;
		int contRegiones = 0;
		String puntajeTotal = "";
		for (int i = fMin; i <= fMax; i++) {
			for (int j = cMin; j <= cMax; j++) {

				int puntajeParcialPorRegion = contarPuntajeParcial(i, j, null, 0);
				acumPuntos += puntajeParcialPorRegion;
				if (puntajeParcialPorRegion > 0) {
					contRegiones++;
					String tipo = tablero[i][j].getTipo();
					puntajeTotal += contRegiones + "-" + tipo + "=" + puntajeParcialPorRegion + " puntos.\n";
				}
			}
		}
		puntajeTotal += "PUNTAJE TOTAL:" + acumPuntos;
		resetearContadoFichas();
		return puntajeTotal;
	}

	public boolean estaCastilloEnMedio() {
		/* |u| | | |w|
		 * | |F|F|F| |
		 * | |F|C|F| |
		 * | |F|F|F| |	
		 * |v| | | |z|
		 * 
		 * Fmin = 0
		 * Fmax = 4
		 * Cmin = 0
		 * Cmax = 4
		 * 
		 * Si C está centrado, dist(x,C) = cte para todo x que pertenece a {u,v,w,z}
		 * C siempre está en centro
		 */
		int[] verticeSupIzq = {fMin,cMin}; //u
		int[] verticeInfIzq = {fMax,cMin}; //v
		int[] verticeSupDer = {fMin,cMax}; //w
		int[] verticeInfDer = {fMax,cMax}; //z
		
		int[] vectorACastillo1 = {centro-verticeSupIzq[0],centro-verticeSupIzq[1]};
		int magnitud1 = (int)Math.sqrt(Math.pow(vectorACastillo1[0], 2) + Math.pow(vectorACastillo1[1],2) );
		int[] vectorACastillo2 = {centro-verticeInfIzq[0],centro-verticeInfIzq[1]};
		int magnitud2 = (int)Math.sqrt(Math.pow(vectorACastillo2[0], 2) + Math.pow(vectorACastillo2[1],2) );
		int[] vectorACastillo3 = {centro-verticeSupDer[0],centro-verticeSupDer[1]};
		int magnitud3 = (int)Math.sqrt(Math.pow(vectorACastillo3[0], 2) + Math.pow(vectorACastillo3[1],2) );
		int[] vectorACastillo4 = {centro-verticeInfDer[0],centro-verticeInfDer[1]};
		int magnitud4 = (int)Math.sqrt(Math.pow(vectorACastillo4[0], 2) + Math.pow(vectorACastillo4[1],2) );
		System.out.println("----------DEBUG-------");
		System.out.println("Fmin: " + fMin);
		System.out.println("Fmax: " + fMax);
		System.out.println("Cmin: " + cMin);
		System.out.println("Cmax: " + cMax);
		System.out.println("Magnitudes: " + magnitud1 + "|" + magnitud2 + "|" + magnitud3 + "|" + magnitud4);
		return magnitud1 == magnitud2 && magnitud2 == magnitud3 && magnitud3 == magnitud4;
	}
}
