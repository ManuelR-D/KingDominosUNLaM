package reyes;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
	public Ficha[][] tablero;// Puse esto en public solamente para hacer test directamente

	public Tablero(int tamTablero) {
		this.tablero = new Ficha[tamTablero * 2 - 1][tamTablero * 2 - 1];
	}

	public int puntajeTotal() {
		int acum = 0;
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++) {
				acum += contarPuntajeParcial(i, j);
			}
		}
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
		if(!(x>=0 && x<tablero.length && y>=0 && y<tablero[x].length))
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

}
