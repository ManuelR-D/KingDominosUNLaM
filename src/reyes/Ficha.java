package reyes;

public class Ficha {
	String Tipo;
	int x, y, cantCoronas;
	public Ficha(String tipo, int cantCoronas, int x, int y) {
		Tipo = tipo;
		this.cantCoronas = cantCoronas;
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public String getTipo() {
		return Tipo;
	}
	public int getCantCoronas() {
		return cantCoronas;
	}
	
	
}
