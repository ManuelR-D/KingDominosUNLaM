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
		int x = -4;
		int y = -4;
		int rotaciones = 0;
		while(tablero.ponerCarta(cartaElegida, x, y) == false && y < 10) {
			//probamos todas las rotaciones posibles
			cartaElegida.rotarCarta();
			rotaciones++;
			if(rotaciones == 3) {
				rotaciones = 0;
				//si fallamos en todas las rotaciones, cambiamos de posicion
				if(x<tablero.getTamanio() - 1)
					x++;
				else {
					x = -4;
					y++;
				}
				cartaElegida.rotarCarta();
			}
		}
		if(y >= 10) {
			//si fallamos todas las rotaciones y todas las posiciones, tenemos un error
			System.err.println("No hay posiciones validas para esta carta");
			System.out.println(cartaElegida);
			System.err.println("En este tablero");
			System.out.println(tablero);
			System.err.println("El jugador pierde el turno!");
		}else
			System.out.println(cartaElegida);
	}
}
