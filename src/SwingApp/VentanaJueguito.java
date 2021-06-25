package SwingApp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reyes.Carta;
import reyes.Jugador;
import reyes.Partida;

public class VentanaJueguito extends JFrame {

	private static final long serialVersionUID = 4460429712849713216L;
	private static String texturaCarta = "./assets/highTest.png";
	private static String texturaCastilloAmarillo = "./assets/castilloAmarillo.png";
	private static String texturaCastilloAzul = "./assets/castilloAzul.png";
	private static String texturaCastilloRojo = "./assets/castilloRojo.png";
	private static String texturaCastilloVerde = "./assets/castilloVerde.png";
	private static String texturaCorona = "./assets/corona.png";
	static String texturaVacia = "./assets/vacio.png";
	
	private Sonido sonido;
	static BufferedImage bufferCastilloAmarillo;
	static BufferedImage bufferCastilloAzul;
	static BufferedImage bufferCastilloRojo;
	static BufferedImage bufferCastilloVerde;
	static BufferedImage bufferCarta;
	static BufferedImage bufferVacio;
	static BufferedImage bufferCorona;
	static final int LARGO_FICHA = 80;
	static final int ALTO_FICHA = 80;
	static int LARGO_VENTANA;
	static int ALTO_VENTANA;
	static int TAM_TABLEROS;
	PanelTableroSeleccion pSeleccion;
	TablerosJugadores tableros;
	PanelInformacion informacion;
	private static int turnoJugador;

	private static CountDownLatch latchCartaElegida = new CountDownLatch(1);
	public static volatile int[] coordenadasElegidas = new int[4];
	public static VentanaJueguito mainFrame;
	public static PanelFicha fichaElegida;

	/**
	 * Launch the application.
	 */
	public void mostrarPartida(Partida p) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaJueguito frame = new VentanaJueguito(p);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error creando ventana");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaJueguito(Partida p) {
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(100, 100, 800, 600);
		getContentPane().setBackground(new Color(0x5E411B));

		cargarTexturas();
		setUndecorated(true);
		informacion = new PanelInformacion(p.getJugadores());

		this.setLocationRelativeTo(null);
		this.setVisible(true);
		LARGO_VENTANA = (int) this.getSize().getWidth();
		ALTO_VENTANA = (int) this.getSize().getHeight();
		TAM_TABLEROS = ALTO_VENTANA;
		tableros = new TablerosJugadores(p.getJugadores());
		tableros.setBounds(0, 0, TAM_TABLEROS, TAM_TABLEROS);
		getContentPane().add(tableros);
		informacion.setBounds(TAM_TABLEROS + 20, 20, LARGO_FICHA * 2, ALTO_VENTANA - (ALTO_FICHA * 5));
		informacion.setBorder(BorderFactory.createLineBorder(Color.black));
		getContentPane().add(informacion);
		try {
			Sonido s = new Sonido("./assets/Sound/main.wav");
			s.setVolume(0.1f);
			s.play(Clip.LOOP_CONTINUOUSLY);
			sonido = s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		VentanaJueguito.mainFrame = this;
	}

	public synchronized int[] obtenerInputCoordenadas(Carta carta) {

		// Si "carta" fue rotada, y luego se intentÃ³ insertar en un lugar invÃ¡lido,
		// Jugador se encarga
		// de dejar a Carta en su posiciÃ³n "default", este cambio requiere que
		// pSeleccion redibuje a
		// carta, puesto que en caso contrario se sigue viendo a la carta "rotada"
		// cuando en realidad
		// no estÃ¡, lo que genera que al insertar realmente la carta, se inserte en una
		// rotaciÃ³n inesperada.
		// onCartaElegida se encarga ya de redibujar la carta en su rotaciÃ³n.

		pSeleccion.onCartaElegida(carta);
		return tableros.obtenerInputCoordenadas();
	}

	public static CountDownLatch getLatchCartaElegida() {
		return latchCartaElegida;
	}

	public static void setLatchCartaElegida(CountDownLatch latchCartaElegida) {
		VentanaJueguito.latchCartaElegida = latchCartaElegida;
	}

	public synchronized int leerCartaElegida() {
		System.out.println("Funcion leerCartaElegida");
		try {
			pSeleccion.getStartLatch().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Error en leerCartaElegida");
		}
		this.repaint();
		System.out.println("Saliendo de leerCartaElegida");
		int idCartaElegida = PanelTableroSeleccion.idCartaElegida;
		pSeleccion.setStartLatch(new CountDownLatch(1));
		Sonido.playCartaSeleccionada();
		return idCartaElegida;
	}

	public void terminarPartida(Map<Jugador, Integer> map) {
		System.out.println("Clase VentanaJueguito funcion terminarPartida");
		pSeleccion.setVisible(false);
		String mensaje = map.size() == 1 ? "Ganador:\n" : "Ganadores:\n";
		for (Map.Entry<Jugador, Integer> entry : map.entrySet()) {
			mensaje += entry.getKey().getNombre() + " con: " + entry.getValue() + " puntos";
		}
		mostrarMensaje(mensaje);
		JOptionPane.showMessageDialog(this, mensaje + "☜(ﾟヮﾟ☜)", "Fin de partida", JOptionPane.PLAIN_MESSAGE);
	}

	public void mostrarCartasAElegir(List<Carta> cartasAElegir) {
		if (pSeleccion != null) {
			this.remove(pSeleccion);

		}
		pSeleccion = new PanelTableroSeleccion(cartasAElegir);

		pSeleccion.setBounds(TAM_TABLEROS + 20, ALTO_VENTANA - ALTO_FICHA * 4, LARGO_FICHA * 2, ALTO_FICHA * 4);
		PanelTableroSeleccion.idCartaElegida = Integer.MIN_VALUE;
		this.getContentPane().add(pSeleccion);

		this.repaint();

	}

	public void actualizarTableros() {
		tableros.actualizarTableros();
	}

	public void mostrarMensaje(String string) {
		informacion.mostrarInfo(string);
	}

	public void pintarFicha(int fila, int columna, int indice, int acumPuntos, int cantCoronas) {
		tableros.pintarFicha(fila, columna, indice, acumPuntos, cantCoronas);
	}

	public void setPSeleccionVisible(boolean b) {
		pSeleccion.setVisible(b);
	}

	public void actualizarTablero(int indice, int fila, int columna) {
		tableros.actualizarTablero(indice, fila, columna);
	}

	public void cargarTextura(String textura) {
		File texturaCarta = new File("./assets/" + textura + ".png");
		try {
			VentanaJueguito.bufferCarta = ImageIO.read(texturaCarta);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Movimiento no permitido", JOptionPane.ERROR_MESSAGE);
	}

	public void mostrarVentanaMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje);
	}

	@Override
	public void dispose() {
		if (sonido != null)
			sonido.stop();
		super.dispose();
	}

	public void deshabilitarBotonesPuntaje() {
		informacion.deshabilitarBotones();
	}

	public void habilitarBotonesPuntaje() {
		informacion.habilitarBotones();
	}

	public static int getTurnoJugador() {
		return turnoJugador;
	}

	public static void setTurnoJugador(int turnoJugador) {
		VentanaJueguito.turnoJugador = turnoJugador;
	}
	
	public static void cargarTexturas() {
		try {
			VentanaJueguito.bufferCastilloAmarillo = ImageIO.read(new File(texturaCastilloAmarillo));
			VentanaJueguito.bufferCastilloAzul = ImageIO.read(new File(texturaCastilloAzul));
			VentanaJueguito.bufferCastilloRojo = ImageIO.read(new File(texturaCastilloRojo));
			VentanaJueguito.bufferCastilloVerde = ImageIO.read(new File(texturaCastilloVerde));
			VentanaJueguito.bufferCarta = ImageIO.read(new File(texturaCarta));
			VentanaJueguito.bufferVacio = ImageIO.read(new File(texturaVacia));
			VentanaJueguito.bufferCorona = ImageIO.read(new File(texturaCorona));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error generando imagenes clase VentanaJueguito");
		}
	}

}
