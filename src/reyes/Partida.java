package reyes;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.print.DocFlavor.URL;

import ClasesQueTodaviaNoSeUsan.Sala;
public class Partida {
	private Sala salaDeJugadores;
	private Carta[] cartasTotales = new Carta[CANTIDAD_CARTAS];
	private Jugador[] castillos;
//	private final static int REYES_DE_PARTIDA = 1;
	private final static int TAMAÑO_TABLERO = 5;
	private final static int CANTIDAD_CARTAS = 48;
	
	//TODO: determinar turnos iniciales a partir de dados
	//TODO: iniciarPartida	
	public Partida(Sala sala) {
		this.salaDeJugadores = sala;
		this.castillos = new Jugador[sala.getCantJugadoresEnSala()];
		//asignamos el castillo a cada jugador
		for (int i = 0; i < castillos.length; i++) {
			castillos[i] = new Jugador(sala.getJugador(i), TAMAÑO_TABLERO);
		}
		//armamos el mazo con las cartas TODO: se podrían pasar parametros para variar la cantidad de cartas, no es complejo
		armarMazo();

	}
	
	private void armarMazo() {
		this.cartasTotales = new Carta[CANTIDAD_CARTAS];
		File file = new File("./assets/cartas.txt");
		Scanner scanner;
		File directory = new File("./");
		System.out.println(directory.getAbsolutePath());
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
			cartasTotales[idCarta-1] = new Carta(idCarta, tipoIzq, cantCoronasI, tipoDer, cantCoronasD);
			idCarta++;
		}
		scanner.close();
	}

	public void iniciarPartida() {
		int[] turnos = determinarTurnos();
		
	}
	private int[] determinarTurnos() {
		int[][] ordenDeTurnos = new int[salaDeJugadores.getCantJugadoresEnSala()][2];
		for (int i = 0; i < salaDeJugadores.getCantJugadoresEnSala(); i++) {
			ordenDeTurnos[i][0] = i;
			ordenDeTurnos[i][1] = (int) (Math.random() * 7);
		}
		for (int j = 1; j < ordenDeTurnos.length-1; j++) {
			for (int i = 0; i < ordenDeTurnos.length-j; i++) {
				if (ordenDeTurnos[i][1] < ordenDeTurnos[i+1][1]) {
					int jugador = ordenDeTurnos[i+1][0];
					int dado = ordenDeTurnos[i+1][1];
					ordenDeTurnos[i+1][0] = ordenDeTurnos[i][0];
					ordenDeTurnos[i+1][1] = ordenDeTurnos[i][1];
					ordenDeTurnos[i][0] = jugador;
					ordenDeTurnos[i][1] = dado;
				}
			}	
		}
		int[] ret = new int[salaDeJugadores.getCantJugadoresEnSala()];
		int i = 0;
		for (int[] jugador : ordenDeTurnos) {
			ret[i++] = jugador[0];
		}
		return ret;
	}

}
