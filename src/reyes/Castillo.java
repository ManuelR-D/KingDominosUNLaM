package reyes;
import java.util.ArrayList;
import java.util.List;
public class Castillo {
	private Usuario dueño;
	private int cantReyes;
	private List<Carta> cartasCastillo = new ArrayList<Carta>();
	private Tablero tablero;
	
	public Castillo(Usuario dueño, int cantReyes, int tamTablero) {
		this.dueño = dueño;
		this.cantReyes = cantReyes;
		this.tablero = new Tablero(tamTablero);
	}
}
