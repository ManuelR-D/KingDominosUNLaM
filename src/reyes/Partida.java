package reyes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Partida {
	private List<Carta> mazo;
	private Jugador[] jugadores;
	private final static int TAMAÑO_TABLERO = 5;
	private final static int CANTIDAD_CARTAS = 48;
	private final static int CANTIDAD_JUGADORES = 2;

	public Partida() {
		this.jugadores = new Jugador[CANTIDAD_JUGADORES];

		// Creamos a cada jugador
		for (int i = 0; i < jugadores.length; i++) {
			jugadores[i] = new Jugador("Jugador " + (i + 1), TAMAÑO_TABLERO);
		}
		mazo = new ArrayList<Carta>(armarMazo());
		mazo = mezclarMazo(mazo);
	}

	public List<Carta> getMazo() {
		return mazo;
	}

	public List<Carta> mezclarMazo(List<Carta> cartas) {
		List<Carta> cartasMezcladas = new ArrayList<Carta>(CANTIDAD_CARTAS);

		for (int i = 0; i < CANTIDAD_CARTAS; i++) {
			int numeroAleatorio = numeroAleatorioEntre(0, CANTIDAD_CARTAS - i - 1);
			cartasMezcladas.add(cartas.remove(numeroAleatorio));
		}

		return cartas = cartasMezcladas;
	}

	private int numeroAleatorioEntre(int mayorIgualQue, int menorIgualQue) {
		int numeroAleatorio = (int) Math.floor(Math.random() * (menorIgualQue - mayorIgualQue + 1) + mayorIgualQue);
		return numeroAleatorio;
	}

	public List<Carta> armarMazo() {
		List<Carta> cartas = new ArrayList<Carta>(CANTIDAD_CARTAS);
		File file = new File("./assets/cartas.txt");
		Scanner scanner;

		try {
			scanner = new Scanner(file);
			int idCarta = 1;
			while (scanner.hasNextLine()) {
				String tipoIzq = scanner.nextLine();
				int cantCoronasI = Integer.parseInt(scanner.nextLine());
				String tipoDer = scanner.nextLine();
				int cantCoronasD = Integer.parseInt(scanner.nextLine());
				cartas.add(new Carta(idCarta, tipoIzq, cantCoronasI, tipoDer, cantCoronasD));
				idCarta++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("No se encontro el archivo de cartas");
			return null;
		}
		scanner.close();

		return cartas;
	}

	public void iniciarPartida() {
		List<Integer> turnos = determinarTurnosIniciales();
		List<Carta> cartasAElegirSig = new ArrayList<Carta>();

		int i = 0;
		while (mazo.size() > 1) {
			System.out.println("--------Ronda: " + ++i + "--------");
			cartasAElegirSig.clear();
			quitarNCartasDelMazo(mazo, 4, cartasAElegirSig);
			cartasAElegirSig.sort(Carta::compareTo);
			elegirCartas(cartasAElegirSig, turnos);
		}
		i = 0;
		for (Jugador jugador : jugadores) {
			System.out.println("-------Tablero de Jugador " + ++i + "-------");
			System.out.println(jugador.tablero);
			jugador.tablero.puntajeTotal();
		}
	}

	private void elegirCartas(List<Carta> cartasAElegir, List<Integer> turnos) {
		int numeroElegido;
		List<Integer> numerosElegidos = new LinkedList<Integer>();
		Map<Integer, Integer> nuevoOrdenDeTurnos = new TreeMap<Integer, Integer>();
		for (int i = 0; i < turnos.size(); i++) {
			do {
				/// ESTO DEBE CAMBIARSE CUANDO EL USUARIO PUEDA JUGAR YA QUE EL DECIDIRA QUE
				/// CARTA QUIERE
				numeroElegido = numeroAleatorioEntre(0, cartasAElegir.size() - 1);
			} while (numerosElegidos.contains(numeroElegido));
			numerosElegidos.add(numeroElegido);
			Carta cartaElegida = cartasAElegir.get(numeroElegido);
			int turno = turnos.get(i);

			//TODO falta la logica de cuando el jugador elige la carta pueda ponerla en su tablero
			jugadores[turno].eligeCarta(cartaElegida, numeroElegido);
			jugadores[turno].insertaEnTablero(cartaElegida);
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

	public void mostrarCartas(Set<Carta> cartas) {
		for (Carta carta : cartas) {
			System.out.println(carta);
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

		for (int i = 0; i < CANTIDAD_JUGADORES; i++) {
			idJugadores.add(i);
		}
		for (int i = 0; i < CANTIDAD_JUGADORES; i++) {
			int numeroAleatorio = numeroAleatorioEntre(0, CANTIDAD_JUGADORES - i - 1);
			turnos.add(idJugadores.remove(numeroAleatorio));
		}

		return turnos;
	}
}
