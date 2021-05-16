package ClasesQueTodaviaNoSeUsan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Juego {
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private List<Sala> salas = new ArrayList<Sala>();
	private Menu Menu;
	
	public static void main(String[] args) {
		Juego test = new Juego();
		test.usuarios.add(new Usuario("Bot1","123"));
		test.usuarios.add(new Usuario("Bot2","123"));
		test.iniciarSalaYMostrar();
	}
	
	
	private void iniciarSalaYMostrar() {
		Scanner in = new Scanner(System.in);
		this.Menu = new Menu(this);
        System.out.println("Ingrese nombre de usuario");
		String nombreDeUsuario = in.nextLine();
		System.out.println("Ingrese contrase�a");
		String contrase�a = in.nextLine();
		Usuario nuevoUsuario = this.crearUsuario(nombreDeUsuario, contrase�a);
		usuarios.add(nuevoUsuario);
		this.crearSalaInicial(nuevoUsuario);
		this.Menu.iniciarMenu();
		
	}
	public Usuario crearUsuario(String nombreDeUsuario, String contrase�a) {
		Usuario user = new Usuario(nombreDeUsuario, contrase�a);
		this.Menu.ingresarMenu(user);
		return user;
	}
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void mostrarSalas() {
		for (Sala sala : salas) {
			System.out.println(sala);
		}
	}
	//Solo se llama al crear el primer usuario
	private void crearSalaInicial(Usuario due�oDeSala) {
		System.out.println("No hay salas! Cree usted la primera");
		Sala testSala = due�oDeSala.crearSala();
		testSala.setIdSala(salas.size());
		salas.add(testSala);
		//Proposito de debug, a�adimos bots/////////
		/*for (Usuario user : usuarios) {
			if (!user.equals(due�oDeSala)) 
				user.unirseSala(testSala);
		}*/
		///////////////////////////////////////////
	}
	///////////////////////////////////////////
	
	public Sala generarIdYA�adirSala(Sala sala) {
		sala.setIdSala(salas.size());
		salas.add(sala);
		return sala; //id de la sala recien creada
	}


	public Sala buscarSala(char op) {
		if(op <= salas.size())
			return salas.get(op);
		return null;
	}

}
