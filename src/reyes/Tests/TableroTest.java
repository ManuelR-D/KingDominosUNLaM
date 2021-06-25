package reyes.Tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import reyes.Carta;
import reyes.Tablero;

public class TableroTest {

	@Test
	public void testPuntajeTotal() {
		
		Tablero t = new Tablero(5);
		
		Carta c1 = new Carta(1,"Oasis",1,"Oasis",0);
		t.ponerCarta(c1, 2, 4, false,null);
		Carta c2= new Carta(2,"Pradera",0,"Pradera",1);
		t.ponerCarta(c2, 4, 3, false,null);
		Carta c3= new Carta(3,"Oasis",2,"Oasis",3);
		t.ponerCarta(c3, 3, 5, false,null);
		Carta c4= new Carta(4,"Bosque",3,"Pradera",0);
		t.ponerCarta(c4, 2, 3, false,null);
		/*
		 *     Bosque/3| Pradera/0| Pradera/0| Pradera/1|
   			 	Oasis/1|   Oasis/0|Castillo/0|           |
          			   |   Oasis/2|   Oasis/3|           |
		 */
		
		int ret = t.puntajeTotal(false);
		assertEquals(30, ret);

	}

	@Test
	public void testPuntajeTotal2() {
		Tablero t = new Tablero(5);
		
		Carta c1 = new Carta(1,"Pradera",0,"Oasis",2);
		t.ponerCarta(c1, 2, 4, false,null);
		Carta c2= new Carta(2,"Mina",0,"Bosque",3);
		t.ponerCarta(c2, 4, 3, false,null);
		Carta c3= new Carta(3,"Mina",2,"Pradera",0);
		t.ponerCarta(c3, 4, 5, false,null);
		Carta c4= new Carta(4,"Pradera",0,"Pradera",1);
		t.ponerCarta(c4, 2, 3, false,null);
		Carta c5= new Carta(5,"Oasis",1,"Oasis",0);
		t.ponerCarta(c5, 2, 5, false,null);
		Carta c6= new Carta(6,"Oasis",1,"Mina",0);
		t.ponerCarta(c6, 2, 6, false,null);

/*
 * 
 *  Pradera/0| Pradera/1|    Mina/0|  Bosque/3|
 	Pradera/0|   Oasis/2|Castillo/0|          |
   	Oasis/1  |   Oasis/0|    Mina/2| Pradera/0|
   	Oasis/1  |    Mina/0|          |          |
 */
		int ret = t.puntajeTotal(false);

		assertEquals(24, ret);

	}

	@Test
	public void testTableroPonerCarta() {
		Carta c1 = new Carta(1, "Oasis", 0, "Oasis", 0);
		Carta c2 = new Carta(2, "Mina", 2, "Pradera", 0);
		Carta c3 = new Carta(3, "Oasis", 2, "Oasis", 3);
		Carta c4 = new Carta(4, "Pradera", 0, "Pradera", 1);
		Carta c5 = new Carta(5, "Mina", 0, "Oasis", 1);
		Carta c6 = new Carta(6, "Mina", 0, "Pradera", 0);
		Carta c7 = new Carta(7, "Bosque", 3, "Pradera", 0);
		Carta c8 = new Carta(8, "Bosque", 3, "Bosque", 3);
		Tablero t = new Tablero(5);

//		List<String> res = new ArrayList<String>();
//		res.add("1-Bosque=27 puntos.\n");
//		res.add("2-Oasis=30 puntos.\n");
//		res.add("3-Mina=2 puntos.\n");
//		res.add("4-Pradera=2 puntos.\n");
//		res.add("PUNTAJE TOTAL:61");

		assertEquals(true, t.ponerCarta(c1, 2, 4,false,null));
		assertEquals(true, t.ponerCarta(c2, 5, 4,false,null));
		assertEquals(true, t.ponerCarta(c3, 4, 3,false,null));
		assertEquals(true, t.ponerCarta(c4, 4, 5,false,null));
		assertEquals(true, t.ponerCarta(c5, 2, 3,false,null));
		assertEquals(true, t.ponerCarta(c6, 2, 2,false,null));
		c7.rotarCarta();
		c7.rotarCarta();
		assertEquals(true, t.ponerCarta(c7, 5, 2,false,null));
		c8.rotarCarta();
		assertEquals(true, t.ponerCarta(c8, 6, 2,false,null));
		
		/*
		 *     	Mina/0 | Pradera/0| Pradera/0|  Bosque/3|  Bosque/3|
    			Mina/0 |   Oasis/1|   Oasis/2|   Oasis/3|  Bosque/3|
   				Oasis/0|   Oasis/0|Castillo/0|    Mina/2| Pradera/0|
          			   |          | Pradera/0| Pradera/1|          |
		 */

		int ret = t.puntajeTotal(false);
		assertEquals(61, ret);

		// Insertamos donde ya hay
		assertEquals(false, t.ponerCarta(c1, -2, 0,false,null));
		// Posici�n ilegal (no adyacente a nadie)
		assertEquals(false, t.ponerCarta(c1, -4, -4,false,null));
		// Posici�n ilegal (extensi�n del castillo demasiado grande)
		assertEquals(false, t.ponerCarta(c1, -4, 0,false,null));
		// Posici�n ilegal (no hay adyacente compatible)
		assertEquals(false, t.ponerCarta(c8, -2, -2,false,null));
	}

	@Test
	public void testTableroQuitarCarta() {
		Carta c1 = new Carta(1, "Oasis", 0, "Oasis", 0);
		Carta c2 = new Carta(2, "Mina", 2, "Pradera", 0);
		Carta c3 = new Carta(3, "Oasis", 2, "Oasis", 3);
		Carta c4 = new Carta(4, "Pradera", 0, "Pradera", 1);
		Carta c5 = new Carta(5, "Mina", 0, "Oasis", 1);
		Carta c6 = new Carta(6, "Mina", 0, "Pradera", 0);
		Carta c7 = new Carta(7, "Bosque", 3, "Pradera", 0);
		Carta c8 = new Carta(8, "Bosque", 3, "Bosque", 3);
		Tablero t = new Tablero(5);

		assertEquals(true, t.ponerCarta(c1, 2, 4, false,null));
		assertEquals(true, t.ponerCarta(c2, 5, 4, false,null));
		assertEquals(true, t.ponerCarta(c3, 4, 5, false,null));
		assertEquals(true, t.ponerCarta(c4, 4, 3, false,null));
		assertEquals(true, t.ponerCarta(c5, 2, 5, false,null));
		assertEquals(true, t.ponerCarta(c6, 2, 6, false,null));
		c7.rotarCarta();
		c7.rotarCarta();
		assertEquals(true, t.ponerCarta(c7, 5, 6, false,null));
		c8.rotarCarta();
		assertEquals(true, t.ponerCarta(c8, 6, 6, false,null));
		/*
		 *     	Mina/0 | Pradera/0| Pradera/0|  Bosque/3|  Bosque/3|
    			Mina/0 |   Oasis/1|   Oasis/2|   Oasis/3|  Bosque/3|
   				Oasis/0|   Oasis/0|Castillo/0|    Mina/2| Pradera/0|
          			   |          | Pradera/0| Pradera/1|          |
		 */
		
		t.quitarCarta(c8);
		t.quitarCarta(c7);
		t.quitarCarta(c6);
		t.quitarCarta(c5);
		t.quitarCarta(c4);
		t.quitarCarta(c3);
		t.quitarCarta(c2);
		t.quitarCarta(c1);
		
		int ret = t.puntajeTotal(false);
		assertEquals(0, ret);
	}

}
