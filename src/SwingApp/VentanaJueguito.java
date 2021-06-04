package SwingApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import reyes.Carta;
import reyes.Jugador;
import reyes.Partida;
import reyes.TableroSeleccion;

public class VentanaJueguito extends JFrame {

	private static final long serialVersionUID = 4460429712849713216L;
	private File texturaCarta = new File("./assets/highTest.png");
	// private File texturaVacia = new File("./assets/vacio.png");
	private File texturaCastillo = new File("./assets/castillo.png");
	static BufferedImage bufferCastillo;
	static BufferedImage bufferCarta;
	private JPanel contentPane;
	private PanelTableroSeleccion pSeleccion;
	private JLabel informacion;
	private TablerosJugadores tablerosJugadores;

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
	public VentanaJueguito(Partida p){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1800, 800);
		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new GridLayout(p.getJugadores().size() / 2, p.getJugadores().size() / 2, 50, 50));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		informacion = new JLabel();
		informacion.setVisible(true);
		this.add(informacion);

		try {
			VentanaJueguito.bufferCastillo = ImageIO.read(texturaCastillo);
			VentanaJueguito.bufferCarta = ImageIO.read(texturaCarta);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error generando imagenes clase VentanaJuegito");
		}
		tablerosJugadores = new TablerosJugadores(p.getJugadores());
		tablerosJugadores.setLayout(new GridLayout(2, 2));
		this.add(tablerosJugadores,BorderLayout.CENTER);

		this.setTitle(p.getJugadores().get(0).getNombre());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
//		this.pSeleccion = new PanelTableroSeleccion(new TableroSeleccion(), 0, 0);
//		pSeleccion.setLayout(null);
//		this.add(pSeleccion);

	}

	public void actualizarTableros(List<Jugador> jugadores){
		tablerosJugadores.actualizarTableros();
	}

	public void terminarPartida(List<Jugador> list) {
		contentPane.remove(pSeleccion);
		String msg = "Ganadores: ";
		for (Jugador jugador : list) {
			msg += jugador.getNombre() + " con " + jugador.tablero.puntajeTotal(false) + " puntos ";
		}
		JLabel msgLabel = new JLabel(msg);
		msgLabel.setFont(new Font("Serif", Font.PLAIN, 24));
		contentPane.add(msgLabel);
		contentPane.invalidate();
		contentPane.validate();
		contentPane.repaint();
	}

	public int mostrarCartas(List<Carta> cartasAElegir){
		if(pSeleccion!=null) {
			contentPane.remove(pSeleccion);			
		}
		pSeleccion = new PanelTableroSeleccion(new TableroSeleccion(cartasAElegir), 1, 1);
		pSeleccion.setBorder(BorderFactory.createLineBorder(Color.black));
		pSeleccion.setPreferredSize(new Dimension(PanelFicha.LARGO_FICHA*3,PanelFicha.ALTO_FICHA*5));
//		pSeleccion.setLayout(null);
//		pSeleccion.setBounds(0, 20, PanelFicha.LARGO_CARTA * 4, PanelFicha.ALTO_CARTA * 4);
		contentPane.add(pSeleccion,BorderLayout.EAST);
//		contentPane.invalidate();
//		contentPane.validate();
		contentPane.repaint();

		return 0;
	}

	public synchronized int leerCartaElegida() {
		try {
			pSeleccion.getStartLatch().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Error en leerCartaElegida");
		}
		System.out.println("Holiwis");
		this.repaint();
		int idCartaElegida = PanelTableroSeleccion.idCartaElegida;
		PanelTableroSeleccion.idCartaElegida = Integer.MIN_VALUE;
		pSeleccion.setStartLatch(new CountDownLatch(1));
		return idCartaElegida;
	}

	public synchronized int[] obtenerInputCoordenadas(Jugador jugador) {
		return tablerosJugadores.obtenerInputCoordenadas(jugador);
	}

	public static CountDownLatch getLatchCartaElegida() {
		return latchCartaElegida;
	}

	public static void setLatchCartaElegida(CountDownLatch latchCartaElegida) {
		VentanaJueguito.latchCartaElegida = latchCartaElegida;
	}

	public void mostrarMensaje(String msj) {
		informacion.setText(msj);
	}

}
