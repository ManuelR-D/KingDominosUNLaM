package reyes;

import java.io.IOException;
import java.util.List;

import SwingApp.VentanaJueguito;

public class Jugador extends Usuario {
	private String nombre;
	public Tablero tablero;

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
		if (!tablero.esPosibleInsertarEnTodoElTablero(carta))
			return false;
		int[] posicion = new int[2];
		do {
			posicion = ventana.obtenerInputCoordenadas(this);
		} while (!tablero.ponerCarta(carta, posicion[0], posicion[1], true,ventana));
		return true;
	}

	public int eligeCarta(List<Carta> cartasAElegir, VentanaJueguito entrada) throws IOException {
		int cartaElegida = 0;
		
		System.out.println("Tablero(" + nombre + ")");
		System.out.println(tablero);
		do {
//			String cad = "Elija una carta(" + nombre + "):";
			cartaElegida = entrada.leerCartaElegida();
			for (int i = 0; i < cartasAElegir.size(); i++) {
				Carta c = cartasAElegir.get(i);
				if (c != null) {
					System.out.println((i + 1) + ":");
					System.out.println(c);
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
}
