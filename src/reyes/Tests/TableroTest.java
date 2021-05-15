package reyes.Tests;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import reyes.Ficha;
import reyes.Tablero;

public class TableroTest {

	@Test
	public void test() {
		Ficha f1=new Ficha("Oasis",1,0,0);
		Ficha f2=new Ficha("Oasis",0,0,1);
		Ficha f3=new Ficha("Mina",2,0,2);
		Ficha f4=new Ficha("Pradera",0,1,0);
		Ficha f5=new Ficha("Oasis",2,1,1);
		Ficha f6=new Ficha("Oasis",3,1,2);
		Ficha f7=new Ficha("Pradera",0,2,0);
		Ficha f8=new Ficha("Pradera",1,2,1);
		Ficha f9=new Ficha("Mina",0,2,2);
		Tablero t=new Tablero(3);
		
		t.tablero[0][0]=f1;
		t.tablero[0][1]=f2;
		t.tablero[0][2]=f3;
		t.tablero[1][0]=f4;
		t.tablero[1][1]=f5;
		t.tablero[1][2]=f6;
		t.tablero[2][0]=f7;
		t.tablero[2][1]=f8;
		t.tablero[2][2]=f9;
		
		int puntajeEsperado=29;
		
		assertEquals(puntajeEsperado, t.puntajeTotal());
		
	}

}
