package netcode;

import java.io.Serializable;

public class MensajeAServidor implements Serializable{

	private static final long serialVersionUID = -5905903694983224221L;
	private String texto;
	private Sala sala;
	private int tipo;
	private MensajeEstadoPartida estado = null;
	
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
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	@Override
	public String toString() {
		return "MensajeAServidor [mensaje=" + texto + ", sala=" + sala + ", tipo=" + tipo + "]";
	}
	public MensajeEstadoPartida getEstado() {
		return estado;
	}
	
	
	

}
