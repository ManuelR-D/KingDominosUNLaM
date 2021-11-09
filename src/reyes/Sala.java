package reyes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sala implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1273807392156990177L;
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

	public void aniadirJugadorASala(Usuario user) {
		jugadoresEnSala.add(new Jugador(user));
		System.out.println(jugadoresEnSala.size());
		if(jugadoresEnSala.size() == 4) {
			try {
				crearPartida();
			} catch (KingDominoExcepcion | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public int getCantJugadoresEnSala() {
		return jugadoresEnSala.size();
	}

	@Override
	public String toString() {
		return idSala + "," + nombreSala + "," + jugadoresEnSala.size();
	}

	public void crearPartida() throws KingDominoExcepcion, IOException {
		Partida partida = new Partida(jugadoresEnSala);
		partida.iniciarPartida("Creado por sala");
	}

	public void quitarJugadorDeSala(Usuario usuario) {
		for (Jugador jugador : jugadoresEnSala) {
			if (jugador.getNombreUsuario() == usuario.getNombreUsuario())
				this.jugadoresEnSala.remove(jugador);
		}
	}
	public List<Jugador> getJugadoresEnSala(){
		return this.jugadoresEnSala;
	}
	public int getId() {
		return this.idSala;
	}
	public Usuario getJugador(int i) {
		return jugadoresEnSala.get(i);
	}

}
