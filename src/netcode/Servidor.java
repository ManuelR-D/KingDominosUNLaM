package netcode;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servidor {
	private ServerSocket server;
	private List<Socket> sockets;
	private List<Sala> salas;

	private Map<String, Sala> mapaSalas;
	private Map<Socket, ObjectOutputStream> mapaSocketsObjectOuput;
	private Map<String, Socket> mapaNombreSocket;

	public Servidor(int puerto) {
		this.sockets = new ArrayList<Socket>();
		this.salas = new ArrayList<Sala>();
		this.mapaSalas = new HashMap<String, Sala>();
		this.mapaNombreSocket = new HashMap<String, Socket>();
		this.mapaSocketsObjectOuput = new HashMap<Socket, ObjectOutputStream>();

		try {
			server = new ServerSocket(puerto);
		} catch (IOException e) {
			System.out.println("Error en creacion de puertos");
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				Socket socket = server.accept();
				System.out.println("Cliente conectado");
				sockets.add(socket);
				new HiloServidor(socket, sockets, mapaSocketsObjectOuput, mapaNombreSocket, salas, mapaSalas).start();
			}

		} catch (IOException e) {
			System.out.println("Error en conexion con el cliente");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		System.out.println("Servidor ejecutandose");
		new Servidor(50000).run();
	}

}
