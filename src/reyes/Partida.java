package reyes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Partida {
	private Mazo mazo;
	private Jugador[] jugadores;
	private static final int DEFAULT_TAM_TABLERO = 5;
	private static final int DEFAULT_CANT_CARTAS = 48;
	private static final int DEFAULT_CANT_JUGADORES = 2;
	private int tamanioTablero;
	private int cantidadCartas;
	private int cantidadJugadores;

	public Partida() {
		this.tamanioTablero = DEFAULT_TAM_TABLERO;
		this.cantidadCartas = DEFAULT_CANT_CARTAS;
		this.cantidadJugadores = DEFAULT_CANT_JUGADORES;
	}

	public Partida(int cantidadJugadores, int tamanioTablero, int cantidadCartas) throws KingDominoExcepcion {
		if (cantidadJugadores > 4 || cantidadJugadores < 2) {
			throw new KingDominoExcepcion("La cantidad de jugadores es invalida!!");
		}
		if (cantidadCartas != 48) {
			throw new KingDominoExcepcion(
					"La cantidad de cartas tiene que ser 48! (limitación por parte del enunciado)");
			// El código puede funcionar sin problemas con cualquier cantidad de cartas
			// mientras el total sea múltiplo de 4, pues siempre se roba de a 4 cartas.
			// Sin embargo, el enunciado tiene la limitación de 48 para todos los modos.
			// Se puede quitar esta validación en el futuro si quisieramos agregar otros
			// modos.
		}
		this.cantidadCartas = cantidadCartas;
		this.tamanioTablero = tamanioTablero;
		this.cantidadJugadores = cantidadJugadores;
	}

	public boolean iniciarPartida() {

		List<Integer> turnos = determinarTurnosIniciales();
		List<Carta> cartasAElegirSig = new ArrayList<Carta>();
		this.jugadores = new Jugador[cantidadJugadores];

		// Creamos a cada jugador
		for (int i = 0; i < jugadores.length; i++) {
			jugadores[i] = new Jugador("Jugador " + (i + 1), tamanioTablero);
		}

		jugadores[0] = new Bot("BotTest!", tamanioTablero);
		// armamos y mezclamos el mazo
		mazo = new Mazo(cantidadCartas);
		mazo.mezclarMazo();
		;

		int rondas = 0;
		Scanner entrada = new Scanner(System.in);
		while (mazo.getTam() > 1) {
			System.out.println("--------Ronda: " + ++rondas + "--------");
			cartasAElegirSig.clear();
			mazo.quitarPrimerasNCartas(4, cartasAElegirSig);
			cartasAElegirSig.sort(Carta::compareTo);
			elegirCartas(cartasAElegirSig, turnos, entrada);
		}
		entrada.close();

		System.out.println("-------Partida finalizada!!!-------");
		List<Integer> puntajesFinales = new ArrayList<Integer>();
		boolean mostrarRegiones = true;

		for (Jugador jugador : jugadores) {
			System.out.println("-------Tablero de Jugador " + jugador.nombre + "-------");
			System.out.println(jugador.tablero);
			int puntajeFinal = jugador.tablero.puntajeTotal(mostrarRegiones);
			puntajesFinales.add(puntajeFinal);
		}

		determinarGanadores(puntajesFinales);
		return true;
	}

	private void determinarGanadores(List<Integer> puntajesFinales) {
		int maxPuntaje = 0;
		List<Integer> ganadoresPorPunto = new ArrayList<Integer>();

		System.out.println("PUNTAJES FINALES:");
		for (int i = 0; i < puntajesFinales.size(); i++) {
			Integer puntaje = puntajesFinales.get(i);
			System.out.println(jugadores[i].getNombre() + ":" + puntaje);
			if (puntaje > maxPuntaje) {
				maxPuntaje = puntaje;
				ganadoresPorPunto.clear();
				ganadoresPorPunto.add(i);
			} else {
				if (puntaje == maxPuntaje) {
					ganadoresPorPunto.add(i);
				}
			}
		}

		if (ganadoresPorPunto.size() == 1) {
			System.out.println("Ha ganado " + jugadores[ganadoresPorPunto.get(0)].getNombre());
			return;
		}

		// Si hay mas de un ganador por puntos, se define por cantidad de terreno
		int maxTerreno = 0;
		List<Integer> ganadoresPorTerreno = new ArrayList<Integer>();
		System.out.println("EMPATE POR PUNTOS");
		for (int i = 0; i < ganadoresPorPunto.size(); i++) {
			int cantTerreno = jugadores[ganadoresPorPunto.get(i)].getCantTerrenoColocado();
			System.out.println(jugadores[i].getNombre() + ":" + cantTerreno);
			if (cantTerreno > maxTerreno) {
				maxTerreno = cantTerreno;
				ganadoresPorTerreno.clear();
				ganadoresPorTerreno.add(i);
			} else {
				if (cantTerreno == maxTerreno) {
					ganadoresPorTerreno.add(i);
				}
			}
		}

		if (ganadoresPorTerreno.size() == 1) {
			System.out.println("Ha ganado " + jugadores[ganadoresPorTerreno.get(0)].getNombre());
			return;
		}

		// Si hay mas de un ganador por terreno se comparte la victoria

		System.out.println("No se pudo desempatar, comparten la victoria :)");
		System.out.println("Ganadores:");
		for (int i = 0; i < ganadoresPorTerreno.size(); i++) {
			System.out.println(jugadores[ganadoresPorTerreno.get(i)].getNombre());
		}

	}

	private void elegirCartas(List<Carta> cartasAElegir, List<Integer> turnos, Scanner entrada) {

		int numeroElegido;
		List<Integer> numerosElegidos = new LinkedList<Integer>();
		Map<Integer, Integer> nuevoOrdenDeTurnos = new TreeMap<Integer, Integer>();

		for (int i = 0; i < turnos.size(); i++) {
			int turno = turnos.get(i);

			if (i == cartasAElegir.size() - 1) { // el ultimo jugador en elegir, no tiene decision
				for (numeroElegido = 0; cartasAElegir.get(numeroElegido) == null; numeroElegido++)
					;
				jugadores[turno].insertaEnTablero(cartasAElegir.get(numeroElegido), entrada);
			} else {

				do {
					numeroElegido = jugadores[turno].eligeCarta(cartasAElegir, entrada);
				} while (numerosElegidos.contains(numeroElegido));

				System.out.println(jugadores[turnos.get(i)].getNombre() + " elige carta " + (numeroElegido + 1));
				jugadores[turno].insertaEnTablero(cartasAElegir.remove(numeroElegido), entrada);
			}
			numerosElegidos.add(numeroElegido);
			// Los numeros elegidos se guardan en un map, ya que el menor de estos decide
			// quien comienza el turno siguiente
			nuevoOrdenDeTurnos.put(numeroElegido, turno);
		}
		turnos.clear();
		for (Map.Entry<Integer, Integer> entry : nuevoOrdenDeTurnos.entrySet()) {
			// modifico turnos de acuerdo al numero elegido
			turnos.add(entry.getValue());
		}
	}

	private List<Integer> determinarTurnosIniciales() {
		List<Integer> turnos = new ArrayList<Integer>();
		List<Integer> idJugadores = new ArrayList<Integer>(4);

		for (int i = 0; i < cantidadJugadores; i++) {
			idJugadores.add(i);
		}
		for (int i = 0; i < cantidadJugadores; i++) {
			int numeroAleatorio = FuncionesGenerales.numeroAleatorioEntre(0, cantidadJugadores - i - 1);
			turnos.add(idJugadores.remove(numeroAleatorio));
		}

		return turnos;
	}
}
