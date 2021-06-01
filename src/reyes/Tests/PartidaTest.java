package reyes.Tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import reyes.Bot;
import reyes.Jugador;
import reyes.KingDominoExcepcion;
import reyes.Partida;

public class PartidaTest {

/*	@Test
	public void partidaDe3Jugadores() throws KingDominoExcepcion {
		int cantidadCartas = 48;
		int tamTablero = 5;
		List<Jugador> jugadores = new ArrayList<Jugador>(3);
		jugadores.add(new Bot("Jugador 1",tamTablero));
		jugadores.add(new Bot("Jugador 2",tamTablero));
		jugadores.add(new Bot("BotTest!",tamTablero));
		Partida p = new Partida(jugadores, tamTablero, cantidadCartas); // 3 jug
		assertEquals(true, p.iniciarPartida());
	}

	@Test
	public void partidaDe4Jugadores() throws KingDominoExcepcion {
		int cantidadCartas = 48;
		int tamTablero = 5;
		List<Jugador> jugadores = new ArrayList<Jugador>(4);
		jugadores.add(new Bot("Jugador 1",tamTablero));
		jugadores.add(new Bot("Jugador 2",tamTablero));
		jugadores.add(new Bot("Jugador 3",tamTablero));
		jugadores.add(new Bot("BotTest!",tamTablero));
		Partida p = new Partida(jugadores, tamTablero, cantidadCartas); // 4 jug
		assertEquals(true, p.iniciarPartida());
	}

	@Test(expected = KingDominoExcepcion.class)
	public void partidaDe5Jugadores() throws KingDominoExcepcion {
		int cantidadCartas = 48;
		int tamTablero = 5;
		List<Jugador> jugadores = new ArrayList<Jugador>(5);
		jugadores.add(new Bot("Jugador 1",tamTablero));
		jugadores.add(new Bot("Jugador 2",tamTablero));
		jugadores.add(new Bot("Jugador 3",tamTablero));
		jugadores.add(new Bot("Jugador 4",tamTablero));
		jugadores.add(new Bot("BotTest!",tamTablero));
		Partida p = new Partida(jugadores, tamTablero, cantidadCartas); // 5 jug
	}

	@Test(expected = KingDominoExcepcion.class)
	public void partidaDe1Bot() throws KingDominoExcepcion {
		int cantidadCartas = 48;
		int tamTablero = 5;
		List<Jugador> jugadores = new ArrayList<Jugador>(5);
		jugadores.add(new Bot("BotTest!",tamTablero));
		Partida p = new Partida(jugadores, tamTablero, cantidadCartas); // 1 jug
	}

	@Test(expected = KingDominoExcepcion.class)
	public void partidaIlegalCartas() throws KingDominoExcepcion {
		int cantidadCartas = 49;
		int tamTablero = 5;
		List<Jugador> jugadores = new ArrayList<Jugador>(2);
		jugadores.add(new Bot("Jugador 1",tamTablero));
		jugadores.add(new Bot("Jugador 2",tamTablero));
		Partida p = new Partida(jugadores, tamTablero, cantidadCartas + 1);
		// es invalido != 48 cartas
		// aunque en el futuro podríamos añadir modos con diferente cantidad de cartas.
		// el código ya está preparado para eso.
	}
*/
	@Test
	public void elGranDuelo() throws KingDominoExcepcion, IOException {
		int cantidadCartas = 48;
		int tamTablero = 7;
		List<Jugador> jugadores = new ArrayList<Jugador>(2);
		jugadores.add(new Bot("BotTest!",tamTablero));
		jugadores.add(new Bot("Jugador 1",tamTablero));
		
		Partida p = new Partida(jugadores, tamTablero, cantidadCartas);
		assertEquals(true, p.iniciarPartida());
	}
/*
	@Test
	public void iniciarPartidaDefault() throws KingDominoExcepcion {
		int cantidadCartas = 48;
		int tamTablero = 5;
		List<Jugador> jugadores = new ArrayList<Jugador>(2);
		jugadores.add(new Jugador("Jugador 1",tamTablero));
		jugadores.add(new Jugador("Jugador 2",tamTablero));
		
		Partida p = new Partida(jugadores, tamTablero, cantidadCartas);
		// paritda por defecto, 2 jugadores, 5x5, 48 cartas
		assertEquals(true, p.iniciarPartida());
	}

	@Test
	public void partidaHumanoVsBot() throws KingDominoExcepcion {
		int cantidadCartas = 48;
		int tamTablero = 5;
		List<Jugador> jugadores = new ArrayList<Jugador>(2);
		jugadores.add(new Bot("BotTest!",tamTablero));
		jugadores.add(new Jugador("Jugador 1",tamTablero));
		
		Partida p = new Partida(jugadores, tamTablero, cantidadCartas);
		assertEquals(true, p.iniciarPartida());
	}*/
}
