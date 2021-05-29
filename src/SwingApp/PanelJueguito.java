package SwingApp;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Label;
import reyes.Ficha;
import reyes.Jugador;

public class PanelJueguito extends JPanel {

	private static final long serialVersionUID = -3826925585139282813L;
	private File texturaCarta = new File("./assets/test.png");
	private File texturaVacia = new File("./assets/vacio.png");
	private File texturaCastillo = new File("./assets/castillo.png");
	private static final int LARGO_CARTA = 160;
	private static final int ALTO_CARTA = 80;
	private static final int LARGO_FICHA = 80;
	private static final int ALTO_FICHA = 80;
	/**
	 * Create the panel.
	 * 
	 * @throws IOException
	 */
	public PanelJueguito(Jugador j) throws IOException {
		setBackground(Color.WHITE);
		setLayout(null);
		Label nombreJugador = new Label(j.nombre);
		nombreJugador.setBounds(50, 0, 100, 25);
		this.add(nombreJugador);
		
		mostrarTablero(j);
		
	}

	private JLabel getTexturaFicha(Ficha f) throws IOException {
		BufferedImage pic = null;
		if (f == null)
			return new JLabel(new ImageIcon(ImageIO.read(texturaVacia)));
		else if (f.idFicha == -1)
			return new JLabel(new ImageIcon(ImageIO.read(texturaCastillo)));
		else
			pic = getTexturaCarta(f.idFicha / 2, f.idFicha % 2 == 0);
		ImageIcon ii = new ImageIcon(pic);
		RotatedIcon rt = null;
		rt = new RotatedIcon(ii,90*(f.rotacion-1));
		return new JLabel(rt);
	}

	private BufferedImage getTexturaCarta(int idCarta, boolean izq) throws IOException {
		int x = idCarta % 8 == 0 ? 7 : idCarta % 8 - 1;
		int y = (idCarta) / 8 - x/7;
		// Obtenemos el recorte de la carta elegida
		BufferedImage myPicture = ImageIO.read(texturaCarta).getSubimage(x * LARGO_CARTA, 
																		 y * ALTO_CARTA, 
																			 LARGO_CARTA,
																			 ALTO_CARTA);
		// Recortamos la carta elegida a la ficha elegida
		return izq ? myPicture.getSubimage(0, 0, LARGO_FICHA, ALTO_FICHA) 
				: myPicture.getSubimage(LARGO_FICHA, 0, LARGO_FICHA, ALTO_FICHA);
	}

	private void mostrarTablero(Jugador jugador) throws IOException {
		int offset_x = 50;
		int offset_y = 50;
		Ficha[][] f = jugador.tablero.getTablero();
		for (int i = jugador.tablero.xMin, y = 0; i <= jugador.tablero.xMax; i++, y++) {
			for (int j = jugador.tablero.yMin, x = 0; j <= jugador.tablero.yMax; j++, x++) {
				JLabel picLabel = getTexturaFicha(f[i][j]);
				picLabel.setBounds(x * LARGO_FICHA + offset_x, y * ALTO_FICHA + offset_y, LARGO_FICHA, ALTO_FICHA);
				this.add(picLabel);
			}
		}
	}

}
