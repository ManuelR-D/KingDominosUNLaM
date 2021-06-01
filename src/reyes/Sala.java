package reyes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Sala {
	private int idSala;
	private String nombreSala;
	private List<Jugador> jugadoresEnSala = new ArrayList<Jugador>();
	
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
		jugadoresEnSala.add(new Jugador(user));
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
	public void crearPartida() throws KingDominoExcepcion, IOException {
		Partida partida = new Partida(jugadoresEnSala);
		partida.iniciarPartida();
	}
	public void quitarJugadorDeSala(Usuario usuario) {
		for (Jugador jugador : jugadoresEnSala) {
			if(jugador.getNombreUsuario() == usuario.getNombreUsuario())
				this.jugadoresEnSala.remove(jugador);
		}
	}
	public Usuario getJugador(int i) {
		return jugadoresEnSala.get(i);
	}
	
	
}
