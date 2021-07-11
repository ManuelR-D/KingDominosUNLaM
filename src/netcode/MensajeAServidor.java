package netcode;

import java.io.Serializable;

public class MensajeAServidor implements Serializable{

	private static final long serialVersionUID = -5905903694983224221L;
	String texto;
	Sala sala;
	int tipo;
	MensajeEstadoPartida estado = null;
	public MensajeAServidor(String texto, Sala sala, int tipo) {
		this.texto = texto;
		this.sala = sala;
		this.tipo = tipo;
	}
	public MensajeAServidor(String texto, Sala sala, int tipo, MensajeEstadoPartida estado) {
		this.estado = estado;
		this.tipo = 11;
		this.sala = sala;
		this.texto = texto;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public Sala getSala() {
		return sala;
	}
	
	public int getTipo() {
		return tipo;
	}
	@Override
	public String toString() {
		return "MensajeAServidor [mensaje=" + texto + ", sala=" + sala + ", tipo=" + tipo + "]";
	}
	
	
	

}
