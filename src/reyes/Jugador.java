package reyes;
import java.util.ArrayList;
import java.util.List;

import ClasesQueTodaviaNoSeUsan.Usuario;
public class Jugador {
	private Usuario due�o;
//	private int cantReyes; No se usa en esta implementacion
	private List<Carta> cartasCastillo = new ArrayList<Carta>();
	private Tablero tablero;
	
	public Jugador(Usuario due�o, int tamTablero) {
		this.due�o = due�o;
		this.tablero = new Tablero(tamTablero);
	}
}
