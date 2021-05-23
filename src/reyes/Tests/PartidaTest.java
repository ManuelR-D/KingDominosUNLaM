package reyes.Tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import reyes.Carta;
import reyes.KingDominoExcepcion;
import reyes.Partida;

public class PartidaTest {

//	@Test
//	public void partidaDe3Jugadores() throws KingDominoExcepcion {
//		int cantidadJugadores = 3;
//		int cantidadCartas = 48;
//		int tamTablero = 5;
//		Partida p = new Partida(cantidadJugadores, tamTablero, cantidadCartas); // 3 jug
//		assertEquals(true, p.iniciarPartida());
//	}

//	@Test
//	public void partidaDe4Jugadores() throws KingDominoExcepcion {
//		int cantidadJugadores = 4;
//		int cantidadCartas = 48;
//		int tamTablero = 5;
//		Partida p = new Partida(cantidadJugadores, tamTablero, cantidadCartas); // 4 jug
//		assertEquals(true, p.iniciarPartida());
//	}

	@Test(expected = KingDominoExcepcion.class)
	public void partidaDe5Jugadores() throws KingDominoExcepcion {
		int cantidadJugadores = 5;
		int cantidadCartas = 48;
		int tamTablero = 5;
		Partida p = new Partida(cantidadJugadores, tamTablero, cantidadCartas); // 4 jug
	}

	@Test(expected = KingDominoExcepcion.class)
	public void partidaDe1Jugador() throws KingDominoExcepcion {
		int cantidadJugadores = 1;
		int cantidadCartas = 48;
		int tamTablero = 5;
		Partida p = new Partida(cantidadJugadores, tamTablero, cantidadCartas); // 4 jug
	}

	@Test(expected = KingDominoExcepcion.class)
	public void partidaIlegalCartas() throws KingDominoExcepcion {
		int cantidadJugadores = 4;
		int cantidadCartas = 49;
		int tamTablero = 5;
		Partida p = new Partida(cantidadJugadores, tamTablero, cantidadCartas + 1);
		// es invalido != 48 cartas
		// aunque en el futuro podríamos añadir modos con diferente cantidad de cartas.
		// el código ya está preparado para eso.
	}

//	@Test
//	public void elGranDuelo() throws KingDominoExcepcion {
//		int cantidadJugadores = 2;
//		int cantidadCartas = 48;
//		int tamTablero = 7;
//		Partida p = new Partida(cantidadJugadores, tamTablero, cantidadCartas);
//		assertEquals(true, p.iniciarPartida());
//	}

//	@Test
//	public void iniciarPartidaDefault() {
//		Partida p = new Partida();
//		// paritda por defecto, 2 jugadores, 5x5, 48 cartas
//		assertEquals(true, p.iniciarPartida());
//	}

	@Test
	public void partidaModelo() throws KingDominoExcepcion {
	}
}
