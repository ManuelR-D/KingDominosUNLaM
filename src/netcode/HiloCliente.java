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
	static Lobby ventana;
	private Socket socket;
	private CountDownLatch mtxPaquetePartida = new CountDownLatch(1);
	private boolean partidaEnProceso = false;
	private Partida partida;

	public HiloCliente(Socket socket, ObjectInputStream entrada, Lobby ventana) {
		this.socket = socket;
		this.entrada = entrada;
		HiloCliente.ventana = ventana;
	}

	public void run() {
		MensajeACliente mensaje;
		try {
			entrada = new ObjectInputStream(socket.getInputStream());
			int tipoMensaje = 0;
			while (tipoMensaje != -1 && tipoMensaje != -2) {// Se cierra el hilo con un mensaje del servidor de tipo 1
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
					// 7: recibe la lista de usuarios en la sala
					recibirListaUsuarios(mensaje);
					break;
				case 8:
					// 8: creacion y apertura de sala privada
					salaPrivadaCreada(mensaje);
					break;
				case 9:
					// 9: ya existe la sala privada
					mostrarErrorPorPantalla("Sala privada existente", "Error en creacion de sala privada");
					break;
				case 10:
					// 10: ya existe una sala con ese nombre.
					mostrarErrorPorPantalla("Elija otro nombre de sala", "Error en creacion de sala");
					break;
				case 11:
					// 11: iniciar partida
					iniciarPartida(mensaje);
					break;
				case 12:
					// 12: unirse a partida ya iniciada (por el host)
					unirsePartida(mensaje);
					break;
				case 13:
					// 13: procesar un turno jugado
					procesarTurnoJugador(mensaje);
					break;
				case 15:
					// 15: error ya hay 4 usuarios en la sala
					mostrarErrorPorPantalla("Ya hay 4 usuarios en la sala", "Error uniendose a sala");
					break;
				case 16:
					// 16: recepcion lista usuarios para menu de creacion de partida
					recibirListaUsuarios(mensaje);
					break;
				case 17:
					// 17: El servidor le avisa al creador que ingreso o salio un usuario, debe actualizar el menu
					actualizarMenu(mensaje);
					break;

				}
				if (tipoMensaje == -2) {
					mostrarErrorPorPantalla("Elija otro nombre de usuario", "Desconexion del servidor");
					socket.close();
				}

			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error en lectura de mensaje en hiloCliente");
			e.printStackTrace();
		}
	}

	private void actualizarMenu(MensajeACliente mensaje) {
		ventana.actualizarMenu(mensaje);
	}

	private void procesarTurnoJugador(MensajeACliente mensaje) {
		partida.setPaquete(mensaje.getTexto());
		partida.getMtxEsperarPaquete().countDown(); // le aviso a partida que tiene un paquete a procesar
	}

	private void unirsePartida(MensajeACliente mensaje) {
		String[] configuracion = mensaje.getEstado().getConfiguracion();
		String tiposJugadores = configuracion[0];
		String[] nombresJugadores = configuracion[1].split("\\|");
		int tamTablero = Integer.parseInt(configuracion[2]);
		String textura = configuracion[3];
		String nombreMazo = configuracion[4];
		String modoDeJuego = configuracion[5];
		List<Jugador> jugadores = new ArrayList<Jugador>();
		String tituloVentana = "Jugador:" + ventana.getNombreCliente() + " Sala:" + mensaje.getSala().getNombreSala();

		for (int i = 0; i < tiposJugadores.length(); i++) {
			char tipo = tiposJugadores.charAt(i);
			Jugador jugador;
			if (tipo == 'B') {
				jugador = new Bot(nombresJugadores[i], tamTablero);
			} else {
				jugador = new Jugador(nombresJugadores[i], tamTablero);
			}
			jugadores.add(jugador);
		}
		String variante = nombreMazo + "|" + modoDeJuego;
		HiloCliente THIS = this;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					partida = new Partida(jugadores, tamTablero, 48, textura, THIS);
					partida.setMazo(mensaje.estado.getMazoMezcladoDePartida());
					partida.setTurnosIniciales(mensaje.estado.getTurnosIniciales());
					partida.setJugadorLocal(ventana.getNombreCliente());
					for (Jugador jugador : jugadores) {
						if (jugador instanceof Bot) {
							partida.addBotLocal(jugador.getNombre());
						}
					}
					partida.iniciarPartida(variante, tituloVentana);
				} catch (KingDominoExcepcion | IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();

		hiloPartida();
	}

	private void iniciarPartida(MensajeACliente mensaje) {
		String[] configuracion = mensaje.getTexto().split(",");
		String tiposJugadores = configuracion[0];
		String[] nombresJugadores = configuracion[1].split("\\|");
		int tamTablero = Integer.parseInt(configuracion[2]);
		String textura = configuracion[3];
		String nombreMazo = configuracion[4];
		String modoDeJuego = configuracion[5];
		List<Jugador> jugadores = new ArrayList<Jugador>();
		String tituloVentana = "Jugador:" + ventana.getNombreCliente() + " Sala:" + mensaje.getSala().getNombreSala();

		for (int i = 0; i < tiposJugadores.length(); i++) {
			char tipo = tiposJugadores.charAt(i);
			Jugador jugador;
			if (tipo == 'B') {
				jugador = new Bot(nombresJugadores[i], tamTablero);
			} else {
				jugador = new Jugador(nombresJugadores[i], tamTablero);
			}
			jugadores.add(jugador);
		}

		// El formato de variante es: {mazo}|{variante1}|{variante2}|{varianteN}
		String variante = nombreMazo + "|" + modoDeJuego;
		// Iniciamos el juego
		HiloCliente THIS = this;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					partida = new Partida(jugadores, tamTablero, 48, textura, THIS);
					partida.setJugadorLocal(ventana.getNombreCliente());
					for (Jugador jugador : jugadores) {
						if (jugador instanceof Bot) {
							partida.addBotLocal(jugador.getNombre());
						}
					}
					partida.iniciarPartida(variante, tituloVentana);
				} catch (KingDominoExcepcion | IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
//		// Le avisamos al servidor que el juego esta iniciado, y le enviamos el estado
//		// del juego.
		Mazo mazo = null;
		List<Integer> turnos = null;
		do {
			if (partida != null) {
				mazo = partida.getMazo();
				turnos = partida.getTurnosIniciales();
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (mazo == null || turnos == null);

		MensajeEstadoPartida msjEstadoPartida = new MensajeEstadoPartida(mazo, configuracion, turnos);
		MensajeAServidor msjServidor = new MensajeAServidor(getName(), mensaje.getSala(), 11, msjEstadoPartida);
		System.out.println("Cliente host: Inicien!");
		ventana.enviarMensaje(msjServidor);

		hiloPartida();
	}

	private void hiloPartida() {
		// El siguiente thread tiene como objetivo enviar un paquete al servidor cuando
		// el jugador local juega su turno. Luego el servidor replica esa jugada a todos
		// los demas jugadores.
		partidaEnProceso = true;
		setMtxPaquetePartida(new CountDownLatch(1));
		new Thread(new Runnable() {
			@Override
			public void run() {
				partidaEnProceso = true;
				while (partidaEnProceso) {
					System.out.println("Iniciando daemon para jugador " + ventana.getNombreCliente());
					try {
						getMtxPaquetePartida().await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					setMtxPaquetePartida(new CountDownLatch(1));
					System.out.println("HiloServidor: Procesando paquete del jugador " + ventana.getNombreCliente());
					MensajeAServidor msj;
					msj = new MensajeAServidor(partida.getPaquete(), ventana.getSalaActual(), 12);
					partida.setPaquete(null);
					ventana.enviarMensaje(msj);

				}
				System.out.println("Sali de hiloCliente");
			}
		}).start();
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

	public void rendirse(String nombre) {
		partidaEnProceso = false;
		partida.rendirse();
		partida.setPaquete("1,1,1," + nombre + ",Rendir,1");
		getMtxPaquetePartida().countDown();
	}

	public CountDownLatch getMtxPaquetePartida() {
		return mtxPaquetePartida;
	}

	public void setMtxPaquetePartida(CountDownLatch mtxPaquetePartida) {
		this.mtxPaquetePartida = mtxPaquetePartida;
	}

}
