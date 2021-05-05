package reyes;

import java.util.ArrayList;
import java.util.List;

public class Sala {
	private int idSala;
	private String nombreSala;
	private List<Usuario> jugadoresEnSala = new ArrayList<Usuario>();
	
	public Sala(String nombreSala) {
		this.nombreSala = nombreSala;
	}
	public Sala(String nombreSala, int idSala) {
		this.nombreSala = nombreSala;
		this.idSala = idSala;
	}
	public void mostrarJugadoresEnSala() {
		for (Usuario usuario : jugadoresEnSala) {
			System.out.println(usuario);
		}
	}
	public void añadirJugadorASala(Usuario user) {
		jugadoresEnSala.add(user);
	}
	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}
	public int getCantJugadoresEnSala() {
		return jugadoresEnSala.size();
	}
	@Override
	public String toString() {
		return "Sala [" + idSala + "]" + nombreSala +" Jugadores en la sala: " + jugadoresEnSala.size();
	}
	public void crearPartida() {
		Partida partida = new Partida(this);
		partida.iniciarPartida();
		
	}
	public void quitarJugadorDeSala(Usuario usuario) {
		jugadoresEnSala.remove(usuario);
	}
	public Usuario getJugador(int i) {
		return jugadoresEnSala.get(i);
	}
	
	
}
