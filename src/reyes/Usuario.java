package reyes;

import java.util.Scanner;

public class Usuario {
	private String nombreUsuario;
	private String contraseña;
	private int idUsuario;
	private Sala salaActual;
//	private int cantVictorias;
//	private int cantDerrotas;
//	private int cantPartidasJugadas;
//	private int cantDominosJugados; 
//	private int puntajeMaximo; 
	
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
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public Sala getSalaActual() {
		return salaActual;
	}
}
