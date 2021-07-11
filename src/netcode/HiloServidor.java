package netcode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HiloServidor extends Thread {
	private Socket socket;
	private List<Socket> sockets;
	private Map<Socket, ObjectOutputStream> mapaSocketsObjectOuput;
	private List<Sala> salas;
	private Map<String, Socket> mapaNombreSocket;
	private Map<String, Sala> mapaSalas;

	public HiloServidor(Socket socket, List<Socket> sockets, Map<Socket, ObjectOutputStream> mapa,
			Map<String, Socket> mapaNombreSocket, List<Sala> salas, Map<String, Sala> mapaSalas) {
		this.socket = socket;
		this.sockets = sockets;
		this.salas = salas;
		this.mapaNombreSocket = mapaNombreSocket;
		this.mapaSalas = mapaSalas;
		this.mapaSocketsObjectOuput = mapa;
	}

	public void run() {
		ObjectInputStream entrada = null;
		ObjectOutputStream salida = null;
		MensajeAServidor mensajeAServidor = null;
		MensajeACliente mensajeACliente = null;
		String nombreCliente = null;
		// Recibo de datos del cliente
		try {
			entrada = new ObjectInputStream(socket.getInputStream());
			salida = new ObjectOutputStream(socket.getOutputStream());
			mensajeAServidor = (MensajeAServidor) entrada.readObject();
			nombreCliente = mensajeAServidor.getTexto();
			// Si ya existe un usuario con ese nombre, desconecto al cliente
			if (mapaNombreSocket.containsKey(nombreCliente)) {
				mensajeACliente = new MensajeACliente(null, null, -2);
				salida.writeObject(mensajeACliente);
				salida.flush();
				salida.reset();
				entrada.close();
				salida.close();
				sockets.remove(socket);
				socket.close();
				System.out.println("Cliente desconectado por nombre de usuario existente");
				return;
			}
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		mapaNombreSocket.put(nombreCliente, socket);

		try {
			mapaSocketsObjectOuput.put(socket, salida);
			/*
			 * A cada socket le pertenece un ObjectOutputStream ya que si se crean nuevos
			 * ObjectOutputStream, estos insertan un header en el OutputStream y dificulta
			 * la recepcion de mensajes por parte del cliente.
			 */

			// Se le envia un mensaje 0 al cliente para habilitar su interfaz.
			mensajeACliente = new MensajeACliente(null, null, 0);
			salida.writeObject(mensajeACliente);
			salida.flush();
			salida.reset();
			actualizarSalas();
			int tipoMensaje = 1;

			while (tipoMensaje != 0) {// El cliente envia un 0 para desconectarse
				mensajeAServidor = (MensajeAServidor) entrada.readObject();
				tipoMensaje = mensajeAServidor.getTipo();

				switch (tipoMensaje) {
				case 2:// El cliente crea una sala por un mensaje tipo 2
					agregarSala(mensajeAServidor);
					break;
				case 3:// El cliente borra una sala por un mensaje tipo 3
					quitarSala(mensajeAServidor);
					break;
				case 4:// El cliente se une a una sala por un mensaje tipo 4
					unirseASala(mensajeAServidor);
					break;
				case 5:// El cliente sale de una sala por un mensaje tipo 5
					salirDeSala(mensajeAServidor);
					break;
				case 6:// El cliente envia un mensaje a una sala por un mensaje tipo 6
					recibirMensaje(mensajeAServidor);
					break;
				case 7:// El cliente envia un mensaje a una sala por un mensaje tipo 6
					recibirPedidoTiemposSesion(mensajeAServidor);
					break;
				case 8:// Se envia al cliente la lista de usuarios en la sala
					enviarListaUsuariosSala(mensajeAServidor);
					break;
				case 9:// se crea la sala privada y se une a los usuarios
					crearSalaPrivada(mensajeAServidor);
					break;
				case 10:// cuando un usuario sale de la sala privada, se quita al otro.
					salirSalaPrivada(mensajeAServidor);
					break;
				case 11:// recibo estado inicial de una partida
					iniciarJuegoClientes(mensajeAServidor);
					break;
				case 12:// recibo un paquete con la carta elegida y posicion
					procesarTurnoJugador(mensajeAServidor);
					break;
				case 13:// creacion de partida
					crearPartida(mensajeAServidor);
					break;
				case 14:// Se envia al cliente la lista de usuarios en la sala
					enviarListaUsuariosSala(mensajeAServidor);
					break;
				}

				if (tipoMensaje != 0) {
					actualizarSalas();// Se actualizan las salas en cada ciclo.
				}

			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error en lectura de mensaje en HiloServidor");
			e.printStackTrace();
		}
		try {// Desconexion del cliente del servidor
			mensajeACliente = new MensajeACliente(null, null, -1);
			salida.writeObject(mensajeACliente);
			entrada.close();
			salida.close();
			mapaSocketsObjectOuput.remove(socket);
			mapaNombreSocket.remove(mensajeAServidor.getTexto());
			sockets.remove(socket);
			socket.close();
			System.out.println("Cliente desconectado normalmente");
		} catch (IOException e) {
			System.out.println("Error desconectando cliente");
			e.printStackTrace();
		}

	}

	private void crearPartida(MensajeAServidor mensajeServidor) {
		Sala sala = mensajeServidor.getSala();
		Sala salaActual = mapaSalas.get(sala.getNombreSala());

		MensajeACliente msj = new MensajeACliente(mensajeServidor.getTexto(), 11, salaActual);
		// mensaje a dueño de sala de que inicie la partida
		enviarMensajeAUsuario(msj, salaActual.getUsuariosConectados().get(0));

	}

	private void procesarTurnoJugador(MensajeAServidor mensajeAServidor) {
		String nombreSala = mensajeAServidor.getSala().getNombreSala();
		Sala s = null;
		for (Sala sala : salas) {
			if (sala.getNombreSala().equals(nombreSala)) {
				s = sala;
				break;
			}
		}
		if (s == null) {
			System.err.println("SALA INEXSITENTE: " + nombreSala);
		}
		System.out.println("Sala: " + s);
		List<String> jugadores = s.getUsuariosConectados();

		String[] datosPaquete = mensajeAServidor.getTexto().split(",");
		boolean seRindioUsuario = datosPaquete[4].contains("Rendir");
		String emisor = datosPaquete[3];
//		System.out.println("Recibi mensaje del jugador " + emisor);
//		System.out.println("Mensaje crudo: " + mensajeAServidor.getTexto());
		for (String jugador : jugadores) {
			System.out.println(jugador);
			if (!jugador.equals(emisor)) {
				MensajeACliente msj = new MensajeACliente(mensajeAServidor.getTexto(), 13, s);
				enviarMensajeAUsuario(msj, jugador);
//				System.out.println("Lo replique a: " + jugador);
			}
		}
		if (seRindioUsuario) {
			mensajeAServidor.setTexto(emisor);
			salirDeSala(mensajeAServidor);
		}

	}

	private void iniciarJuegoClientes(MensajeAServidor mensajeAServidor) {
		// 11: Nos pidieron avisar a todos los usuarios que el host inicio la partida
		// Tenemos que especificar los turnos iniciales y el mazo.
		Sala salaActual = mensajeAServidor.getSala();
		List<String> jugadoresEnSala = salaActual.getUsuariosConectados();
		for (int i = 1; i < jugadoresEnSala.size(); i++) {
			// 12: Le decimos a los usuarios que inicien su UI, y le pasamos el estado de la
			// partida
			MensajeACliente msj = new MensajeACliente(getName(), 12, salaActual, mensajeAServidor.estado);
			System.out.println("Servdor: Usuario " + jugadoresEnSala.get(i) + " inicia!");
			enviarMensajeAUsuario(msj, jugadoresEnSala.get(i));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void salirSalaPrivada(MensajeAServidor mensaje) {
		String nombreSala = mensaje.getSala().getNombreSala();
		Sala sala = mapaSalas.get(nombreSala);
		if (sala != null) {
			mapaSalas.remove(nombreSala);
			List<String> clientes = sala.getUsuariosConectados();
			// mensaje tipo 4:saca al usuario de la sala
			MensajeACliente msj = new MensajeACliente(null, 4, sala);
			for (String usuario : clientes) {
				enviarMensajeAUsuario(msj, usuario);
			}

		}
	}

	private void enviarMensajeAUsuario(MensajeACliente msj, String usuario) {
		Socket socket = mapaNombreSocket.get(usuario);
		ObjectOutputStream salida = mapaSocketsObjectOuput.get(socket);
		try {
			if (salida != null) {
				salida.writeObject(msj);
				salida.flush();
				salida.reset();
			}
		} catch (IOException e) {
			System.out.println("Error en envio de mensaje a usuario");
			e.printStackTrace();
		}
	}

	private void crearSalaPrivada(MensajeAServidor mensaje) {
		Sala sala = mensaje.getSala();
		mapaSalas.put(sala.getNombreSala(), sala);

		// mensaje tipo 8:crea la sala privada
		List<String> usuarios = sala.getUsuariosConectados();
		MensajeACliente msj = new MensajeACliente(null, 8, sala);
		for (String usuario : usuarios) {
			enviarMensajeAUsuario(msj, usuario);
		}

	}

	private void enviarListaUsuariosSala(MensajeAServidor mensaje) {
		String sala = mensaje.getSala().getNombreSala();
		Sala salaActual = mapaSalas.get(sala);
		List<String> usuarios = salaActual.getUsuariosConectados();
		String cad = "";
		for (String user : usuarios) {
			cad += user + "\n";
		}
		int tipo = mensaje.getTipo() == 8 ? 7 : 16;
		// mensaje tipo 7:envia la lista de usuarios en la sala
		// mensaje tipo 16:envia la lista de usuarios en la sala para menu iniciar
		// partida
		MensajeACliente msj = new MensajeACliente(cad, tipo, salaActual);
		enviarMensajeAUsuario(msj, mensaje.getTexto());
	}

	private void recibirPedidoTiemposSesion(MensajeAServidor mensaje) {
		String sala = mensaje.getSala().getNombreSala();
		Sala salaActual = mapaSalas.get(sala);

		List<String> usuarios = salaActual.getUsuariosConectados();
		Map<String, Long> tiempos = salaActual.getTiempoUsuarios();

		String cad = "";
		Long tiempoActual = System.currentTimeMillis();
		for (String usuario : usuarios) {
			Long tiempoInicial = tiempos.get(usuario);
			int horas = (int) ((tiempoActual - tiempoInicial) / 3600000);
			int minutos = (int) (((tiempoActual - tiempoInicial) % 3600000) / 60000);
			int segundos = (int) ((((tiempoActual - tiempoInicial) % 3600000) % 60000) / 1000);
			String txtHoras = horas < 10 ? ("0" + horas) : horas + "";
			String txtMinutos = minutos < 10 ? ("0" + minutos + "") : minutos + "";
			String txtSegundos = segundos < 10 ? ("0" + segundos + "") : segundos + "";
			cad += usuario + "➡" + txtHoras + ":" + txtMinutos + ":" + txtSegundos + "\n";

		}

		// mensaje tipo 6:envia los tiempos de conexion
		MensajeACliente msjCliente = new MensajeACliente(cad, 6, salaActual);
		enviarMensajeAUsuario(msjCliente, mensaje.getTexto());
	}

	@SuppressWarnings("deprecation")
	private void recibirMensaje(MensajeAServidor mensajeServidor) {
		String nombreSala = mensajeServidor.getSala().getNombreSala();
		Sala salaActual = mapaSalas.get(nombreSala);

		// Se concatena la hora con el mensaje enviado por el cliente.
		Date tiempo = new Date();
		int horas = tiempo.getHours();
		String txtHoras = horas < 10 ? "0" + horas : "" + horas;
		int minutos = tiempo.getMinutes();
		String txtMinutos = minutos < 10 ? "0" + minutos : "" + minutos;
		String hora = "(" + txtHoras + ":" + txtMinutos + ")";
		String mensaje = hora + mensajeServidor.getTexto();

		// mensaje tipo 5: envia mensaje a la sala.
		MensajeACliente msjCliente = new MensajeACliente(mensaje, 5, salaActual);
		List<String> usuariosEnSala = new ArrayList<String>();
		usuariosEnSala = salaActual.getUsuariosConectados();

		for (String usuario : usuariosEnSala) {
			enviarMensajeAUsuario(msjCliente, usuario);
		}
	}

	private void salirDeSala(MensajeAServidor mensajeServidor) {
		Sala sala = mensajeServidor.getSala();
		Sala salaActual = mapaSalas.get(sala.getNombreSala());
		salaActual.eliminarUsuario(mensajeServidor.getTexto());

		// mensaje tipo 4: saca al usuario de la sala
		MensajeACliente msj = new MensajeACliente(null, 4, salaActual);
		enviarMensajeAUsuario(msj, mensajeServidor.getTexto());

		String notificacion = mensajeServidor.getTexto() + " se ha desconectado de la sala";
		MensajeAServidor msjServidor = new MensajeAServidor(notificacion, salaActual, 0);
		recibirMensaje(msjServidor);
	}

	private void unirseASala(MensajeAServidor mensajeServidor) {
		Sala sala = mensajeServidor.getSala();
		Sala salaActual = mapaSalas.get(sala.getNombreSala());
		MensajeACliente msj;

		if (salaActual.getCantUsuarios() < 4) {
			long tiempoInicioSesion = System.currentTimeMillis();
			salaActual.agregarUsuario(mensajeServidor.getTexto(), tiempoInicioSesion);
			// mensaje tipo 3: une al usuario a la sala
			msj = new MensajeACliente(null, 3, salaActual);
			enviarMensajeAUsuario(msj, mensajeServidor.getTexto());
			String notificacion = mensajeServidor.getTexto() + " se ha unido a la sala";
			MensajeAServidor msjServidor = new MensajeAServidor(notificacion, salaActual, 0);
			recibirMensaje(msjServidor);
		} else {
			msj = new MensajeACliente(null, 15, null);
			enviarMensajeAUsuario(msj, mensajeServidor.getTexto());
		}

	}

	private void quitarSala(MensajeAServidor mensaje) {
		Sala sala = mensaje.getSala();
		salas.remove(sala);
		mapaSalas.remove(sala.getNombreSala());
	}

	private void agregarSala(MensajeAServidor mensaje) {
		Sala sala = mensaje.getSala();
		if (!mapaSalas.containsKey(sala.getNombreSala())) {
			salas.add(sala);
			mapaSalas.put(sala.getNombreSala(), sala);
		} else {
			MensajeACliente msj = new MensajeACliente(null, null, 12);
			enviarMensajeAUsuario(msj, mensaje.getTexto());
		}
	}

	private void actualizarSalas() {
		// mensaje tipo 2: actualizacion de salas
		MensajeACliente msj = new MensajeACliente(null, salas, 2);
		enviarMensajeATodosLosSockets(msj);

	}

	private void enviarMensajeATodosLosSockets(MensajeACliente msj) {
		ObjectOutputStream salida;

		try {
			for (Socket envio : sockets) {
				salida = mapaSocketsObjectOuput.get(envio);
				salida.writeObject(msj);
				salida.flush();
				salida.reset();

			}
		} catch (IOException e) {
			System.out.println("Error envio mensaje actualizando sala");
			e.printStackTrace();
		}

	}
}
