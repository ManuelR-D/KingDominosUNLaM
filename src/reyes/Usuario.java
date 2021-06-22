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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idUsuario;
		result = prime * result + ((nombreUsuario == null) ? 0 : nombreUsuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (idUsuario != other.idUsuario)
			return false;
		if (nombreUsuario == null) {
			if (other.nombreUsuario != null)
				return false;
		} else if (!nombreUsuario.equals(other.nombreUsuario))
			return false;
		return true;
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
