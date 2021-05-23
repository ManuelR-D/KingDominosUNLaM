package reyes;

public class FuncionesGenerales {
	public static int numeroAleatorioEntre(int mayorIgualQue, int menorIgualQue) {
		int numeroAleatorio = (int) Math.floor(Math.random() * (menorIgualQue - mayorIgualQue + 1) + mayorIgualQue);
		return numeroAleatorio;
	}
}
