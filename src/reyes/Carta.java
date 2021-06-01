package reyes;

public class Carta implements Comparable<Carta> {
	public int idCarta;
	private Ficha[] fichas = new Ficha[2];
	private int rotacion = 1;

	public Carta(int idCarta, String tipoIzq, int cantCoronasI, String tipoDer, int cantCoronasD) {
		this.idCarta = idCarta;
		fichas[0] = new Ficha(tipoIzq, cantCoronasI, 0, 0,this.idCarta*2, this);
		fichas[1] = new Ficha(tipoDer, cantCoronasD, 0, 1,this.idCarta*2+1, this);
	}

	public void setDefault() {
		this.rotacion = 1;
		this.fichas[0].x = 0;
		this.fichas[0].y = 0;
		this.fichas[1].x = 0;
		this.fichas[1].y = 1;
	}

	public Ficha[] getFichas() {
		return fichas;
	}

	public void rotarCarta() {
		int x1 = fichas[0].getX();
		int y1 = fichas[0].getY();

		switch (rotacion) {
		case 1:
			fichas[1].setX(x1 + 1);
			fichas[1].setY(y1);
			break;
		case 2:
			fichas[1].setX(x1);
			fichas[1].setY(y1 - 1);
			break;
		case 3:
			fichas[1].setX(x1 - 1);
			fichas[1].setY(y1);
			break;
		case 4:
			rotacion = 0;
			fichas[1].setX(x1);
			fichas[1].setY(y1 + 1);
			break;
		}
		rotacion++;
		fichas[0].rotacion = rotacion;
		fichas[1].rotacion = rotacion;
	}

	@Override
	public String toString() {
		String ret = "";
		String[][] mat = { { "", "", "" }, { "", "", "" }, { "", "", "" } };
		Ficha ficha1 = fichas[0];
		Ficha ficha2 = fichas[1];
		mat[1][1] = ficha1.toString();
		// mat[ficha2.getX() + 1][ficha2.getY() + 1] = ficha2.toString();
		switch (rotacion) {
		case 1:
			mat[1][2] = ficha2.toString();
			break;
		case 2:
			mat[2][1] = ficha2.toString();
			break;
		case 3:
			mat[1][0] = ficha2.toString();
			break;
		case 4:
			mat[0][1] = ficha2.toString();
			break;
		}
		for (String[] strings : mat) {
			for (String strings2 : strings) {
				ret += String.format("%9s" + "|", strings2);
			}
			ret += "\n";
		}
		return ret;
	}

	@Override
	public int compareTo(Carta otraCarta) {
		return this.idCarta - otraCarta.idCarta;
	}

	public void moverCarta(int fila, int columna) {
		this.fichas[0].x += fila;
		this.fichas[0].y += columna;
		this.fichas[1].x += fila;
		this.fichas[1].y += columna;
	}

}
