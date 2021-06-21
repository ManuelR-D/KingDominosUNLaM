package reyes;

public class Ficha {
	private String Tipo;
	private int fila, columna, cantCoronas;
	private Carta carta;
	private int id;
	private int rotacion = 1;
	private boolean puntajeContado=false;
//	private static int fichas = 0;
	
	
	public Ficha(String tipo, int cantCoronas, int x, int y, int idFicha, Carta c) {
		Tipo = tipo;
		this.cantCoronas = cantCoronas;
		this.fila=x;
		this.columna=y;
//		fichas++;
		this.id = idFicha;
		this.carta = c;
	}
	public int getFila() {
		return fila;
	}
	public void setFila(int x) {
		this.fila = x;
	}
	public int getColumna() {
		return columna;
	}
	public void setColumna(int y) {
		this.columna = y;
	}
	public Carta getCarta() {
		return carta;
	}
	public int getId() {
		return id;
	}
	public void setRotacion(int rotacion) {
		this.rotacion = rotacion;
	}
	
	public String getTipo() {
		return Tipo;
	}
	public int getCantCoronas() {
		return cantCoronas;
	}
	public int getRotacion() {
		return rotacion;
	}
	
	
	@Override
	public String toString() {
		return Tipo + "/" + cantCoronas;
	}
	public boolean isPuntajeContado() {
		return puntajeContado;
	}
	public void setPuntajeContado(boolean puntajeContado) {
		this.puntajeContado = puntajeContado;
	}
	
	
}
