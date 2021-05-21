package reyes.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import reyes.Carta;
import reyes.Ficha;

public class CartaTest {

	@Test
	public void rotarCarta1() {
		Carta c = new Carta(0, "Pradera", 0, "Oasis", 2);

		//		Carta original
		int f1xOriginal = c.getFichas()[0].getX();
		int f1yOriginal = c.getFichas()[0].getY();
		int f2xOriginal = c.getFichas()[1].getX();
		int f2yOriginal = c.getFichas()[1].getY();

		//		Carta rotada 1 vez
		c.rotarCarta();

		int f1xActual = c.getFichas()[0].getX();
		int f1yActual = c.getFichas()[0].getY();
		int f2xActual = c.getFichas()[1].getX();
		int f2yActual = c.getFichas()[1].getY();

		assertEquals(f1xOriginal, f1xActual);
		assertEquals(f1yOriginal, f1yActual);
		assertEquals(f2xOriginal, f2xActual - 1);
		assertEquals(f2yOriginal, f2yActual + 1);
	}

	@Test
	public void rotarCarta2() {
		Carta c = new Carta(0, "Pradera", 0, "Oasis", 2);

		//		Carta original
		int f1xOriginal = c.getFichas()[0].getX();
		int f1yOriginal = c.getFichas()[0].getY();
		int f2xOriginal = c.getFichas()[1].getX();
		int f2yOriginal = c.getFichas()[1].getY();

		//		Carta rotada 2 veces
		c.rotarCarta();
		c.rotarCarta();

		int f1xActual = c.getFichas()[0].getX();
		int f1yActual = c.getFichas()[0].getY();
		int f2xActual = c.getFichas()[1].getX();
		int f2yActual = c.getFichas()[1].getY();

		assertEquals(f1xOriginal, f1xActual);
		assertEquals(f1yOriginal, f1yActual);
		assertEquals(f2xOriginal, f2xActual);
		assertEquals(f2yOriginal, f2yActual + 2);
	}

	@Test
	public void rotarCarta3() {
		Carta c = new Carta(0, "Pradera", 0, "Oasis", 2);

		// Carta original
		int f1xOriginal = c.getFichas()[0].getX();
		int f1yOriginal = c.getFichas()[0].getY();
		int f2xOriginal = c.getFichas()[1].getX();
		int f2yOriginal = c.getFichas()[1].getY();

		// Carta rotada 3 veces
		c.rotarCarta();
		c.rotarCarta();
		c.rotarCarta();

		int f1xActual = c.getFichas()[0].getX();
		int f1yActual = c.getFichas()[0].getY();
		int f2xActual = c.getFichas()[1].getX();
		int f2yActual = c.getFichas()[1].getY();

		assertEquals(f1xOriginal, f1xActual);
		assertEquals(f1yOriginal, f1yActual);
		assertEquals(f2xOriginal, f2xActual + 1);
		assertEquals(f2yOriginal, f2yActual + 1);
	}

	@Test
	public void rotarCarta4() {
		Carta c = new Carta(0, "Pradera", 0, "Oasis", 2);

		// Carta original
		int f1xOriginal = c.getFichas()[0].getX();
		int f1yOriginal = c.getFichas()[0].getY();
		int f2xOriginal = c.getFichas()[1].getX();
		int f2yOriginal = c.getFichas()[1].getY();

		//		Carta rotada 1 vez
		c.rotarCarta();


		//		Carta rotada 2 veces
		c.rotarCarta();


		//		Carta rotada 3 veces
		c.rotarCarta();


		//		Carta rotada 4 veces
		c.rotarCarta();

		int f1xActual = c.getFichas()[0].getX();
		int f1yActual = c.getFichas()[0].getY();
		int f2xActual = c.getFichas()[1].getX();
		int f2yActual = c.getFichas()[1].getY();
		assertEquals(f1xOriginal, f1xActual);
		assertEquals(f1yOriginal, f1yActual);
		assertEquals(f2xOriginal, f2xActual);
		assertEquals(f2yOriginal, f2yActual);
	}

	@Test
	public void moverCarta1() {
		Carta c1 = new Carta(0, "Pradera", 0, "Oasis", 2);

//		c1.moverCarta(0, 0, 9);
		c1.moverCarta(0, 0);
		Ficha[] fichas = c1.getFichas();
		assertEquals(0, fichas[0].getX());
		assertEquals(0, fichas[0].getY());
		assertEquals(0, fichas[1].getX());
		assertEquals(1, fichas[1].getY());

	}

	@Test
	public void moverCarta2() {
		Carta c1 = new Carta(0, "Pradera", 0, "Oasis", 2);

//		c1.moverCarta(5, 5, 9);
		c1.moverCarta(5, 5);
		Ficha[] fichas = c1.getFichas();
		assertEquals(5, fichas[0].getX());
		assertEquals(5, fichas[0].getY());
		assertEquals(5, fichas[1].getX());
		assertEquals(6, fichas[1].getY());
	}

//	@Test
//	public void moverCarta3() {
//		Carta c1 = new Carta(0, "Pradera", 0, "Oasis", 2);
//
//		assertFalse(c1.moverCarta(10, 10, 9));
//	}

	@Test
	public void moverCarta4() {
		Carta c1 = new Carta(0, "Pradera", 0, "Oasis", 2);

//		c1.moverCarta(5, 5, 9);
		c1.moverCarta(5, 5);

//		c1.moverCarta(-2, -3, 9);
		c1.moverCarta(-2, -3);
		Ficha[] fichas = c1.getFichas();
		assertEquals(3, fichas[0].getX());
		assertEquals(2, fichas[0].getY());
		assertEquals(3, fichas[1].getX());
		assertEquals(3, fichas[1].getY());
	}

}
