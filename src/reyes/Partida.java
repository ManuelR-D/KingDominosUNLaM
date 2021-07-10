package reyes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import SwingApp.VentanaJueguito;
import netcode.HiloCliente;

public class Partida {
	private static Mazo mazoInicial = null;
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
	private Map<Jugador, Integer> puntajes = new HashMap<Jugador, Integer>();
	private boolean reinoMedio = false;
	private boolean armonia = false;
	private boolean isMazoMezclado = false;
	private static boolean esTurnoJugadorLocal = false;
	private static String jugadorLocal;

	public static CountDownLatch mtxEsperarPaquete = new CountDownLatch(1);
	public static String paquete;

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

	/*
	 * El formato de variante es: {mazo}|{variante1}|{variante2}|{varianteN}
	 */
	public boolean iniciarPartida(String variante, String tituloVentana) throws IOException {
		System.out.println(variante);
		armonia = variante.contains("Armonia");
		reinoMedio = variante.contains("ReinoMedio");
		if (variante.equals("N"))
			mazo = new Mazo(cantidadCartas, variante.substring(0, variante.indexOf("|")));
		else
			mazo = new Mazo(cantidadCartas, "original"); // si no especificaron variante, usamos la original

		if (variante.contains("Dinastia")) {
			Map<String, Integer> sumatoriaPuntajes = new HashMap<String, Integer>();

			SortedSet<Map.Entry<String, Integer>> sortedset = new TreeSet<Map.Entry<String, Integer>>(
					(a, b) -> a.getValue().compareTo(b.getValue())); // lambda porque soy re fancy

			for (Jugador jugador : jugadores) {
				sumatoriaPuntajes.put(jugador.getNombreUsuario(), 0);
			}
			for (int i = 0; i < 3; i++) {

				iniciarPartida(tituloVentana);
				for (Map.Entry<Jugador, Integer> entry : puntajes.entrySet()) {
					sumatoriaPuntajes.put(entry.getKey().getNombreUsuario(),
							entry.getValue() + sumatoriaPuntajes.get(entry.getKey().getNombreUsuario()));
				}
				VentanaJueguito.mainFrame.dispose();
				mazo = new Mazo(cantidadCartas, variante.substring(0, variante.indexOf("|")));
			}
			sortedset.addAll(sumatoriaPuntajes.entrySet());
			System.out.println(sortedset);
			VentanaJueguito.mainFrame.mostrarVentanaMensaje("El ganador es: " + sortedset.last());
			VentanaJueguito.mainFrame.dispose();
		} else
			iniciarPartida(tituloVentana);
		return true;
	}

	public boolean iniciarPartida(String tituloVentana) throws IOException {

		List<Integer> turnos = determinarTurnosIniciales();
		List<Carta> cartasAElegirSig = new ArrayList<Carta>();
		// armamos y mezclamos el mazo
		if (!isMazoMezclado) {
			mazo.mezclarMazo();
			isMazoMezclado = true;

		}
		Partida.mazoInicial = new Mazo(this.mazo); // Deepcopy.
		// seteamos el tablero para cada jugador
		for (int i = 0; i < jugadores.size(); i++) {
			Jugador jugador = jugadores.get(i);
			jugador.setTablero(this.tamanioTablero);
			jugador.setIdCastilloCentro(i + 1);
		}

		ventana = new VentanaJueguito(this);
		ventana.setTitle(tituloVentana);
		if (textura != null) {
			ventana.cargarTextura(textura);
		}
		ventana.actualizarTableros();
		while (mazo.getTam() > 1) {
			mazo.quitarPrimerasNCartas(4, cartasAElegirSig);
			jugarRonda(cartasAElegirSig, turnos, ventana);

		}
		VentanaJueguito.setTurnoJugador(-1);
		ventana.setPSeleccionVisible(false);
		List<Integer> puntajesFinales = calcularPuntajesFinales(ventana);
		ventana.terminarPartida(determinarGanadores(puntajesFinales));
		
		return true;
	}

	private List<Integer> calcularPuntajesFinales(VentanaJueguito ventana) {
		ventana.deshabilitarBotonesPuntaje();
		List<Integer> puntajesFinales = new ArrayList<Integer>();
		for (int i = 0; i < jugadores.size(); i++) {
			Jugador jugador = jugadores.get(i);
			int puntaje = jugador.getTablero().puntajeTotal(true, ventana, i);
			if (reinoMedio && jugador.getTablero().estaCastilloEnMedio()) {
				ventana.mostrarVentanaMensaje("El jugador: " + jugador.getNombre() + " gana el bono de reinoMedio");
				puntaje += 10;
			}
			if (armonia && jugador.tieneReinoCompletamenteOcupado()) {
				ventana.mostrarVentanaMensaje("El jugador: " + jugador.getNombre() + " gana el bono de armonia");
				puntaje += 5;
			}
			puntajesFinales.add(puntaje);
			this.puntajes.put(jugador, puntaje);
		}
		ventana.habilitarBotonesPuntaje();
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
		ventana.mostrarVentanaMensaje("Empate por puntos, se define por cantidad de terreno colocado");
		// Si hay mas de un ganador por puntos, se define por cantidad de terreno
		int maxTerreno = 0;
		Map<Jugador, Integer> ganadoresPorTerreno = new HashMap<Jugador, Integer>();
		for (int i = 0; i < ganadoresPorPunto.size(); i++) {
			int cantTerreno = jugadores.get(i).getCantTerrenoColocado();
			cantTerreno *= 2;// se puede multiplicar o no por 2, ya que cada ficha tiene 2 "terrenos", me
								// parece mas claro asi, es indistinto.
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

		int numeroElegido = 0;
		Map<Integer, Integer> nuevoOrdenDeTurnos = new TreeMap<Integer, Integer>();

		for (int i = 0; i < turnos.size(); i++) {
			
			entrada.mostrarCartasAElegir(cartasAElegir);
			int turno = turnos.get(i);
			boolean pudoInsertar = false;
			String insercion = null;
			
			VentanaJueguito.setTurnoJugador(turno);
			Jugador jugadorTurnoActual = jugadores.get(turno);
			entrada.mostrarMensaje("Turno del jugador\n" + jugadorTurnoActual.getNombre());
			int coordenadaX = 0;
			int coordenadaY = 0;
			Carta cartaElegida = null;
			
			if (jugadorLocal.equals(jugadorTurnoActual.getNombre())) {
				// Si es el turno del jugador local, tiene que elegir su carta y posicion
				esTurnoJugadorLocal = true;
				numeroElegido = jugadorTurnoActual.eligeCarta(cartasAElegir, entrada);
				cartaElegida = cartasAElegir.get(numeroElegido);
				pudoInsertar = jugadorTurnoActual.insertaEnTablero(cartaElegida, entrada);
				insercion = pudoInsertar ? "S" : "N";
				coordenadaX = cartaElegida.getFichas()[0].getColumna();
				coordenadaY = cartaElegida.getFichas()[0].getFila();
				int rotacion=cartaElegida.getRotacion();
				crearPaquete(numeroElegido, coordenadaX, coordenadaY, pudoInsertar,rotacion);
			} else {
				// Sino, tiene que esperar a que el servidor informe lo que hizo el otro jugador
				esTurnoJugadorLocal = false;
				try {
					mtxEsperarPaquete.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mtxEsperarPaquete = new CountDownLatch(1);
				String[] paqueteActual = Partida.paquete.split(",");
				Partida.paquete = null;
				numeroElegido = Integer.valueOf(paqueteActual[0]);
				coordenadaX = Integer.valueOf(paqueteActual[1]);
				coordenadaY = Integer.valueOf(paqueteActual[2]);
				insercion = paqueteActual[4];
				int rotacion=Integer.parseInt(paqueteActual[5]);
				cartaElegida=cartasAElegir.get(numeroElegido);
				for(int j=1;j<rotacion;j++) {
					cartaElegida.rotarCarta();
				}
				
//				System.out.println("INSERCION" + insercion);
//				System.out.println("Paquete en crudo: " + paqueteActual);
//				System.out.println("Se leyo el paquete: " + numeroElegido + ";" + coordenadaX + ";" + coordenadaY + ";"
//						+ paqueteActual[3]);
				if (insercion.equals("S")) {
					jugadorTurnoActual.tablero.ponerCarta(cartaElegida, coordenadaX, coordenadaY,
							true, ventana);
				}
			}
			if (insercion.equals("S")) {
				System.out.println("Turno:"+turno+" y:"+coordenadaY+" x:"+coordenadaX);
				ventana.actualizarTablero(turno, coordenadaY, coordenadaX);
			}
			else {
				if(!jugadorLocal.equals(jugadorTurnoActual.getNombre())) {
					ventana.mostrarVentanaMensaje(jugadorTurnoActual.getNombre()+" no pudo insertar la carta");
				}
			}
			cartasAElegir.set(numeroElegido, null);
			nuevoOrdenDeTurnos.put(numeroElegido, turno);
		}
		turnos.clear();
		for (Map.Entry<Integer, Integer> entry : nuevoOrdenDeTurnos.entrySet())
			turnos.add(entry.getValue());
	}

	private void crearPaquete(int numeroElegido, int coordenadaX, int coordenadaY, boolean pudoInsertar, int rotacion) {
		String insercion = pudoInsertar ? "S" : "N";
		Partida.paquete = numeroElegido + "," + coordenadaX + "," + coordenadaY + "," + jugadorLocal + "," + insercion+","+rotacion;
		HiloCliente.mtxPaquetePartida.countDown(); // aviso a mi hilo que tiene preparado un paquete
	}

	private static List<Integer> turnosIniciales = null;

	private List<Integer> determinarTurnosIniciales() {
		if (Partida.turnosIniciales != null)
			return Partida.turnosIniciales;
		List<Integer> idJugadores = new ArrayList<Integer>(4);

		for (int i = 0; i < cantidadJugadores; i++) {
			idJugadores.add(i);
		}
		Collections.shuffle(idJugadores);
		turnosIniciales = idJugadores; // lo usa el servidor para sincronizar el estado inicial
		return idJugadores;
	}

	public static List<Integer> getTurnosIniciales() {
		return turnosIniciales;
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public void setReinoMedio(boolean b) {
		reinoMedio = b;
	}

	public static Mazo getMazo() {
		return mazoInicial;
	}

	public void setMazo(Mazo mazoMezcladoDePartida) {
		Partida.mazoInicial = mazoMezcladoDePartida;
		this.mazo = mazoMezcladoDePartida;
		isMazoMezclado = true;
	}

	public void setTurnosIniciales(List<Integer> turnosIniciales) {
		Partida.turnosIniciales = turnosIniciales;
	}

	public void setJugadorLocal(String nombre) {
		jugadorLocal = nombre;
	}

	public static boolean esTurnoJugadorLocal() {
		return esTurnoJugadorLocal;
	}
	
	public static String getJugadorLocal() {
		return jugadorLocal;
	}
}
