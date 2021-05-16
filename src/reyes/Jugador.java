package reyes;
import java.util.ArrayList;
import java.util.List;

import ClasesQueTodaviaNoSeUsan.Usuario;
public class Jugador {
	private Usuario dueño;
//	private int cantReyes; No se usa en esta implementacion
	private List<Carta> cartasCastillo = new ArrayList<Carta>();
	private Tablero tablero;
	
	public Jugador(Usuario dueño, int tamTablero) {
		this.dueño = dueño;
		this.tablero = new Tablero(tamTablero);
	}
}
