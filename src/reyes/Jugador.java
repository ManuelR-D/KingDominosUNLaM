package reyes;

import java.io.IOException;
import java.util.List;

import SwingApp.VentanaJueguito;

public class Jugador extends Usuario {
	private String nombre;
	public Tablero tablero;
	private boolean reinoCompletamenteOcupado = true;

	public Jugador(String nombre, int tamTablero) {
		super(nombre, "");
		this.nombre = nombre;
		this.tablero = new Tablero(tamTablero);
	}

	public Jugador(Usuario user) {
		super(user);
		this.nombre = user.getNombreUsuario();
	}

	public Jugador(String nombre, String contrasenia) {
		super(nombre, contrasenia);
	}

	public String getNombre() {
		return nombre;
	}

	public boolean insertaEnTablero(Carta carta, VentanaJueguito ventana) {
		if (!tablero.esPosibleInsertarEnTodoElTablero(carta)) {
			reinoCompletamenteOcupado = false;
			return false;
		}
		int[] posicion = new int[2];
		do {
			posicion = ventana.obtenerInputCoordenadas(carta);
		} while (!tablero.ponerCarta(carta, posicion[0], posicion[1], true, ventana));
		return true;
	}

	public int eligeCarta(List<Carta> cartasAElegir, VentanaJueguito entrada) throws IOException {
		int cartaElegida = 0;
		do {
			cartaElegida = entrada.leerCartaElegida();
			for (int i = 0; i < cartasAElegir.size(); i++) {
				Carta c = cartasAElegir.get(i);
				if (c != null) {
					if (c.getId() == cartaElegida)
						cartaElegida = i;
				}

			}

		} while (cartasAElegir.get(cartaElegida) == null);

		return cartaElegida;
	}

	public int getCantTerrenoColocado() {
		return tablero.getCantTerrenoColocado();
	}

	public void setTablero(int tamanioTablero) {
		this.tablero = new Tablero(tamanioTablero);
	}

	public Tablero getTablero() {
		return tablero;
	}

	public boolean tieneReinoCompletamenteOcupado() {
		return reinoCompletamenteOcupado;
	}

	public void setIdCastilloCentro(int i) {
		tablero.setIdCastilloCentro(i);
	}
}
