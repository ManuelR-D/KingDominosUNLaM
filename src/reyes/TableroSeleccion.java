package reyes;

import java.util.List;

public class TableroSeleccion extends Tablero {
	public TableroSeleccion() {
		super(4);
		this.tablero[4 - 1][4 - 1] = null;
	}

	public TableroSeleccion(int tamTablero, List<Carta> cartasAElegir) {
		super(tamTablero);
		this.tablero[tamTablero - 1][tamTablero - 1] = null;
		int[][] posiciones = { {0,0},
							   {2,0},
							   {0,1},
							   {2,1}};
		int i = 0;
		for (Carta carta : cartasAElegir) {
			if(carta != null) {
				ponerCarta(carta, posiciones[i][0], posiciones[i][1], true);
				carta.setDefault();
				i++;
			}
		}
	}

	public TableroSeleccion(int tamTablero, Carta c) {
		super(tamTablero);
		this.tablero[tamTablero - 1][tamTablero - 1] = null;

		ponerCarta(c, 0, 0, false);
		//c.setPosicionDefault();
	}

	@Override
	public boolean ponerCarta(Carta carta, int columnaAbsoluta, int filaAbsoluta, boolean mostrarMensaje) {
		Ficha[] fichas = carta.getFichas();
		//carta.moverCarta(centro, centro);
		//carta.moverCarta(-filaRelativa, columnaRelativa);
		int f1X = fichas[0].getX() + filaAbsoluta + 2;
		int f1Y = fichas[0].getY() + columnaAbsoluta + 2;
		int f2X = fichas[1].getX() + filaAbsoluta + 2;
		int f2Y = fichas[1].getY() + columnaAbsoluta + 2;
		actualizarLimites(f1X, f1Y, f2X, f2Y, tamTablero);
		tablero[f1X][f1Y] = fichas[0];
		tablero[f2X][f2Y] = fichas[1];
		return true;
	}

	@Override
	public boolean esPosibleInsertarEnTodoElTablero(Carta carta) {
		return true;
	}

	@Override
	protected boolean esPosibleInsertar(Carta carta, boolean mostrarMensaje) {
		return true;
	}

	@Override
	protected boolean tipoAdyacenteCompatible(Carta carta) {
		return true;
	}

}
