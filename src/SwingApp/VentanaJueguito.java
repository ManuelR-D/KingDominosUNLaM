package SwingApp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reyes.Carta;
import reyes.Jugador;
import reyes.Partida;

public class VentanaJueguito extends JFrame {

	private static final long serialVersionUID = 4460429712849713216L;
	//Texturas por defecto
	private static String texturaCarta = "./assets/mazos/original.png";
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
	static final int TAM_FICHA = 80;
	static double LARGO_VENTANA;
	static double ALTO_VENTANA;
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
		setResizable(false);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(100, 100, 400, 300);
		getContentPane().setBackground(new Color(0x5E411B));

		cargarTexturas();
		

		this.setLocationRelativeTo(null);
		this.setVisible(true);
		Dimension dimensiones=getContentPane().getSize();
		LARGO_VENTANA = (int) dimensiones.getWidth();
		ALTO_VENTANA = (int) dimensiones.getHeight();
		TAM_TABLEROS = (int) ALTO_VENTANA;
		tableros = new TablerosJugadores(p.getJugadores());
		tableros.setBounds(0, 0, TAM_TABLEROS, TAM_TABLEROS);
		getContentPane().add(tableros);
		int largoPanelInformacion=(int)LARGO_VENTANA-TAM_TABLEROS;
		int altoPanelInformacion = (int)ALTO_VENTANA/2;
		informacion = new PanelInformacion(p.getJugadores(),largoPanelInformacion,altoPanelInformacion);
		informacion.setBounds(TAM_TABLEROS,0,largoPanelInformacion, altoPanelInformacion);
		informacion.setBorder(BorderFactory.createLineBorder(Color.black));
		getContentPane().add(informacion);
		try {
			Sonido s = new Sonido("./assets/Sound/main.wav");
			s.setVolume(0.01f);
			//s.play(Clip.LOOP_CONTINUOUSLY);
			sonido = s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Inicializamos pSeleccion con cartas vacias, esto evita problemas con condiciones de carrera
		//además, no es intuitivo que pSeleccion no exista hasta que se le envien cartas, puesto
		//que delega la responsabilidad de su creacion a Partida, que no es una clase grafica
		List<Carta> cartasAElegir = new ArrayList<Carta>();
		cartasAElegir.add(null);
		mostrarCartasAElegir(cartasAElegir);
		
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
		pSeleccion.setVisible(false);
		List<String> emojis = new ArrayList<String>(4);
		emojis.add("☜(ﾟヮﾟ☜)");
		emojis.add("╰(*°▽°*)╯");
		emojis.add("＼(ﾟｰﾟ＼)");
		emojis.add("(⌐■_■)");
		Collections.shuffle(emojis);
		String mensaje = map.size() == 1 ? "Ganador:\n" : "Ganadores:\n";
		int i = 0;
		for (Map.Entry<Jugador, Integer> entry : map.entrySet()) {
			mensaje += entry.getKey().getNombre() + " con: " + entry.getValue() + " puntos" + emojis.get(i) + "\n";
			i++;
		}
		mostrarMensaje(mensaje);
		JOptionPane.showMessageDialog(this, mensaje, "Fin de partida", JOptionPane.PLAIN_MESSAGE);
	}

	public void mostrarCartasAElegir(List<Carta> cartasAElegir) {
		if (pSeleccion != null) {
			this.remove(pSeleccion);

		}
		
		int altoPanel=(int) (ALTO_VENTANA/2);
		int largoPanel=(int) (LARGO_VENTANA-TAM_TABLEROS);
		
		
		
		pSeleccion = new PanelTableroSeleccion(cartasAElegir,largoPanel,altoPanel);
		pSeleccion.setBounds(TAM_TABLEROS, altoPanel, largoPanel,altoPanel);
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
		//Esto puede suceder por condiciones de carrera. Arreglar de manera mas elegante luego
		if(pSeleccion != null)
			pSeleccion.setVisible(b);
	}

	public void actualizarTablero(int indice, int fila, int columna) {
		tableros.actualizarTablero(indice, fila, columna);
	}

	public void cargarTextura(String textura) {
		File texturaCarta = new File("./assets/mazos/" + textura + ".png");
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
		super.dispose();
		if (sonido != null)
			sonido.stop();
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

	public static void cargarTexturaMazo(String skin) {
		try {
			VentanaJueguito.bufferCarta = ImageIO.read(new File("./assets/mazos/"+skin+".png") );
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}

}
