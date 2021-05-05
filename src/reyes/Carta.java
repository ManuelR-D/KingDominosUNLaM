package reyes;

public class Carta {
	private int idCarta;
	private String tipoIzq;
	private int cantCoronasI;
	private String tipoDer;
	private int cantCoronasD;
	private boolean usada = false;
	
	public Carta(int idCarta, String tipoIzq, int cantCoronasI, String tipoDer, int cantCoronasD) {
		this.idCarta = idCarta;
		this.tipoIzq = tipoIzq;
		this.cantCoronasI = cantCoronasI;
		this.tipoDer = tipoDer;
		this.cantCoronasD = cantCoronasD;
	}

}
