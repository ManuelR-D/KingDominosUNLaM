package SwingApp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.io.IOException;
import java.util.List;

import reyes.Jugador;
import reyes.Partida;
import java.awt.GridLayout;

public class VentanaJueguito extends JFrame {

	private static final long serialVersionUID = 4460429712849713216L;
	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void mostrarPartida(Partida p) {
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
	public VentanaJueguito(Partida p) throws IOException{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1920, 1080);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		List<Jugador> jugadores = p.getJugadores();
		contentPane.setLayout(new GridLayout(2, jugadores.size(), 50, 50));
		for (Jugador jugador : jugadores) {
			JPanel panel = new PanelJugador(jugador);
			contentPane.add(panel);
		}
		
		this.setTitle(p.getJugadores().get(0).nombre);
	}
}
