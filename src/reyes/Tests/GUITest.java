package reyes.Tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import reyes.Bot;
import reyes.Jugador;
import reyes.KingDominoExcepcion;
import reyes.Partida;

public class GUITest {
	public static void main(String[] args) throws KingDominoExcepcion, IOException {
		int cantidadCartas = 48;
		int tamTablero = 5;
		List<Jugador> jugadores = new ArrayList<Jugador>(4);
		jugadores.add(new Bot("BotTest!", tamTablero));
		jugadores.add(new Bot("Jugador 1", tamTablero));
		//jugadores.add(new Jugador("Jugador 1", tamTablero));
		//jugadores.add(new Jugador("Jugador 1", tamTablero));
		jugadores.add(new Bot("Jugador 2", tamTablero));
		jugadores.add(new Bot("Jugador 3", tamTablero));

		Partida p = new Partida(jugadores, tamTablero, cantidadCartas);
		p.iniciarPartida();
	}
}
