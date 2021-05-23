package reyes;

import java.util.List;

public class Jugador {
	public String nombre;
	public Tablero tablero;
	
	public Jugador(String nombre, int tamTablero) {
		this.nombre = nombre;
		this.tablero = new Tablero(tamTablero);
	}
	
	public String getNombre() {
		return nombre;
	}

	/*public void eligeCarta(Carta cartaElegida, int numeroAleatorio) {
		System.out.println(this.nombre+" elige la carta "+numeroAleatorio);

	}*/

	public void insertaEnTablero(Carta cartaElegida) {
		if(!tablero.esPosibleInsertarEnTodoElTablero(cartaElegida)) {
			/*System.out.println("No es posible insertar la carta");
			System.out.println(cartaElegida);
			System.out.println("En el tablero");
			System.out.println(tablero);
			System.out.println("El jugador pierde el turno!!");*/
			return;
		}
		int tamTablero=tablero.getTamanio();
		int x = FuncionesGenerales.numeroAleatorioEntre(-(tamTablero-1), tamTablero-1);
		int y = FuncionesGenerales.numeroAleatorioEntre(-(tamTablero-1), tamTablero-1);
		int rotaciones = 0;
		while(tablero.ponerCarta(cartaElegida, x, y) == false) {
			//probamos todas las rotaciones posibles
			cartaElegida.rotarCarta();
			rotaciones++;
			if(rotaciones == 3) {
				rotaciones = 0;
				//si fallamos en todas las rotaciones, cambiamos de posicion
				x = FuncionesGenerales.numeroAleatorioEntre(-(tamTablero-1), tamTablero-1);
				y = FuncionesGenerales.numeroAleatorioEntre(-(tamTablero-1), tamTablero-1);
				cartaElegida.rotarCarta();
			}
		}
		//System.out.println(this.nombre + "\n" + cartaElegida + "\n" + tablero);
	}

	public int eligeCarta(List<Carta> cartasAElegir) {
		//TODO: Prompt al jugador preguntando qué carta quiere elegir de las disponibles
		int numeroElegido;
		do {
			numeroElegido = FuncionesGenerales.numeroAleatorioEntre(0, cartasAElegir.size()-1);
		}while(cartasAElegir.get(numeroElegido) == null);
		return numeroElegido;
	}
	
	public int getCantTerrenoColocado() {
		return tablero.getCantTerrenoColocado();
	}
}
