package reyes;

import java.util.List;

public class TableroSeleccion {
	Ficha[][] tablero;

	public TableroSeleccion() {
		tablero = new Ficha[4][3];
	}

	public TableroSeleccion(List<Carta> cartasAElegir) {

		this();

		for (int i = 0; i < cartasAElegir.size(); i++) {
			Carta carta = cartasAElegir.get(i);
			if (carta != null) {
				ponerCarta(carta, i);
				carta.setDefault();
			}
		}
	}

	public TableroSeleccion(Carta carta) {
		
		this();
		carta.moverCarta(1, 1);
		ponerCarta(carta, 0);
	}

	public void ponerCarta(Carta carta, int indice) {
		Ficha[] fichas = carta.getFichas();

		int f1X = fichas[0].getX() + indice;
		int f1Y = fichas[0].getY();
		int f2X = fichas[1].getX() + indice;
		int f2Y = fichas[1].getY();

		tablero[f1X][f1Y] = fichas[0];
		tablero[f2X][f2Y] = fichas[1];
	}

	public Ficha[][] getTablero() {
		return tablero;
	}

}
