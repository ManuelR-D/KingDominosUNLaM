package reyes.Tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import reyes.Carta;
import reyes.Mazo;

public class MazoTest {

	@Test
	public void armarMazo() {
		Mazo mazo = new Mazo(48);

		assertEquals(48, mazo.getTam());
	}

	@Test
	public void mezclarMazo() {
		Mazo mazo = new Mazo(48);

		mazo.mezclarMazo();
		assertEquals(48, mazo.getTam());
	}

	@Test
	public void quitarNCartasDelMazo() {
		Mazo mazo = new Mazo(48);
		List<Carta> cartasOrdenadas = new ArrayList<Carta>();
		
		mazo.mezclarMazo();

		int i = 0;
		int nCartas = 4;
		int tamanioOriginalMazo = mazo.getTam();
		while (mazo.getTam() >= 1) {
			mazo.quitarPrimerasNCartas(nCartas, cartasOrdenadas);
			i++;
		}
		assertEquals(tamanioOriginalMazo / nCartas, i);
	}
}
