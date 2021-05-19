package reyes.Tests;

import org.junit.Test;

import reyes.Carta;

public class CartaTest {

	@Test
	public void test() {
//		Pradera
//		0
//		Oasis
//		2
//		Mina
//		2
//		Campo
//		0

		Carta c1= new Carta(0,"Pradera",0,"Oasis",2);
		System.out.println("Carta original");
		System.out.println(c1);
		
		System.out.println();
		System.out.println("Carta rotada 1 vez");
		c1.rotarCarta();
		System.out.println(c1);
		
		System.out.println();
		System.out.println("Carta rotada 2 veces");
		c1.rotarCarta();
		System.out.println(c1);
		
		
		System.out.println();
		System.out.println("Carta rotada 3 veces");
		c1.rotarCarta();
		System.out.println(c1);
		
		System.out.println();
		System.out.println("Carta rotada 4 veces (original)");
		c1.rotarCarta();
		System.out.println(c1);
	}

}
