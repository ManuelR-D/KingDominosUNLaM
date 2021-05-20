package reyes;


public class Jugador {
	private String nombre;
//	private int cantReyes; No se usa en esta implementacion
	public Tablero tablero;
	
	public Jugador(String nombre, int tamTablero) {
		this.nombre = nombre;
		this.tablero = new Tablero(tamTablero);
	}

	public void eligeCarta(Carta cartaElegida, int numeroAleatorio) {
		System.out.println(this.nombre+" elige la carta "+numeroAleatorio);

	}

	public void insertaEnTablero(Carta cartaElegida) {
		// TODO Auto-generated method stub
		if(!tablero.esPosibleInsertar(cartaElegida)) {
			System.out.println("No es posible insertar la carta");
			System.out.println(cartaElegida);
			System.out.println("En el tablero");
			System.out.println(tablero);
			System.out.println("El jugador pierde el turno!!");
			return;
		}
		int x = (int) Math.floor(Math.random() * (4 - (-4) + 1) - 4);
		int y = (int) Math.floor(Math.random() * (4 - (-4) + 1) - 4);
		int rotaciones = 0;
		while(tablero.ponerCarta(cartaElegida, x, y) == false) {
			//probamos todas las rotaciones posibles
			cartaElegida.rotarCarta();
			rotaciones++;
			if(rotaciones == 3) {
				rotaciones = 0;
				//si fallamos en todas las rotaciones, cambiamos de posicion
				x = (int) Math.floor(Math.random() * (4 - (-4) + 1) - 4);
				y = (int) Math.floor(Math.random() * (4 - (-4) + 1) - 4);
				cartaElegida.rotarCarta();
			}
		}
	}
}
