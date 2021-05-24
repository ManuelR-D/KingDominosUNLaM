package reyes;

import java.util.List;
import java.util.Scanner;

public class Jugador extends Usuario{
	public String nombre;
	public Tablero tablero;

	public Jugador(String nombre, int tamTablero) {
		super(nombre,"");
		this.nombre = nombre;
		this.tablero = new Tablero(tamTablero);
	}
	public Jugador(Usuario user) {
		super(user);
		this.nombre = user.nombreUsuario;
	}

	public Jugador(String nombre, String contrasenia) {
		super(nombre,contrasenia);
	}
	public String getNombre() {
		return nombre;
	}

	public void insertaEnTablero(Carta carta, Scanner entrada) {
		if (!tablero.esPosibleInsertarEnTodoElTablero(carta)) {
			System.out.println("No es posible insertar la carta");
			System.out.println(carta);
			System.out.println("En el tablero");
			System.out.println(tablero);
			System.out.println("El jugador pierde el turno!!");
			return;
		}
		int x, y;
		int tamTablero = tablero.getTamanio() - 1;
		do {
			char opcion = 0;
			do {
				System.out.println("Tablero actual:");
				System.out.println(tablero);
				if (opcion == 's' || opcion == 'S')
					carta.rotarCarta();
				System.out.println("Desea rotar la carta?(s/n):");
				System.out.println(carta);
				opcion = entrada.next().charAt(0);
			} while (opcion == 's' || opcion == 'S');


			String cadena = "Inserte x[" + -tamTablero + "/" + tamTablero + "]:";
			x = FuncionesGenerales.intXTecladoEntre(-tamTablero, tamTablero, cadena, entrada);
			cadena = "Inserte y[" + -tamTablero + "/" + tamTablero + "]:";
			y = FuncionesGenerales.intXTecladoEntre(-tamTablero, tamTablero, cadena, entrada);

		} while (!tablero.ponerCarta(carta, x, y,true));
		System.out.println("Tablero actualizado:");
		System.out.println(tablero);

	}

	public int eligeCarta(List<Carta> cartasAElegir, Scanner entrada) {
		int cartaElegida = 0;
		do {
			for (int i = 0; i < cartasAElegir.size(); i++) {
				Carta c = cartasAElegir.get(i);
				if (c != null) {
					System.out.println((i + 1) + ":");
					System.out.println(c);
				}
			}
			cartaElegida = FuncionesGenerales.intXTecladoEntre(0, cartasAElegir.size(), "Elija una carta:", entrada);
		}while(cartasAElegir.get(cartaElegida-1) == null);

		return cartaElegida - 1;
	}

	public int getCantTerrenoColocado() {
		return tablero.getCantTerrenoColocado();
	}
}
