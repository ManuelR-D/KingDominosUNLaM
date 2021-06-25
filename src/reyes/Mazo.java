package reyes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Mazo {
	private List<Carta> cartas;

	public Mazo(int cantidadCartas) {
		armarMazo(cantidadCartas);
	}

	public void armarMazo(int cantidadCartas) {
		List<Carta> cartas = new ArrayList<Carta>(cantidadCartas);
		File file = new File("./assets/cartas2.txt");
		Scanner scanner;

		try {
			int idCarta = 1;
			scanner = new Scanner(file);
			while (idCarta <= cantidadCartas) {

				if (scanner.hasNextLine()) {
					String tipoIzq = scanner.nextLine();
					int cantCoronasI = Integer.parseInt(scanner.nextLine());
					String tipoDer = scanner.nextLine();
					int cantCoronasD = Integer.parseInt(scanner.nextLine());
					cartas.add(new Carta(idCarta, tipoIzq, cantCoronasI, tipoDer, cantCoronasD));
					idCarta++;
				} else {
					scanner.close(); // volvemos al inicio del archivo y seguimos cargando
					scanner = new Scanner(file);
				}
			}
			// si la cantidad de cartas no fuera multiplo de 48, podria suceder que scanner
			// siguiera abierto
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("No se encontro el archivo de cartas");
		}
		this.cartas = cartas;
	}

	public void mezclarMazo() {
		Collections.shuffle(cartas);
	}

	public void quitarPrimerasNCartas(int n, List<Carta> cartasDevueltas) {
		for (int i = 0; i < n; i++) {
			cartasDevueltas.add(this.cartas.remove(0));
		}

	}

	public List<Carta> getCartas() {
		return cartas;
	}

	public int getTam() {
		return cartas.size();
	}
}
