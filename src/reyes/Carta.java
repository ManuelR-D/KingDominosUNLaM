package reyes;

public class Carta implements Comparable<Carta> {
	private int idCarta;
	private int x = 0, y = 0;
	private Ficha[] fichas = new Ficha[2];
	private int rotacion = 1;

	public Carta(int idCarta, String tipoIzq, int cantCoronasI, String tipoDer, int cantCoronasD) {
		this.idCarta = idCarta;
		fichas[0] = new Ficha(tipoIzq, cantCoronasI, x, y);
		fichas[1] = new Ficha(tipoDer, cantCoronasD, x, y + 1);
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
	}

	public void mostrarCarta() {
		Ficha ficha1 = fichas[0];
		Ficha ficha2 = fichas[1];

		switch (rotacion) {
		case 1:
			System.out.printf("id:%d --", this.idCarta);
			System.out.printf("[%s/%d coronas]---", ficha1.getTipo(), ficha1.getCantCoronas());
			System.out.printf("[%s/%d coronas]\n", ficha2.getTipo(), ficha2.getCantCoronas());
			break;
		case 2:
			System.out.printf("id:%d --", this.idCarta);
			System.out.printf("[%s/%d coronas]\n", ficha2.getTipo(), ficha2.getCantCoronas());
			System.out.println("\t|");
			System.out.printf("[%s/%d coronas]\n", ficha1.getTipo(), ficha1.getCantCoronas());
			break;
		case 3:
			System.out.printf("id:%d --", this.idCarta);
			System.out.printf("[%s/%d coronas]---", ficha2.getTipo(), ficha2.getCantCoronas());
			System.out.printf("[%s/%d coronas]\n", ficha1.getTipo(), ficha1.getCantCoronas());
			break;
		case 4:
			System.out.printf("id:%d --", this.idCarta);
			System.out.printf("[%s/%d coronas]\n", ficha1.getTipo(), ficha1.getCantCoronas());
			System.out.println("\t|");
			System.out.printf("[%s/%d coronas]\n", ficha2.getTipo(), ficha2.getCantCoronas());
			break;
		}

	}

	@Override
	public int compareTo(Carta otraCarta) {
		return this.idCarta - otraCarta.idCarta;
	}

}
