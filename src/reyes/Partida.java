package reyes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import SwingApp.VentanaJueguito;


public class Partida {
	private Mazo mazo;
	private List<Jugador> jugadores;
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
		// Creamos a cada jugador
		jugadores = new ArrayList<Jugador>(2);
		jugadores.add(new Bot("BotTest!", tamanioTablero));
		jugadores.add(new Jugador("Jugador 1", tamanioTablero));
	}

	public Partida(List<Jugador> jugadores) throws KingDominoExcepcion {
		this.cantidadCartas = DEFAULT_CANT_CARTAS;
		this.tamanioTablero = DEFAULT_TAM_TABLERO;
		this.jugadores = jugadores;
		this.cantidadJugadores = jugadores.size();
		if (cantidadJugadores > 4 || cantidadJugadores < 2) {
			throw new KingDominoExcepcion("La cantidad de jugadores es invalida!!");
		}
	}
	public Partida(List<Jugador> jugadores, int tamanioTablero, int cantidadCartas) throws KingDominoExcepcion {

		if (cantidadCartas != 48) {
			throw new KingDominoExcepcion(
					"La cantidad de cartas tiene que ser 48! (limitación por parte del enunciado)");
			// El código puede funcionar sin problemas con cualquier cantidad de cartas
			// mientras el total sea múltiplo de 4, pues siempre se roba de a 4 cartas.
			// Sin embargo, el enunciado tiene la limitación de 48 para todos los modos.
			// Se puede quitar esta validación en el futuro si quisieramos agregar otros
			// modos.
		}
		this.cantidadJugadores = jugadores.size();
		if (cantidadJugadores > 4 || cantidadJugadores < 2) {
			throw new KingDominoExcepcion("La cantidad de jugadores es invalida!!");
		}
		this.cantidadCartas = cantidadCartas;
		this.tamanioTablero = tamanioTablero;
		this.jugadores = jugadores;
	}
	
	public boolean iniciarPartida() throws IOException {

		List<Integer> turnos = determinarTurnosIniciales();
		List<Carta> cartasAElegirSig = new ArrayList<Carta>();
		// armamos y mezclamos el mazo
		mazo = new Mazo(cantidadCartas);
		mazo.mezclarMazo();
		//seteamos el tablero para cada jugador
		for (Jugador jugador : jugadores) {
			jugador.setTablero(this.tamanioTablero);
		}

		int rondas = 0;
		Scanner entrada = new Scanner(System.in);
		VentanaJueguito ventana = new VentanaJueguito(this);
		
		while (mazo.getTam() > 1) {
			System.out.println("--------Ronda: " + ++rondas + "--------");
			cartasAElegirSig.clear();
			mazo.quitarPrimerasNCartas(4, cartasAElegirSig);
			cartasAElegirSig.sort(Carta::compareTo);
			elegirCartas(cartasAElegirSig, turnos, ventana,entrada);
			
		}
		//entrada.close(); Los scanner asociados a System.in no se tienen que cerrar
		//puesto que genera errores más adelante, debido a que se cierra System.in
		//la JVM es la encargada de cerrar esa entrada si lo considera necesario

		System.out.println("-------Partida finalizada!!!-------");

		List<Integer> puntajesFinales = calcularPuntajesFinales();

		
		ventana.terminarPartida(determinarGanadores(puntajesFinales));
		return true;
	}

	private List<Integer> calcularPuntajesFinales() {
		List<Integer> puntajesFinales = new ArrayList<Integer>();
		for (Jugador jugador : jugadores) {
			System.out.println("-------Tablero de Jugador " + jugador.getNombre() + "-------");
			System.out.println(jugador.tablero);
			puntajesFinales.add(jugador.tablero.puntajeTotal(true));
		}
		return puntajesFinales;
	}

	private List<Jugador> determinarGanadores(List<Integer> puntajesFinales) {
		List<Integer> ganadoresPorPunto = obtenerGanadoresPorPuntos(puntajesFinales);
		List<Jugador> ganadores = new ArrayList<Jugador>(jugadores.size());
		if (ganadoresPorPunto.size() == 1) {
			System.out.println("Ha ganado " + jugadores.get(ganadoresPorPunto.get(0)).getNombre());
			ganadores.add(jugadores.get(ganadoresPorPunto.get(0)));
			return ganadores;
		}

		List<Integer> ganadoresPorTerreno = obtenerGanadoresPorTerreno(ganadoresPorPunto);

		if (ganadoresPorTerreno.size() == 1) {
			System.out.println("Ha ganado " + jugadores.get(ganadoresPorTerreno.get(0)).getNombre());
			ganadores.add(jugadores.get(ganadoresPorTerreno.get(0)));
			return ganadores;
		}

		// Si hay mas de un ganador por terreno se comparte la victoria

		System.out.println("No se pudo desempatar, comparten la victoria :)");
		System.out.println("Ganadores:");
		for (int i = 0; i < ganadoresPorTerreno.size(); i++) {
			System.out.println(jugadores.get(ganadoresPorTerreno.get(i)).getNombre());
			ganadores.add(jugadores.get(ganadoresPorTerreno.get(i)));
		}
		return ganadores;

	}

	private List<Integer> obtenerGanadoresPorPuntos(List<Integer> puntajesFinales) {
		int maxPuntaje = 0;
		List<Integer> ganadoresPorPunto = new ArrayList<Integer>();

		System.out.println("PUNTAJES FINALES:");
		for (int i = 0; i < puntajesFinales.size(); i++) {
			Integer puntaje = puntajesFinales.get(i);
			System.out.println(jugadores.get(i).getNombre() + ":" + puntaje);
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
		return ganadoresPorPunto;
	}

	private List<Integer> obtenerGanadoresPorTerreno(List<Integer> ganadoresPorPunto) {
		// Si hay mas de un ganador por puntos, se define por cantidad de terreno
		int maxTerreno = 0;
		List<Integer> ganadoresPorTerreno = new ArrayList<Integer>();
		System.out.println("EMPATE POR PUNTOS");
		for (int i = 0; i < ganadoresPorPunto.size(); i++) {
			int cantTerreno = jugadores.get(ganadoresPorPunto.get(0)).getCantTerrenoColocado();
			System.out.println(jugadores.get(i).getNombre() + ":" + cantTerreno);
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
		return ganadoresPorTerreno;
	}

	private void elegirCartas(List<Carta> cartasAElegir, List<Integer> turnos, VentanaJueguito entrada, Scanner teclado) throws IOException {

		int numeroElegido;
		Map<Integer, Integer> nuevoOrdenDeTurnos = new TreeMap<Integer, Integer>();

		for (int i = 0; i < turnos.size(); i++) {
			entrada.mostrarCartas(cartasAElegir);
			entrada.actualizarTableros(jugadores);
			int turno = turnos.get(i);
			numeroElegido = jugadores.get(turno).eligeCarta(cartasAElegir, entrada);
			//System.out.println(jugadores.get(turno).getNombre() + " elige carta " + (numeroElegido));
			//System.out.println(numeroElegido);
			jugadores.get(turno).insertaEnTablero(cartasAElegir.get(numeroElegido), entrada, teclado);
			cartasAElegir.set(numeroElegido, null);
			nuevoOrdenDeTurnos.put(numeroElegido, turno);
		}
		turnos.clear();
		for (Map.Entry<Integer, Integer> entry : nuevoOrdenDeTurnos.entrySet())
			turnos.add(entry.getValue());

		if(turnos.size() != nuevoOrdenDeTurnos.size())
			System.out.println("check");
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

	public List<Jugador> getJugadores() {
		return jugadores;
	}
	
	
}
