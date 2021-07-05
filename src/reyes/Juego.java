package reyes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Juego {
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private List<Sala> salas = new ArrayList<Sala>();
	private Menu Menu = new Menu(this);

	public static void main(String[] args) throws KingDominoExcepcion, IOException {
		//Juego test = new Juego();
		//test.usuarios.add(new Bot("Bot1", "123"));
		//test.iniciarSalaYMostrar();
	}

	private void iniciarSalaYMostrar() throws KingDominoExcepcion, IOException {
		Scanner in = new Scanner(System.in);
		this.Menu = new Menu(this);
		System.out.println("Ingrese nombre de usuario");
		String nombreDeUsuario = in.nextLine();
		System.out.println("Ingrese contraseña");
		String contraseña = in.nextLine();
		Usuario nuevoUsuario = this.crearUsuario(nombreDeUsuario, contraseña);
		usuarios.add(nuevoUsuario);
		this.crearSalaInicial(nuevoUsuario);
		this.Menu.iniciarMenu();

	}

	public Usuario crearUsuario(String nombreDeUsuario, String contraseña) {
		Usuario user = new Usuario(nombreDeUsuario, contraseña);
		this.Menu.ingresarMenu(user);
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

	// Solo se llama al crear el primer usuario
	private void crearSalaInicial(Usuario dueñoDeSala) {
		System.out.println("No hay salas! Cree usted la primera");
		Sala testSala = dueñoDeSala.crearSala();
		testSala.setIdSala(salas.size());
		salas.add(testSala);
	}
	///////////////////////////////////////////

	public Sala generarIdYAñadirSala(Sala sala) {
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
