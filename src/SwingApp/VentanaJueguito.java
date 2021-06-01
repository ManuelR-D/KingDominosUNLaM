package SwingApp;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import jdk.jfr.ContentType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import reyes.Bot;
import reyes.Carta;
import reyes.Jugador;
import reyes.Partida;
import reyes.Tablero;
import reyes.TableroSeleccion;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.concurrent.CountDownLatch;

public class VentanaJueguito extends JFrame {

	private static final long serialVersionUID = 4460429712849713216L;
	private File texturaCarta = new File("./assets/highTest.png");
	// private File texturaVacia = new File("./assets/vacio.png");
	private File texturaCastillo = new File("./assets/castillo.png");
	static BufferedImage bufferCastillo;
	static BufferedImage bufferCarta;
	private JPanel contentPane;
	private List<PanelJugador> tableros;
	private PanelCartasElegir pSeleccion;
	private Carta cartaActual = null;

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
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaJueguito(Partida p) throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(p.getJugadores().size() / 2, p.getJugadores().size() / 2, 50, 50));
		setContentPane(contentPane);

		VentanaJueguito.bufferCastillo = ImageIO.read(texturaCastillo);
		VentanaJueguito.bufferCarta = ImageIO.read(texturaCarta);
		dibujarTableros(p.getJugadores());

		this.setTitle(p.getJugadores().get(0).getNombre());
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// this.setUndecorated(true);
		this.setVisible(true);
		this.pSeleccion = new PanelCartasElegir(new TableroSeleccion(), 0, 0);
		this.add(pSeleccion);

	}


	public void dibujarTableros(List<Jugador> jugadores) throws IOException {
		this.tableros = new ArrayList<PanelJugador>(jugadores.size());
		for (Jugador jugador : jugadores) {
			PanelJugador panel = new PanelJugador(jugador);
			contentPane.add(panel);
			this.tableros.add(panel);
		}
	}

	public void actualizarTableros(List<Jugador> jugadores) throws IOException {
		for (PanelJugador pJ : tableros) {
			pJ.actualizarPanel();
		}
		contentPane.repaint();
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

	public int mostrarCartas(List<Carta> cartasAElegir) throws IOException {
		contentPane.remove(pSeleccion);
		pSeleccion = new PanelCartasElegir(new TableroSeleccion(5, cartasAElegir), 1, 1);
		pSeleccion.setBounds(0, 20, PanelFicha.LARGO_CARTA * 4, PanelFicha.ALTO_CARTA * 4);
		contentPane.add(pSeleccion);
		contentPane.invalidate();
		contentPane.validate();
		contentPane.repaint();

		return 0;
	}

	public synchronized int leerCartaElegida() {
		try {
			pSeleccion.getStartLatch().await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Holiwis");
		int idCartaElegida = PanelCartasElegir.idCartaElegida;
		PanelCartasElegir.idCartaElegida = Integer.MIN_VALUE;
		pSeleccion.setStartLatch(new CountDownLatch(1));
		return idCartaElegida;
	}

	public synchronized int[] obtenerInputCoordenadas(Jugador jugador) {
		int[] coordenadasElegidas = new int[2];
		for (PanelJugador panelJugador : tableros) {
			if (panelJugador.j.equals(jugador)) {
				while (VentanaJueguito.coordenadasElegidas[0] == 0 && VentanaJueguito.coordenadasElegidas[1] == 0) {
					try {
						VentanaJueguito.latchCartaElegida.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		coordenadasElegidas[0] = VentanaJueguito.coordenadasElegidas[0];
		coordenadasElegidas[1] = VentanaJueguito.coordenadasElegidas[1];
		VentanaJueguito.coordenadasElegidas[0] = 0;
		VentanaJueguito.coordenadasElegidas[1] = 0;
		VentanaJueguito.latchCartaElegida = new CountDownLatch(1);
		return coordenadasElegidas;
	}

	public static CountDownLatch getLatchcartaelegida() {
		return latchCartaElegida;
	}

}
