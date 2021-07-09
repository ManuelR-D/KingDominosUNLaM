package netcode;

import java.io.Serializable;
import java.util.List;

import reyes.Mazo;

public class MensajeEstadoPartida implements Serializable {


	private static final long serialVersionUID = -5428551470677977825L;
	private Mazo mazoMezcladoDePartida;
	private List<Integer> turnosIniciales;
	private String[] configuracion;
	
	public MensajeEstadoPartida(Mazo mazo, String[] configuracion, List<Integer> turnos) {
		this.mazoMezcladoDePartida=mazo;
		this.configuracion=configuracion;
		this.turnosIniciales=turnos;
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
	public String[] getConfiguracion() {
		return configuracion;
	}

	
	
	
}
