package netcode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import SwingMenu.Lobby;
import reyes.*;

public class HiloCliente extends Thread {
	ObjectInputStream entrada;
	Lobby ventana;
	private static Socket socket;
	public static CountDownLatch mtxPaquetePartida = new CountDownLatch(1);
	public HiloCliente(Socket socket, ObjectInputStream entrada, Lobby ventana) {
		HiloCliente.socket = socket;
		this.entrada = entrada;
		this.ventana = ventana;
	}

	public void run() {
		MensajeACliente mensaje;
		try {
			entrada = new ObjectInputStream(socket.getInputStream());
			int tipoMensaje=0;
			//El siguiente thread tiene como objetivo enviar un paquete al servidor cuando
			//el jugador local juega su turno. Luego el servidor replica esa jugada a todos
			//los demas jugadores.
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) { //cambiar esta asquerosidad inmunda
						System.out.println("Iniciando daemon para jugador " + ventana.getNombreCliente());
						try {
							mtxPaquetePartida.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mtxPaquetePartida = new CountDownLatch(1);
						System.out.println("HiloServidor: Procesando paquete del jugador " + ventana.getNombreCliente());
						MensajeAServidor msj = new MensajeAServidor(Partida.paquete, ventana.getSalaActual(), 12);
						Partida.paquete = null;
						
						ventana.enviarMensaje(msj);
					}
				}
			}).start();
			while (tipoMensaje != -1 && tipoMensaje!=-2) {// Se cierra el hilo con un mensaje del servidor de tipo 1
				mensaje = (MensajeACliente) entrada.readObject();
				tipoMensaje = mensaje.getTipo();
				switch (tipoMensaje) {
				case 0:// 0:el cliente se conecto correctamente
					clienteAceptado();
					break;
				case 1:// 1: el servidor pidio los datos del cliente
					enviarDatos();
					break;
				case 2:// 2:actualizar salas
					actualizarSalas(mensaje);
					break;
				case 3:// 3: el cliente se unio a una sala
					unirseASala(mensaje);
					break;
				case 4:// 4: el cliente salio de una sala
					salirDeSala(mensaje);
					break;
				case 5:// 5: el cliente recibio un mensaje en alguna de sus salas abiertas
					recibirMensaje(mensaje);
					break;
				case 6:
					// 6: recibe tiempos de usuarios en la sala
					recibirTiempos(mensaje);
					break;
				case 7:
					//7: recibe la lista de usuarios en la sala
					recibirListaUsuarios(mensaje);
					break;
				case 8:
					//8: creacion y apertura de sala privada
					salaPrivadaCreada(mensaje);
					break;
				case 9:
					//9: ya existe la sala privada
					mostrarErrorPorPantalla("Sala privada existente", "Error en creacion de sala privada");
					break;
				case 10:
					//10: ya existe una sala con ese nombre.
					mostrarErrorPorPantalla("Elija otro nombre de sala", "Error en creacion de sala");
					break;
				case 11: 
					//11: iniciar partida
					iniciarPartida(mensaje);
					break;
				case 12:
					//12: unirse a partida ya iniciada (por el host)
					unirsePartida(mensaje);
					break;
				case 13:
					//13: procesar un turno jugado
					procesarTurnoJugador(mensaje);
					break;
				case 14:
					//14: recibe la habilitacion o deshabilitacion del boton de iniciarPartida
					setEnabledBtnIniciarPartida(mensaje);
					break;
				}
				if(tipoMensaje==-2) {
					mostrarErrorPorPantalla("Elija otro nombre de usuario", "Desconexion del servidor");
					socket.close();
				}
				
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error en lectura de mensaje en hiloCliente");
			e.printStackTrace();
		}
	}

	private void setEnabledBtnIniciarPartida(MensajeACliente mensaje) {
		ventana.setEnabledBtnIniciarPartida(mensaje);
	}

	private void procesarTurnoJugador(MensajeACliente mensaje) {
		System.out.println(ventana.getNombreCliente() + "recibi el turno de otro jugador!");
		Partida.paquete = mensaje.getTexto();
		Partida.mtxEsperarPaquete.countDown(); //le aviso a partida que tiene un paquete a procesar
	}

	private void unirsePartida(MensajeACliente mensaje) {
		Sala s = mensaje.getSala();
		List<String> jugadoresEnSala = s.getUsuariosConectados();
		List<Jugador> jugadores = new ArrayList<Jugador>(4);
		String tituloVentana="Jugador:"+ventana.getNombreCliente()+" Sala:"+ mensaje.getSala().getNombreSala();
		for (String string : jugadoresEnSala) {
			jugadores.add(new Jugador(new Usuario(string,"123")));
		}
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Partida p = new Partida(jugadores,5,48);
					p.setMazo(mensaje.estado.getMazoMezcladoDePartida());
					p.setTurnosIniciales(mensaje.estado.getTurnosIniciales());
					p.setJugadorLocal(ventana.getNombreCliente());
					p.iniciarPartida("",tituloVentana);
				} catch (KingDominoExcepcion | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private void iniciarPartida(MensajeACliente mensaje) {
		List<Jugador> jugadores = new ArrayList<Jugador>(4);
		List<String> usuarios = mensaje.getSala().getUsuariosConectados();
		String tituloVentana="Jugador:"+ventana.getNombreCliente()+" Sala:"+ mensaje.getSala().getNombreSala();
		for (String string : usuarios) {
			jugadores.add(new Jugador(new Usuario(string,"123")));
		}
		//Iniciamos el juego
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Partida p = new Partida(jugadores,5,48);
					p.setJugadorLocal(ventana.getNombreCliente());
					p.iniciarPartida("",tituloVentana);
				} catch (KingDominoExcepcion | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
		//Le avisamos al servidor que el juego esta iniciado, y le enviamos el estado del juego.
		Mazo mazo = null;
		List<Integer> turnos = null;
		do {
			mazo = Partida.getMazo();
			turnos = Partida.getTurnosIniciales();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while(mazo == null || turnos == null);
		
		MensajeEstadoPartida msj = new MensajeEstadoPartida(mazo, turnos);
		MensajeAServidor msjServidor = new MensajeAServidor(getName(), mensaje.getSala(), 11, msj);
		System.out.println("Cliente host: Inicien!");
		ventana.enviarMensaje(msjServidor);
	}

	private void mostrarErrorPorPantalla(String descripcion, String titulo) {
		ventana.mostrarErrorPorPantalla(descripcion, titulo);
	}

	private void salaPrivadaCreada(MensajeACliente mensaje) {
		ventana.salaPrivadaCreada(mensaje);
	}

	private void recibirListaUsuarios(MensajeACliente mensaje) {
		ventana.recibirListaUsuarios(mensaje);
	}

	private void recibirTiempos(MensajeACliente mensaje) {
		ventana.recibirTiempos(mensaje);
	}

	private void recibirMensaje(MensajeACliente mensaje) {
		ventana.recibirMensaje(mensaje);
	}

	private void enviarDatos() {
		ventana.enviarDatosAlServidor();
	}

	private void clienteAceptado() {
		ventana.activarBotones();
	}

	private void salirDeSala(MensajeACliente mensaje) {
		ventana.cerrarSala(mensaje.getSala());
	}

	private void unirseASala(MensajeACliente mensaje) {
		ventana.abrirSala(mensaje.getSala());
	}

	private void actualizarSalas(MensajeACliente mensaje) {
		ventana.actualizarSalas(mensaje.getSalas());
	}

}
