package reyes.Tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import reyes.Carta;
import reyes.Ficha;

public class CartaTest {

	@Test
	public void rotarCarta1() {
		Carta c = new Carta(0, "Pradera", 0, "Oasis", 2);

		//		Carta original
		int f1xOriginal = c.getFichas()[0].getFila();
		int f1yOriginal = c.getFichas()[0].getColumna();
		int f2xOriginal = c.getFichas()[1].getFila();
		int f2yOriginal = c.getFichas()[1].getColumna();

		//		Carta rotada 1 vez
		c.rotarCarta();

		int f1xActual = c.getFichas()[0].getFila();
		int f1yActual = c.getFichas()[0].getColumna();
		int f2xActual = c.getFichas()[1].getFila();
		int f2yActual = c.getFichas()[1].getColumna();

		assertEquals(f1xOriginal, f1xActual);
		assertEquals(f1yOriginal, f1yActual);
		assertEquals(f2xOriginal, f2xActual - 1);
		assertEquals(f2yOriginal, f2yActual + 1);
	}

	@Test
	public void rotarCarta2() {
		Carta c = new Carta(0, "Pradera", 0, "Oasis", 2);

		//		Carta original
		int f1xOriginal = c.getFichas()[0].getFila();
		int f1yOriginal = c.getFichas()[0].getColumna();
		int f2xOriginal = c.getFichas()[1].getFila();
		int f2yOriginal = c.getFichas()[1].getColumna();

		//		Carta rotada 2 veces
		c.rotarCarta();
		c.rotarCarta();

		int f1xActual = c.getFichas()[0].getFila();
		int f1yActual = c.getFichas()[0].getColumna();
		int f2xActual = c.getFichas()[1].getFila();
		int f2yActual = c.getFichas()[1].getColumna();

		assertEquals(f1xOriginal, f1xActual);
		assertEquals(f1yOriginal, f1yActual);
		assertEquals(f2xOriginal, f2xActual);
		assertEquals(f2yOriginal, f2yActual + 2);
	}

	@Test
	public void rotarCarta3() {
		Carta c = new Carta(0, "Pradera", 0, "Oasis", 2);

		// Carta original
		int f1xOriginal = c.getFichas()[0].getFila();
		int f1yOriginal = c.getFichas()[0].getColumna();
		int f2xOriginal = c.getFichas()[1].getFila();
		int f2yOriginal = c.getFichas()[1].getColumna();

		// Carta rotada 3 veces
		c.rotarCarta();
		c.rotarCarta();
		c.rotarCarta();

		int f1xActual = c.getFichas()[0].getFila();
		int f1yActual = c.getFichas()[0].getColumna();
		int f2xActual = c.getFichas()[1].getFila();
		int f2yActual = c.getFichas()[1].getColumna();

		assertEquals(f1xOriginal, f1xActual);
		assertEquals(f1yOriginal, f1yActual);
		assertEquals(f2xOriginal, f2xActual + 1);
		assertEquals(f2yOriginal, f2yActual + 1);
	}

	@Test
	public void rotarCarta4() {
		Carta c = new Carta(0, "Pradera", 0, "Oasis", 2);

		// Carta original
		int f1xOriginal = c.getFichas()[0].getFila();
		int f1yOriginal = c.getFichas()[0].getColumna();
		int f2xOriginal = c.getFichas()[1].getFila();
		int f2yOriginal = c.getFichas()[1].getColumna();

		//		Carta rotada 1 vez
		c.rotarCarta();


		//		Carta rotada 2 veces
		c.rotarCarta();


		//		Carta rotada 3 veces
		c.rotarCarta();


		//		Carta rotada 4 veces
		c.rotarCarta();

		int f1xActual = c.getFichas()[0].getFila();
		int f1yActual = c.getFichas()[0].getColumna();
		int f2xActual = c.getFichas()[1].getFila();
		int f2yActual = c.getFichas()[1].getColumna();
		assertEquals(f1xOriginal, f1xActual);
		assertEquals(f1yOriginal, f1yActual);
		assertEquals(f2xOriginal, f2xActual);
		assertEquals(f2yOriginal, f2yActual);
	}

	@Test
	public void moverCarta1() {
		Carta c1 = new Carta(0, "Pradera", 0, "Oasis", 2);

		c1.moverCarta(0, 0);
		Ficha[] fichas = c1.getFichas();
		assertEquals(0, fichas[0].getFila());
		assertEquals(0, fichas[0].getColumna());
		assertEquals(0, fichas[1].getFila());
		assertEquals(1, fichas[1].getColumna());

	}

	@Test
	public void moverCarta2() {
		Carta c1 = new Carta(0, "Pradera", 0, "Oasis", 2);

		c1.moverCarta(5, 5);
		Ficha[] fichas = c1.getFichas();
		assertEquals(5, fichas[0].getFila());
		assertEquals(5, fichas[0].getColumna());
		assertEquals(5, fichas[1].getFila());
		assertEquals(6, fichas[1].getColumna());
	}


	@Test
	public void moverCarta4() {
		Carta c1 = new Carta(0, "Pradera", 0, "Oasis", 2);

		c1.moverCarta(5, 5);

		c1.moverCarta(-2, -3);
		Ficha[] fichas = c1.getFichas();
		assertEquals(3, fichas[0].getFila());
		assertEquals(2, fichas[0].getColumna());
		assertEquals(3, fichas[1].getFila());
		assertEquals(3, fichas[1].getColumna());
	}

}
