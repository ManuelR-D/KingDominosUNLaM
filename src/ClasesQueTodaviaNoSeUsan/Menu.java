//package ClasesQueTodaviaNoSeUsan;
//
//import java.util.Scanner;
//
//public class Menu {
//	private Juego juego;
//	private Usuario user;
//	private Scanner input;
//	
//	public Menu(Juego juego) {
//		this.juego = juego;
//		input = new Scanner(System.in);
//	}
//	
//	public void ingresarMenu(Usuario user) {
//		this.user = user;
//	}
//	
//	public void iniciarMenu() {
//		char op = 1;
//		while(op < 3 && op >= 0)
//		{
//			if(user.salaActual != null) {
//				try {
//					esperarEnSala();
//					op=1;
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			else {
//				op = elegirOpcion();
//			}
//			
//			
//		}
//	}
//	
//	private char elegirOpcion() {
//
//		System.out.println("Elija una opcion");
//		System.out.println("1: Unirse a sala");
//		System.out.println("2: Crear sala");
//		System.out.println("3: Salir");
//		char op = (char) input.nextByte();
//		switch (op) {
//		case 1: {
//			System.out.println("Elija sala a la cual unirse");
//			juego.mostrarSalas();
//			char idSala = (char)input.nextByte();
//			Sala salaElegida = juego.buscarSala(idSala);
//			if(salaElegida == null)
//				System.out.println("La sala elegida no existe");
//			else
//				user.unirseSala(salaElegida);
//			break;
//		}
//		case 2: {
//			Sala nuevaSala = user.crearSala();
//			juego.generarIdYAñadirSala(nuevaSala);
//			break;
//		}
//		case 3: {
//			return op;
//		}
//		default:
//			System.out.println("La opcion ingresada no es valida");
//			return 1;
//		}
//		return op;
//	}
//	
//	private void esperarEnSala() throws InterruptedException {
//		char op;
//		while(user.salaActual != null && user.salaActual.getCantJugadoresEnSala() < 4) {
//			System.out.println("Se esperan mas jugadores");
//			user.salaActual.mostrarJugadoresEnSala();
//			System.out.println("1: Salir de sala");
//			System.out.println("2: Refrescar");
//			System.out.println("3: Añadir Bots");
//			op = (char) input.nextByte();
//			if (op == 1) {
//				user.salirSala();
//			} else if (op == 3) {
//				for (Usuario user : juego.getUsuarios()) {
//					if (!user.equals(this.user)) {
//						user.unirseSala(this.user.salaActual);
//					}
//				}
//			}
//		}
//		if(user.salaActual != null && user.salaActual.getCantJugadoresEnSala() >= 4) {
//			user.salaActual.crearPartida();
//		}
//	}
//}
