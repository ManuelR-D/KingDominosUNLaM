package reyes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Juego {
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private List<Sala> salas = new ArrayList<Sala>();

	public static void main(String[] args) throws KingDominoExcepcion, IOException {
		//Juego test = new Juego();
		//test.usuarios.add(new Bot("Bot1", "123"));
		//test.iniciarSalaYMostrar();
	}

	public Usuario crearUsuario(String nombreDeUsuario, String contrasenia) {
		Usuario user = new Usuario(nombreDeUsuario, contrasenia);
		return user;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public List<Sala> getSalas(){
		return salas;
	}
	public void mostrarSalas() {
		for (Sala sala : salas) {
			System.out.println(sala);
		}
	}
	public Sala generarIdYAniadirSala(Sala sala) {
		sala.setIdSala(salas.size());
		salas.add(sala);
		return sala; // id de la sala recien creada
	}

	public Sala buscarSala(char op) {
		if (op <= salas.size())
			return salas.get(op);
		return null;
	}

}
