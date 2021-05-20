package reyes.Tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import reyes.Carta;
import reyes.Partida;

public class PartidaTest {

	@Test
	public void armarMazo() {
		Partida p = new Partida();
		List<Carta> mazo = p.armarMazo();

		assertEquals(48, mazo.size());
	}

	@Test
	public void mezclarMazo() {
		Partida p = new Partida();
		List<Carta> mazo = p.armarMazo();

		mazo = p.mezclarMazo(mazo);
		assertEquals(48, mazo.size());
	}

	@Test
	public void quitarNCartasDelMazo() {
		Partida p = new Partida();
		List<Carta> mazo = p.armarMazo();
		List<Carta> cartasOrdenadas = new ArrayList<Carta>();

		int i = 0;
		int nCartas = 4;
		int tamanioOriginalMazo = mazo.size();
		while (mazo.size() >= 1) {
			p.quitarNCartasDelMazo(mazo, nCartas, cartasOrdenadas);
			i++;
		}
		assertEquals(tamanioOriginalMazo/nCartas, i);
	}
	
	@Test
	public void iniciarPartida() {
		int cantidadJugadores = 3;
		int cantidadCartas = 48;
		int tamTablero = 5;
		Partida p = new Partida();
		//paritda por defecto, 2 jugadores, 5x5, 48 cartas
		assertEquals(true, p.iniciarPartida());
		p = new Partida(cantidadJugadores,tamTablero,cantidadCartas); //3 jug
		assertEquals(true, p.iniciarPartida());
		cantidadJugadores++;
		p = new Partida(cantidadJugadores,tamTablero,cantidadCartas); // 4 jug
		assertEquals(true, p.iniciarPartida());
		
		cantidadJugadores++;
		p = new Partida(cantidadJugadores,tamTablero,cantidadCartas); // 5 jug
		assertEquals(false, p.iniciarPartida()); //es invalido >4 jugadores
		cantidadJugadores = 1;
		p = new Partida(cantidadJugadores,tamTablero,cantidadCartas);
		assertEquals(false, p.iniciarPartida()); //es invalido <2 jugadores
		p = new Partida(cantidadJugadores,tamTablero,cantidadCartas+1);
		assertEquals(false, p.iniciarPartida()); //es invalido != 48 cartas
		//aunque en el futuro podríamos añadir modos con diferente cantidad de cartas.
		//el código ya está preparado para eso.
		
		//El gran duelo
		cantidadJugadores = 2;
		tamTablero = 7;
		cantidadCartas = 48;
		p = new Partida(cantidadJugadores,tamTablero,cantidadCartas);
		assertEquals(true, p.iniciarPartida());
	}

}
