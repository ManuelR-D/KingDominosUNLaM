package SwingApp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import reyes.Carta;
import reyes.Jugador;
import reyes.Partida;

public class VentanaJueguito extends JFrame {

	private static final long serialVersionUID = 4460429712849713216L;
	private File texturaCarta = new File("./assets/highTest.png");
	private File texturaCastillo = new File("./assets/castillo.png");
	static File texturaVacia = new File("./assets/vacio.png");
	static BufferedImage bufferCastillo;
	static BufferedImage bufferCarta;
	static BufferedImage bufferVacio;
	static final int LARGO_FICHA = 80;
	static final int ALTO_FICHA = 80;
	static int LARGO_VENTANA;
	static int ALTO_VENTANA;
	static int TAM_TABLEROS;
	PanelTableroSeleccion pSeleccion;
	TablerosJugadores tableros;
	JTextArea informacion;
	
	private static CountDownLatch latchCartaElegida = new CountDownLatch(1);
	public static volatile int[] coordenadasElegidas = new int[2];

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
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		try {
			VentanaJueguito.bufferCastillo = ImageIO.read(texturaCastillo);
			VentanaJueguito.bufferCarta = ImageIO.read(texturaCarta);
			VentanaJueguito.bufferVacio = ImageIO.read(texturaVacia);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error generando imagenes clase VentanaJueguito");
		}
		LARGO_VENTANA=(int) this.getSize().getWidth();
		ALTO_VENTANA=(int) this.getSize().getHeight();
		TAM_TABLEROS=(LARGO_VENTANA-LARGO_FICHA*2)-20;
		tableros=new TablerosJugadores(p.getJugadores());
		tableros.setBounds(0,0,TAM_TABLEROS,TAM_TABLEROS);
		getContentPane().add(tableros);
		setUndecorated(true);
		informacion=new JTextArea("Hola");
		informacion.setEditable(false);
		informacion.setBackground(new Color(138,3,3));
		informacion.setForeground(Color.WHITE);
		informacion.setBounds(TAM_TABLEROS+20,0 , LARGO_VENTANA-TAM_TABLEROS, ALTO_VENTANA-(ALTO_FICHA*4));
		informacion.setBorder(BorderFactory.createLineBorder(Color.black));
		getContentPane().add(informacion);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public synchronized int[] obtenerInputCoordenadas(Jugador jugador) {
		return tableros.obtenerInputCoordenadas(jugador);
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
		PanelTableroSeleccion.idCartaElegida = Integer.MIN_VALUE;
		pSeleccion.setStartLatch(new CountDownLatch(1));
		return idCartaElegida;
	}

	public void terminarPartida(List<Jugador> determinarGanadores) {
		System.out.println("Clase VentanaJueguito funcion terminarPartida");		
	}

	public void mostrarCartasAElegir(List<Carta> cartasAElegir) {
//		System.out.println("Clase VentanaJueguito funcion mostrarCartasAELegir");
		if(pSeleccion!=null) {
			this.remove(pSeleccion);
			
		}
		pSeleccion=new PanelTableroSeleccion(cartasAElegir);
		pSeleccion.setBounds(TAM_TABLEROS+20, ALTO_VENTANA-ALTO_FICHA*4, LARGO_FICHA*2, ALTO_FICHA*4);
		this.getContentPane().add(pSeleccion);
		this.repaint();
		
	}

	public void actualizarTableros(List<Jugador> jugadores) {
		System.out.println("Clase VentanaJueguito funcion actualizarTableros");
		tableros.actualizarTableros();
	}

	public void mostrarMensaje(String string) {
		informacion.setText(string);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		LARGO_VENTANA=(int) this.getSize().getWidth();
		ALTO_VENTANA=(int) this.getSize().getHeight();
		TAM_TABLEROS=(LARGO_VENTANA-LARGO_FICHA*2)-20;
		g.fillRect(TAM_TABLEROS+5,0, 10, ALTO_VENTANA);
	}
}
