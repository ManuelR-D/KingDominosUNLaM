package reyes;

import java.io.Serializable;

public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2007699419196672147L;
	private String nombreUsuario;
	private String contrasenia;
	private int idUsuario;
	private Sala salaActual;
//	private int cantVictorias;
//	private int cantDerrotas;
//	private int cantPartidasJugadas;
//	private int cantDominosJugados; 
//	private int puntajeMaximo; 

	public Usuario(String nombreUsuario, String contrasenia) {
		this.nombreUsuario = nombreUsuario;
		this.contrasenia = contrasenia;
		this.idUsuario = 1;
	}

	public Usuario(Usuario user) {
		this.nombreUsuario = user.nombreUsuario;
		this.contrasenia = user.contrasenia;
		this.idUsuario = user.idUsuario;
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
		salaActual.aniadirJugadorASala(this);
	}

	public void verEstadistica(Usuario other) {

	}

	@Override
	public String toString() {
		return nombreUsuario + "," + contrasenia;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public Sala getSalaActual() {
		return salaActual;
	}
}
