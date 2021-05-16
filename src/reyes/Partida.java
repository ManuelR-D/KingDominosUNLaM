package reyes;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class Partida {
	private List<Carta> cartasTotales;
	private Jugador[] jugadores;
	private final static int TAMAÑO_TABLERO = 5;
	private final static int CANTIDAD_CARTAS = 48;
	private final static int CANTIDAD_JUGADORES= 2;
	
	//TODO: determinar turnos iniciales a partir de dados
	//TODO: iniciarPartida	
	public Partida() {
		this.jugadores = new Jugador[CANTIDAD_JUGADORES];
		
		//Creamos a cada jugador
		for (int i = 0; i < jugadores.length; i++) {
			jugadores[i] = new Jugador("Jugador "+i, TAMAÑO_TABLERO);
		}
		armarMazo();
		mezclarMazo();
		
		iniciarPartida();

	}
	public List<Carta> getCartasTotales() {
		return cartasTotales;
	}
	
	private void mezclarMazo() {
		List<Carta> cartasMezcladas= new ArrayList<Carta>(CANTIDAD_CARTAS);
		for(int i=0;i<CANTIDAD_CARTAS;i++) {
			int numeroAleatorio=(int) (Math.random() * (CANTIDAD_CARTAS-i));
			cartasMezcladas.add(cartasTotales.remove(numeroAleatorio));
		}
		cartasTotales=cartasMezcladas;		
	}
	private void armarMazo() {
		this.cartasTotales = new ArrayList<Carta>(CANTIDAD_CARTAS);
		File file = new File("./assets/cartas.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("No se encontro el archivo de cartas");
			return;
		}
		int idCarta = 1;
		while(scanner.hasNextLine()) {
			String tipoIzq = scanner.nextLine();
			int    cantCoronasI = Integer.parseInt(scanner.nextLine());
			String tipoDer = scanner.nextLine();
			int    cantCoronasD = Integer.parseInt(scanner.nextLine());
			cartasTotales.add(new Carta(idCarta, tipoIzq, cantCoronasI, tipoDer, cantCoronasD));
			idCarta++;
		}
		scanner.close();
	}

	public void iniciarPartida() {
		int[] turnos = determinarTurnosIniciales();
		Set<Carta> cartas4=new TreeSet<Carta>();
		quitarNCartasDelMazo(4,cartas4);
		mostrarNCartas(4,cartas4);
	}
	private void mostrarNCartas(int n, Set<Carta> cartas) {
		for(Carta carta:cartas) {
			carta.mostrarCarta();
		}
	}
	private void quitarNCartasDelMazo(int n,Set<Carta> cartasN) {
		for(int i=0;i<n;i++) {
			cartasN.add(cartasTotales.remove(0));
		}
		
	}
	private int[] determinarTurnosIniciales() {
		int[] turnos=new int[CANTIDAD_JUGADORES];
		List<Integer>idJugadores=new ArrayList<Integer>(4);
		
		for(int i=0;i<CANTIDAD_JUGADORES;i++) {
			idJugadores.add(i);
		}
		for(int i=0;i<CANTIDAD_JUGADORES;i++) {
			int numeroAleatorio=(int) (Math.random() * (CANTIDAD_JUGADORES-i));
			turnos[i]=idJugadores.remove(numeroAleatorio);
		}
		
		return turnos;
	}
}
