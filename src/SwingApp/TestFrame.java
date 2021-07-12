package SwingApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import reyes.Ficha;

public class TestFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
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
	private File texturaCarta = new File("./assets/mazos/highTest.png");
	private File texturaCastilloAmarillo = new File("./assets/castilloAmarillo.png");
	private File texturaCastilloAzul = new File("./assets/castilloAzul.png");
	private File texturaCastilloRojo = new File("./assets/castilloRojo.png");
	private File texturaCastilloVerde = new File("./assets/castilloVerde.png");
	private Sonido sonido;
	static File texturaVacia = new File("./assets/vacio.png");
	static BufferedImage bufferCastilloAmarillo;
	static BufferedImage bufferCastilloAzul;
	static BufferedImage bufferCastilloRojo;
	static BufferedImage bufferCastilloVerde;
	static BufferedImage bufferCarta;
	static BufferedImage bufferVacio;
	
	public TestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		try {
			VentanaJueguito.bufferCastilloAmarillo = ImageIO.read(texturaCastilloAmarillo);
			VentanaJueguito.bufferCastilloAzul = ImageIO.read(texturaCastilloAzul);
			VentanaJueguito.bufferCastilloRojo = ImageIO.read(texturaCastilloRojo);
			VentanaJueguito.bufferCastilloVerde = ImageIO.read(texturaCastilloVerde);
			VentanaJueguito.bufferCarta = ImageIO.read(texturaCarta);
			VentanaJueguito.bufferVacio = ImageIO.read(texturaVacia);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error generando imagenes clase VentanaJueguito");
		}
		Ficha f = null;
		PanelFicha pF = null;
		
		for(int i = 2; i <= 98; i++) {
			f = new Ficha("Campo", 0,0,0,i,null);
			pF = new PanelFicha(f,0,0,1,1);
			pF.setBounds((i-2)%16*PanelFicha.getTamFicha(),((i-2)/16)*PanelFicha.getTamFicha(),PanelFicha.getTamFicha(),PanelFicha.getTamFicha());
			
			contentPane.add(pF);
		}
	}

}
