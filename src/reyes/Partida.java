package reyes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Partida {
	private List<Carta> mazo;
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

	public Partida(int cantidadJugadores, int tamanioTablero, int cantidadCartas) {
		this.cantidadCartas = cantidadCartas;
		this.tamanioTablero = tamanioTablero;
		this.cantidadJugadores = cantidadJugadores;
	}

	public List<Carta> getMazo() {
		return mazo;
	}

	public List<Carta> mezclarMazo(List<Carta> cartas) {
		List<Carta> cartasMezcladas = new ArrayList<Carta>(cantidadCartas);

		for (int i = 0; i < cantidadCartas; i++) {
			int numeroAleatorio = numeroAleatorioEntre(0, cantidadCartas - i - 1);
			cartasMezcladas.add(cartas.remove(numeroAleatorio));
		}

		return cartas = cartasMezcladas;
	}

	private int numeroAleatorioEntre(int mayorIgualQue, int menorIgualQue) {
		int numeroAleatorio = (int) Math.floor(Math.random() * (menorIgualQue - mayorIgualQue + 1) + mayorIgualQue);
		return numeroAleatorio;
	}

	public List<Carta> armarMazo() {
		List<Carta> cartas = new ArrayList<Carta>(cantidadCartas);
		File file = new File("./assets/cartas.txt");
		Scanner scanner;

		try {
			int idCarta = 1;
			scanner = new Scanner(file);
			while (idCarta <= cantidadCartas) {

				if (scanner.hasNextLine()) {
					String tipoIzq = scanner.nextLine();
					int cantCoronasI = Integer.parseInt(scanner.nextLine());
					String tipoDer = scanner.nextLine();
					int cantCoronasD = Integer.parseInt(scanner.nextLine());
					cartas.add(new Carta(idCarta, tipoIzq, cantCoronasI, tipoDer, cantCoronasD));
					idCarta++;
				} else {
					scanner.close(); // volvemos al inicio del archivo y seguimos cargando
					scanner = new Scanner(file);
				}
			}
			// si la cantidad de cartas no fuera multiplo de 48, podria suceder que scanner
			// siguiera abierto
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("No se encontro el archivo de cartas");
			return null;
		}

		return cartas;
	}

	public boolean iniciarPartida() {
		if (cantidadJugadores > 4 || cantidadJugadores < 2) {
			System.out.println("La cantidad de jugadores es invalida!!");
			return false;
		}
		// El código puede funcionar sin problemas con cualquier cantidad de cartas
		// mientras el total sea múltiplo de 4, pues siempre se roba de a 4 cartas.
		// Sin embargo, el enunciado tiene la limitación de 48 para todos los modos.
		// Se puede quitar esta validación en el futuro si quisieramos agregar otros
		// modos.
		if (cantidadCartas != 48) {
			System.out.println("La cantidad de cartas tiene que ser 48! (limitación por parte del enunciado)");
			return false;
		}
		List<Integer> turnos = determinarTurnosIniciales();
		List<Carta> cartasAElegirSig = new ArrayList<Carta>();
		this.jugadores = new Jugador[cantidadJugadores];

		// Creamos a cada jugador
		for (int i = 0; i < jugadores.length; i++) {
			jugadores[i] = new Jugador("Jugador " + (i + 1), tamanioTablero);
		}
		jugadores[0] = new Bot("BotTest!", tamanioTablero);
		// armamos y mezclamos el mazo
		mazo = new ArrayList<Carta>(armarMazo());
		mazo = mezclarMazo(mazo);

		int rondas = 0;
		while (mazo.size() > 1) {
			System.out.println("--------Ronda: " + ++rondas + "--------");
			cartasAElegirSig.clear();
			quitarNCartasDelMazo(mazo, 4, cartasAElegirSig);
			cartasAElegirSig.sort(Carta::compareTo);
			elegirCartas(cartasAElegirSig, turnos);
		}

		System.out.println("-------Partida finalizada!!!-------");
		List<Integer> puntajesFinales = new ArrayList<Integer>();

		for (Jugador jugador : jugadores) {
			System.out.println("-------Tablero de Jugador " + jugador.nombre + "-------");
			System.out.println(jugador.tablero);
			List<String> puntajesTotales = jugador.tablero.puntajeTotal();
			for (int i = 0; i < puntajesTotales.size(); i++) {
				String puntaje = puntajesTotales.get(i);
				System.out.println(puntaje);
				if (i == puntajesTotales.size() - 1) {
					int puntajeFinal = Integer.valueOf(puntaje.split(":")[1]);
					puntajesFinales.add(puntajeFinal);
				}
			}

		}

		int maxPuntaje = 0;
		String ganador = null;

		for (int i = 0; i < puntajesFinales.size(); i++) {
			Integer puntaje = puntajesFinales.get(i);
			if (puntaje > maxPuntaje) {
				maxPuntaje = puntaje;
				ganador = jugadores[i].getNombre();
			}
		}

		System.out.println("Ha ganado " + ganador);

		return true;
	}

	private void elegirCartas(List<Carta> cartasAElegir, List<Integer> turnos) {
		int numeroElegido;
		List<Integer> numerosElegidos = new LinkedList<Integer>();
		Map<Integer, Integer> nuevoOrdenDeTurnos = new TreeMap<Integer, Integer>();
		for (int i = 0; i < turnos.size(); i++) {
			int turno = turnos.get(i);
			if (i == cartasAElegir.size() - 1) { // el ultimo jugador en elegir, no tiene decision
				for (numeroElegido = 0; cartasAElegir.get(numeroElegido) == null; numeroElegido++);
				jugadores[turno].insertaEnTablero(cartasAElegir.get(numeroElegido));
			} else {
				do {
					numeroElegido = jugadores[turno].eligeCarta(cartasAElegir);
				} while (numerosElegidos.contains(numeroElegido));
				jugadores[turno].insertaEnTablero(cartasAElegir.get(numeroElegido));
				cartasAElegir.set(numeroElegido, null);
			}
			numerosElegidos.add(numeroElegido);
			// Los numeros elegidos se guardan en un map, ya que el menor de estos decide
			// quien comienza el turno siguiente
			nuevoOrdenDeTurnos.put(numeroElegido, turno);
		}
		turnos.clear();
		for (Map.Entry<Integer, Integer> entrada : nuevoOrdenDeTurnos.entrySet()) {
			// modifico turnos de acuerdo al numero elegido
			turnos.add(entrada.getValue());
		}
	}

	public void quitarNCartasDelMazo(List<Carta> mazo, int n, List<Carta> cartas) {
		for (int i = 0; i < n; i++) {
			cartas.add(mazo.remove(0));
		}

	}

	private List<Integer> determinarTurnosIniciales() {
		List<Integer> turnos = new ArrayList<Integer>();
		List<Integer> idJugadores = new ArrayList<Integer>(4);

		for (int i = 0; i < cantidadJugadores; i++) {
			idJugadores.add(i);
		}
		for (int i = 0; i < cantidadJugadores; i++) {
			int numeroAleatorio = numeroAleatorioEntre(0, cantidadJugadores - i - 1);
			turnos.add(idJugadores.remove(numeroAleatorio));
		}

		return turnos;
	}
}
