package reyes;

public class Jugador {
	private String nombre;
//	private int cantReyes; No se usa en esta implementacion
	private Tablero tablero;
	
	public Jugador(String nombre, int tamTablero) {
		this.nombre = nombre;
		this.tablero = new Tablero(tamTablero);
	}
}
