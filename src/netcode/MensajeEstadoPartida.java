package netcode;

import java.io.Serializable;
import java.util.List;

import reyes.Mazo;

public class MensajeEstadoPartida implements Serializable {

	private Mazo mazoMezcladoDePartida;
	private List<Integer> turnosIniciales;
	public MensajeEstadoPartida(Mazo m, List<Integer> turnos) {
		mazoMezcladoDePartida = m;
		turnosIniciales = turnos;
	}
	@Override
	public String toString() {
		return "MensajeEstadoPartida [mazoMezcladoDePartida=" + mazoMezcladoDePartida + ", turnosIniciales="
				+ turnosIniciales + "]";
	}
	public Mazo getMazoMezcladoDePartida() {
		return mazoMezcladoDePartida;
	}
	public List<Integer> getTurnosIniciales() {
		return turnosIniciales;
	}
	

	
	
	
}
