package reyes;

import java.io.Serializable;

public class Ficha implements Serializable {
	private String Tipo;
	private int fila, columna, cantCoronas;
	private Carta carta;
	private int id;
	private int rotacion = 1;
	private boolean puntajeContado=false;
	
	
	public Ficha(String tipo, int cantCoronas, int fila, int columna, int idFicha, Carta c) {
		Tipo = tipo;
		this.cantCoronas = cantCoronas;
		this.fila=fila;
		this.columna=columna;
		this.id = idFicha;
		this.carta = c;
	}
	public int getFila() {
		return fila;
	}
	public void setFila(int fila) {
		this.fila = fila;
	}
	public int getColumna() {
		return columna;
	}
	public void setColumna(int columna) {
		this.columna = columna;
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
	public void setCarta(Carta carta2) {
		this.carta = carta2;
	}
	
	
}
