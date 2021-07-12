package reyes;

import java.io.IOException;
import java.util.List;

import SwingApp.VentanaJueguito;
import netcode.HiloCliente;

public class Jugador extends Usuario {

	private static final long serialVersionUID = -5342621303315896626L;
	private String nombre;
	protected Tablero tablero;
	protected boolean reinoCompletamenteOcupado = true;

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
			ventana.mostrarError("La carta no se puede colocar en ninguna posicion, se descarta.");
			return false;
		}
		int[] posicion = new int[2];
		do {
			posicion = ventana.obtenerInputCoordenadas(carta);
		} while (!tablero.ponerCarta(carta, posicion[0], posicion[1], true, ventana));
		return true;
	}

	public int eligeCarta(List<Carta> cartasAElegir, VentanaJueguito entrada,Partida partida) throws IOException {
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
		} while (!partida.isRendido() && cartasAElegir.get(cartaElegida) == null);

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

	public void crearPaquete(Partida partida,HiloCliente hiloCliente,int numeroElegido, int coordenadaX, int coordenadaY, boolean pudoInsertar, int rotacion) {
		String insercion = pudoInsertar ? "S" : "N";
		partida.setPaquete(numeroElegido + "," + coordenadaX + "," + coordenadaY + "," + this.nombre + "," + insercion
				+ "," + rotacion);
		hiloCliente.getMtxPaquetePartida().countDown(); // aviso a mi hilo que tiene preparado un paquete
	}

}
