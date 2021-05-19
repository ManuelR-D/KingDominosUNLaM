package reyes.Tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import reyes.Ficha;
import reyes.Tablero;

public class TableroTest {

	@Test
	public void testPuntajeTotal() {
		Ficha f1 = new Ficha("Oasis", 1, 0, 0);
		Ficha f2 = new Ficha("Oasis", 0, 0, 1);
		Ficha f3 = new Ficha("Mina", 2, 0, 2);
		Ficha f4 = new Ficha("Pradera", 0, 1, 0);
		Ficha f5 = new Ficha("Oasis", 2, 1, 1);
		Ficha f6 = new Ficha("Oasis", 3, 1, 2);
		Ficha f7 = new Ficha("Pradera", 0, 2, 0);
		Ficha f8 = new Ficha("Pradera", 1, 2, 1);
		Ficha f9 = new Ficha("Mina", 0, 2, 2);
		Tablero t = new Tablero(3);
		Ficha[][] tablero = t.getTablero();

		tablero[0][0] = f1;
		tablero[0][1] = f2;
		tablero[0][2] = f3;
		tablero[1][0] = f4;
		tablero[1][1] = f5;
		tablero[1][2] = f6;
		tablero[2][0] = f7;
		tablero[2][1] = f8;
		tablero[2][2] = f9;

		int puntajeEsperado = 29;

		assertEquals(puntajeEsperado, t.puntajeTotal());
		System.out.println();

	}

	@Test
	public void testPuntajeTotal2() {
		Ficha f1 = new Ficha("Oasis", 1, 1, 0);
		Ficha f2 = new Ficha("Oasis", 0, 1, 1);
		Ficha f3 = new Ficha("Mina", 2, 1, 2);
		Ficha f4 = new Ficha("Pradera", 0, 2, 0);
		Ficha f5 = new Ficha("Oasis", 2, 2, 1);
		Ficha f6 = new Ficha("Oasis", 3, 2, 2);
		Ficha f7 = new Ficha("Pradera", 0, 3, 0);
		Ficha f8 = new Ficha("Pradera", 1, 3, 1);
		Ficha f9 = new Ficha("Mina", 0, 3, 2);
		Ficha f10 = new Ficha("Oasis", 1, 0, 0);
		Ficha f11 = new Ficha("Mina", 0, 0, 1);
		Ficha f12 = new Ficha("Pradera", 0, 0, 2);
		Ficha f13 = new Ficha("Bosque", 3, 0, 3);
		Ficha f14 = new Ficha("Pradera", 0, 1, 3);
		Ficha f15 = new Ficha("Bosque", 3, 2, 3);
		Ficha f16 = new Ficha("Bosque", 3, 3, 3);

		Tablero t = new Tablero(4);
		Ficha[][] tablero = t.getTablero();

		tablero[1][0] = f1;
		tablero[1][1] = f2;
		tablero[1][2] = f3;
		tablero[2][0] = f4;
		tablero[2][1] = f5;
		tablero[2][2] = f6;
		tablero[3][0] = f7;
		tablero[3][1] = f8;
		tablero[3][2] = f9;

		tablero[0][0] = f10;
		tablero[0][1] = f11;
		tablero[0][2] = f12;
		tablero[0][3] = f13;
		tablero[1][3] = f14;
		tablero[2][3] = f15;
		tablero[3][3] = f16;

		int puntajeEsperado2 = 55;

		assertEquals(puntajeEsperado2, t.puntajeTotal());

	}

}
