package reyes;

import java.util.Scanner;

public class Usuario {
	public String nombreUsuario;
	private String contraseña;
	private int idUsuario;
	public Sala salaActual;
	public int cantVictorias;
	public int cantDerrotas;
	public int cantPartidasJugadas;
	public int cantDominosJugados; 
	public int puntajeMaximo; 
	
	public Usuario(String nombreUsuario, String contraseña) {
		this.nombreUsuario = nombreUsuario;
		this.contraseña = contraseña;
		this.idUsuario = 1;
	}
	public Usuario(Usuario user) {
		this.nombreUsuario = user.nombreUsuario;
		this.contraseña = user.contraseña;
		this.idUsuario = user.idUsuario;
	}
	public Sala crearSala() {
		System.out.println("Ingrese nombre de sala a crear");
		Scanner in = new Scanner(System.in);
		String nombreDeSala = in.nextLine();
		Sala nuevaSala = new Sala(nombreDeSala);
		this.salaActual = nuevaSala;
		nuevaSala.añadirJugadorASala(new Jugador(this));
		return nuevaSala;
		
	}
	public void salirSala() {
		salaActual.quitarJugadorDeSala(this);
		this.salaActual = null;
	}
	public void unirseSala(Sala salaActual) {
		this.salaActual = salaActual;
		salaActual.añadirJugadorASala(this);
	}
	public void verEstadistica(Usuario other) {
		
	}
	@Override
	public String toString() {
		return "Usuario: " + nombreUsuario;
	}
}
