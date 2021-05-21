package reyes.Tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import reyes.Carta;
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

		List<String> ret = t.puntajeTotal();		
		assertEquals("PUNTAJE TOTAL:29", ret.get(ret.size()-1));
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

		List<String> ret = t.puntajeTotal();

		assertEquals("PUNTAJE TOTAL:55", ret.get(ret.size()-1));

	}
	
	@Test
	public void testTableroPonerCarta() {
		Carta c1 = new Carta(1,"Oasis",0,"Oasis",0);
		Carta c2 = new Carta(2,"Mina",2,"Pradera",0);
		Carta c3 = new Carta(3,"Oasis",2,"Oasis",3);
		Carta c4 = new Carta(4,"Pradera",0,"Pradera",1);
		Carta c5 = new Carta(5,"Mina",0,"Oasis",1);
		Carta c6 = new Carta(6,"Mina",0,"Pradera",0);
		Carta c7 = new Carta(7,"Bosque",3,"Pradera",0);
		Carta c8 = new Carta(8,"Bosque",3,"Bosque",3);
		Tablero t = new Tablero(5);
		List<String> res=new ArrayList<String>();
		res.add("1-Bosque=27 puntos.\n");
		res.add("2-Oasis=30 puntos.\n");
		res.add("3-Mina=2 puntos.\n");
		res.add("4-Pradera=2 puntos.\n");
		res.add("PUNTAJE TOTAL:61");
		
		
		assertEquals(true, t.ponerCarta(c1, -2, 0));
		assertEquals(true,t.ponerCarta(c2, 1, 0));
		assertEquals(true,t.ponerCarta(c3, 0, 1));
		assertEquals(true,t.ponerCarta(c4, 0, -1));
		assertEquals(true,t.ponerCarta(c5, -2, 1));
		assertEquals(true,t.ponerCarta(c6, -2, 2));
		c7.rotarCarta();
		c7.rotarCarta();
		assertEquals(true,t.ponerCarta(c7, 1, 2));
		c8.rotarCarta();
		assertEquals(true,t.ponerCarta(c8, 2, 2));
		
		
		List<String> ret = t.puntajeTotal();
		for (int i = 0; i < res.size(); i++) {
			assertEquals(ret.get(i), res.get(i));
		}
		
		//Insertamos donde ya hay
		assertEquals(false, t.ponerCarta(c1, -2, 0));
		//Posici�n ilegal (no adyacente a nadie)
		assertEquals(false, t.ponerCarta(c1, -4, -4));
		//Posici�n ilegal (extensi�n del castillo demasiado grande)
		assertEquals(false, t.ponerCarta(c1, -4, 0));
		//Posici�n ilegal (no hay adyacente compatible)
		assertEquals(false, t.ponerCarta(c8, -2, -2));
	}
	@Test
	public void testTableroQuitarCarta() {
		Carta c1 = new Carta(1,"Oasis",0,"Oasis",0);
		Carta c2 = new Carta(2,"Mina",2,"Pradera",0);
		Carta c3 = new Carta(3,"Oasis",2,"Oasis",3);
		Carta c4 = new Carta(4,"Pradera",0,"Pradera",1);
		Carta c5 = new Carta(5,"Mina",0,"Oasis",1);
		Carta c6 = new Carta(6,"Mina",0,"Pradera",0);
		Carta c7 = new Carta(7,"Bosque",3,"Pradera",0);
		Carta c8 = new Carta(8,"Bosque",3,"Bosque",3);
		Tablero t = new Tablero(5);
		
		
		assertEquals(true, t.ponerCarta(c1, -2, 0));
		assertEquals(true,t.ponerCarta(c2, 1, 0));
		assertEquals(true,t.ponerCarta(c3, 0, 1));
		assertEquals(true,t.ponerCarta(c4, 0, -1));
		assertEquals(true,t.ponerCarta(c5, -2, 1));
		assertEquals(true,t.ponerCarta(c6, -2, 2));
		c7.rotarCarta();
		c7.rotarCarta();
		assertEquals(true,t.ponerCarta(c7, 1, 2));
		c8.rotarCarta();
		assertEquals(true,t.ponerCarta(c8, 2, 2));
		
		t.quitarCarta(c8);
		t.quitarCarta(c7);
		t.quitarCarta(c6);
		t.quitarCarta(c5);
		t.quitarCarta(c4);
		t.quitarCarta(c3);
		t.quitarCarta(c2);
		t.quitarCarta(c1);
		List<String> ret = t.puntajeTotal();
		assertEquals("PUNTAJE TOTAL:0", ret.get(ret.size()-1));
	}

}
