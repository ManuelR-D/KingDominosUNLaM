package SwingApp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import reyes.Ficha;

public class PanelFicha extends JPanel {

	private File texturaCarta = new File("./assets/highTest.png");
	private File texturaVacia = new File("./assets/vacio.png");
	private File texturaCastillo = new File("./assets/castillo.png");
	static final int LARGO_CARTA = 160;
	static final int ALTO_CARTA = 80;
	static final int LARGO_FICHA = LARGO_CARTA/2;
	static final int ALTO_FICHA = ALTO_CARTA;
	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public PanelFicha(Ficha f) throws IOException {
		JLabel pic = getTexturaFicha(f);
		pic.setBounds(0, 0, LARGO_FICHA, ALTO_FICHA);
		this.add(pic);
	}

	private JLabel getTexturaFicha(Ficha f) throws IOException {
		BufferedImage pic = null;
		if (f == null)
			return new JLabel(new ImageIcon(ImageIO.read(texturaVacia).getSubimage(0, 0, LARGO_FICHA, ALTO_FICHA)));
		else if (f.idFicha == -1)
			return new JLabel(new ImageIcon(ImageIO.read(texturaCastillo).getSubimage(0, 0, LARGO_FICHA, ALTO_FICHA)));
		else
			pic = getTexturaCarta(f.idFicha / 2, f.idFicha % 2 == 0);
		ImageIcon ii = new ImageIcon(pic);
		RotatedIcon rt = new RotatedIcon(ii,90*(f.rotacion-1));
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

}
