package reyes;

import java.util.Scanner;

public class FuncionesGenerales {
	public static int numeroAleatorioEntre(int mayorIgualQue, int menorIgualQue) {
		int numeroAleatorio = (int) Math.floor(Math.random() * (menorIgualQue - mayorIgualQue + 1) + mayorIgualQue);
		return numeroAleatorio;
	}

	public static int intXTecladoEntre(int mayorIgualQue, int menorIgualQue, String msj, Scanner entrada) {
		int numero;
		do {
			System.out.println(msj);
			numero = entrada.nextInt();
		} while (numero < mayorIgualQue || numero > menorIgualQue);
		return numero;
	}
}
