package ClasesQueTodaviaNoSeUsan;

import static org.junit.Assert.*;

import org.junit.Test;

public class SalaTest {

	@Test
	public void test1() {
		Usuario user1= new Usuario("user 1","1234");
		Usuario user2= new Usuario("user 2","12345");
		Usuario user3= new Usuario("user 3","12345");
		Usuario user4= new Usuario("user 4","12345");
		
		Sala sala1= new Sala("Sala nº 1",1);
		sala1.añadirJugadorASala(user1);
		sala1.añadirJugadorASala(user2);
		int cantReal=sala1.getCantJugadoresEnSala();
		assertEquals(2,cantReal);
		sala1.quitarJugadorDeSala(user2);
		sala1.añadirJugadorASala(user3);
		sala1.añadirJugadorASala(user4);
		cantReal=sala1.getCantJugadoresEnSala();
		assertEquals(3,cantReal);
	}
}
