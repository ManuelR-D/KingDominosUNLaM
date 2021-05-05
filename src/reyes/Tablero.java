package reyes;

public class Tablero {
	private int[][] tablero;
	public Tablero(int tamTablero) {
		this.tablero = new int[tamTablero*2 - 1][tamTablero*2 - 1];
	}

}
