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
import reyes.Tablero;

public class PanelTablero extends JPanel {

	private static final long serialVersionUID = -3826925585139282813L;
	/**
	 * Create the panel.
	 * 
	 * @throws IOException
	 */
	public PanelTablero(Tablero t) throws IOException {
		
		setLayout(null);
		mostrarTablero(t);
		
	}

	private void mostrarTablero(Tablero tablero) throws IOException {
		int offset_x = 50; //offset para dejar el nombre del jugador
		int offset_y = 50;
		Ficha[][] f = tablero.getTablero();
		for (int i = tablero.xMin, y = 0; i <= tablero.xMax; i++, y++) {
			for (int j = tablero.yMin, x = 0; j <= tablero.yMax; j++, x++) {
				JPanel panelFicha = new PanelFicha(f[i][j]);
				panelFicha.setBounds(x * PanelFicha.LARGO_FICHA + offset_x, 
									 y * PanelFicha.ALTO_FICHA + offset_y, 
									 PanelFicha.LARGO_FICHA, 
									 PanelFicha.ALTO_FICHA);
				this.add(panelFicha);
			}
		}
	}

}
