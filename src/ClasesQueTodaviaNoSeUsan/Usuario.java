package ClasesQueTodaviaNoSeUsan;

import java.util.Scanner;

public class Usuario {
	private String nombreUsuario;
	private String contrase�a;
	private int idUsuario;
	public Sala salaActual;
	public int cantVictorias;
	public int cantDerrotas;
	public int cantPartidasJugadas;
	public int cantDominosJugados; 
	public int puntajeMaximo; 
	
	public Usuario(String nombreUsuario, String contrase�a) {
		this.nombreUsuario = nombreUsuario;
		this.contrase�a = contrase�a;
		this.idUsuario = 1;
	}
	public Sala crearSala() {
		System.out.println("Ingrese nombre de sala a crear");
		Scanner in = new Scanner(System.in);
		String nombreDeSala = in.nextLine();
		Sala nuevaSala = new Sala(nombreDeSala);
		this.salaActual = nuevaSala;
		nuevaSala.a�adirJugadorASala(this);
		return nuevaSala;
		
	}
	public void salirSala() {
		salaActual.quitarJugadorDeSala(this);
		this.salaActual = null;
	}
	public void unirseSala(Sala salaActual) {
		this.salaActual = salaActual;
		salaActual.a�adirJugadorASala(this);
	}
	public void verEstadistica(Usuario other) {
		
	}
	@Override
	public String toString() {
		return "Usuario: " + nombreUsuario;
	}
}
