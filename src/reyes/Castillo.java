package reyes;
import java.util.ArrayList;
import java.util.List;
public class Castillo {
	private Usuario due�o;
	private int cantReyes;
	private List<Carta> cartasCastillo = new ArrayList<Carta>();
	private Tablero tablero;
	
	public Castillo(Usuario due�o, int cantReyes, int tamTablero) {
		this.due�o = due�o;
		this.cantReyes = cantReyes;
		this.tablero = new Tablero(tamTablero);
	}
}
