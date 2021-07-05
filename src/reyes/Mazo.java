package reyes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Mazo implements Serializable {
	private List<Carta> cartas;

	public Mazo(int cantidadCartas, String nombreMazo) {
		armarMazo(cantidadCartas, nombreMazo);
	}

	public Mazo(int cantidadCartas) {
		cartas = new ArrayList<Carta>(cantidadCartas);
	}
	//Constructor de copia
	public Mazo(Mazo mazo) {
		cartas = new ArrayList<Carta>(mazo.getCartas().size());
		for (Carta carta : mazo.getCartas()) {
			cartas.add(new Carta(carta));
		}
	}

	public void armarMazo(int cantidadCartas, String nombreMazo) {
		List<Carta> cartas = new ArrayList<Carta>(cantidadCartas);
		File file = new File("./assets/"+nombreMazo+".txt");
		Scanner scanner;

		try {
			int idCarta = 1;
			scanner = new Scanner(file);
			while (idCarta <= cantidadCartas) {

				if (scanner.hasNextLine()) {
					String tipoIzq = scanner.nextLine();
					int cantCoronasI = Integer.parseInt(scanner.nextLine());
					int idFichaI = Integer.parseInt(scanner.nextLine());
					String tipoDer = scanner.nextLine();
					int cantCoronasD = Integer.parseInt(scanner.nextLine());
					int idFichaD = Integer.parseInt(scanner.nextLine());
					Ficha fI = new Ficha(tipoIzq, cantCoronasI, 0, 0, idFichaI, null);
					Ficha fD = new Ficha(tipoDer, cantCoronasD, 0, 1, idFichaD, null);
					cartas.add(new Carta(idCarta,fI,fD));
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
	
	@Override
	public String toString() {
		String ret = "";
		for (Carta carta : cartas) {
			Ficha[] fichas = carta.getFichas();
			ret = ret + fichas[0].getTipo() + "\n" + fichas[0].getCantCoronas() + "\n" + fichas[0].getId() + "\n" 
					  + fichas[1].getTipo() + "\n" + fichas[1].getCantCoronas() + "\n" + fichas[1].getId() + "\n";
		}
		return ret;
	}

	public void agregarCarta(Carta cartaActual) {
		cartas.add(new Carta(cartaActual.getId(),cartaActual.getFichas()[0],cartaActual.getFichas()[1]));
	}
}
