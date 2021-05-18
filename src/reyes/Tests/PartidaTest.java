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
		while (mazo.size() >= 1) {
			p.quitarNCartasDelMazo(mazo, 4, cartasOrdenadas);
			i++;
		}
		assertEquals(12, i);
	}
	
	@Test
	public void iniciarPartida() {
		Partida p = new Partida();
		
		p.iniciarPartida();
	}

}
