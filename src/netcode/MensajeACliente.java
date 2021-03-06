package netcode;

import java.io.Serializable;
import java.util.List;

public class MensajeACliente implements Serializable{




	private static final long serialVersionUID = -3270450619107272291L;
	private String texto;
	private List<Sala> listaSalas;
	private Sala sala;
	private int tipo;
	MensajeEstadoPartida estado = null;
	public MensajeACliente(String texto, List<Sala> salas, int tipo) {
		this.texto = texto;
		this.listaSalas = salas;
		this.tipo = tipo;
	}
	public MensajeACliente(String texto, int tipo,Sala sala) {
		this.texto = texto;
		this.sala = sala;
		this.tipo = tipo;
	}
	public MensajeACliente(String texto, int tipo, Sala salaActual, MensajeEstadoPartida estado) {
		this.texto = texto;
		this.sala = salaActual;
		this.tipo = tipo;
		this.estado = estado;
	}
	public String getTexto() {
		return texto;
	}
	
	public Sala getSala() {
		return sala;
	}
	
	public List<Sala> getSalas() {
		return listaSalas;
	}
	
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public MensajeEstadoPartida getEstado() {
		return estado;
	}
	@Override
	public String toString() {
		return "MensajeACliente [mensaje=" + texto + ", sala=" + sala + ", tipo=" + tipo + "]";
	}
	

}
