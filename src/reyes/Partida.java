package reyes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private String textura = null;
	VentanaJueguito ventana;

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
			// El codigo puede funcionar sin problemas con cualquier cantidad de cartas
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

	public Partida(List<Jugador> jugadores, int tamanioTablero, int cantidadCartas, String textura)
			throws KingDominoExcepcion {
		if (cantidadCartas != 48) {
			throw new KingDominoExcepcion(
					"La cantidad de cartas tiene que ser 48! (limitación por parte del enunciado)");
			// El codigo puede funcionar sin problemas con cualquier cantidad de cartas
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
		this.textura = textura;
	}

	public boolean iniciarPartida() throws IOException {

		List<Integer> turnos = determinarTurnosIniciales();
		List<Carta> cartasAElegirSig = new ArrayList<Carta>();
		// armamos y mezclamos el mazo
		mazo = new Mazo(cantidadCartas);
		mazo.mezclarMazo();
		// seteamos el tablero para cada jugador
		for (Jugador jugador : jugadores) {
			jugador.setTablero(this.tamanioTablero);
		}

		ventana = new VentanaJueguito(this);
		if (textura != null) {
			ventana.cargarTextura(textura);
		}
		ventana.actualizarTableros();
		while (mazo.getTam() > 1) {
			cartasAElegirSig.clear();
			mazo.quitarPrimerasNCartas(4, cartasAElegirSig);
			cartasAElegirSig.sort(Carta::compareTo);
			jugarRonda(cartasAElegirSig, turnos, ventana);

		}

		ventana.setPSeleccionVisible(false);
		List<Integer> puntajesFinales = calcularPuntajesFinales();

		ventana.terminarPartida(determinarGanadores(puntajesFinales));
		return true;
	}

	private List<Integer> calcularPuntajesFinales() {
		List<Integer> puntajesFinales = new ArrayList<Integer>();
		for (int i = 0; i < jugadores.size(); i++) {
			Jugador jugador = jugadores.get(i);
			puntajesFinales.add(jugador.getTablero().puntajeTotal(true, ventana, i));
		}

		return puntajesFinales;
	}

	private Map<Jugador, Integer> determinarGanadores(List<Integer> puntajesFinales) {
		Map<Jugador, Integer> ganadoresPorPunto = obtenerGanadoresPorPuntos(puntajesFinales);
		return ganadoresPorPunto.size() == 1 ? ganadoresPorPunto : obtenerGanadoresPorTerreno(ganadoresPorPunto);
	}

	private Map<Jugador, Integer> obtenerGanadoresPorPuntos(List<Integer> puntajesFinales) {
		int maxPuntaje = 0;
		Map<Jugador, Integer> ganadoresPorPunto = new HashMap<Jugador, Integer>();
		for (int i = 0; i < puntajesFinales.size(); i++) {
			Integer puntaje = puntajesFinales.get(i);
			if (puntaje > maxPuntaje) {
				maxPuntaje = puntaje;
				ganadoresPorPunto.clear();
				ganadoresPorPunto.put(jugadores.get(i), puntaje);
			} else if (puntaje == maxPuntaje) {
				ganadoresPorPunto.put(jugadores.get(i), puntaje);
			}
		}
		return ganadoresPorPunto;
	}

	private Map<Jugador, Integer> obtenerGanadoresPorTerreno(Map<Jugador, Integer> ganadoresPorPunto) {
		// Si hay mas de un ganador por puntos, se define por cantidad de terreno
		int maxTerreno = 0;
		Map<Jugador, Integer> ganadoresPorTerreno = new HashMap<Jugador, Integer>();
		for (int i = 0; i < ganadoresPorPunto.size(); i++) {
			int cantTerreno = jugadores.get(i).getCantTerrenoColocado();
			if (cantTerreno > maxTerreno) {
				maxTerreno = cantTerreno;
				ganadoresPorTerreno.clear();
				ganadoresPorTerreno.put(jugadores.get(i), cantTerreno);
			} else if (cantTerreno == maxTerreno) {
				ganadoresPorTerreno.put(jugadores.get(i), cantTerreno);
			}
		}
		return ganadoresPorTerreno;
	}

	private void jugarRonda(List<Carta> cartasAElegir, List<Integer> turnos, VentanaJueguito entrada)
			throws IOException {

		int numeroElegido;
		Map<Integer, Integer> nuevoOrdenDeTurnos = new TreeMap<Integer, Integer>();

		for (int i = 0; i < turnos.size(); i++) {
			entrada.mostrarCartasAElegir(cartasAElegir);

			int turno = turnos.get(i);
			entrada.mostrarMensaje("Turno del jugador:" + jugadores.get(turno).getNombre());
			numeroElegido = jugadores.get(turno).eligeCarta(cartasAElegir, entrada);
			Carta cartaElegida = cartasAElegir.get(numeroElegido);
			boolean pudoInsertar = jugadores.get(turno).insertaEnTablero(cartaElegida, entrada);
			int coordenadaX = cartaElegida.getFichas()[0].getColumna();
			int coordenadaY = cartaElegida.getFichas()[0].getFila();
			if (pudoInsertar) {
				ventana.actualizarTablero(turno, coordenadaY, coordenadaX);
			}
			cartasAElegir.set(numeroElegido, null);
			nuevoOrdenDeTurnos.put(numeroElegido, turno);
		}
		turnos.clear();
		for (Map.Entry<Integer, Integer> entry : nuevoOrdenDeTurnos.entrySet())
			turnos.add(entry.getValue());

		if (turnos.size() != nuevoOrdenDeTurnos.size())
			System.out.println("check");
	}

	private List<Integer> determinarTurnosIniciales() {
		List<Integer> idJugadores = new ArrayList<Integer>(4);

		for (int i = 0; i < cantidadJugadores; i++) {
			idJugadores.add(i);
		}
		Collections.shuffle(idJugadores);

		return idJugadores;
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

}
