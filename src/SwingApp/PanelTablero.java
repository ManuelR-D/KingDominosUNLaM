package SwingApp;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Color;
import java.awt.Label;

import reyes.Carta;
import reyes.Ficha;
import reyes.Jugador;
import reyes.Tablero;

public class PanelTablero extends JPanel {
	private Tablero t;
	private Set<Ficha> fichasEnTablero = new HashSet<Ficha>();
	private static final long serialVersionUID = -3826925585139282813L;
	int offset_x = 0;
	int offset_y = 0;
	/**
	 * Create the panel.
	 * 
	 * @throws IOException
	 */
	public PanelTablero(Tablero t) throws IOException {
		
		setLayout(null);
		this.t = t;
		mostrarTablero(t);
		
	}
	public PanelTablero(Tablero t,int offset_x, int offset_y) throws IOException {
		
		setLayout(null);
		this.t = t;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
		mostrarTablero(t);
		
	}
	public void actualizarTablero() throws IOException{
		this.removeAll();
		mostrarTablero(t);
		this.repaint();
	}

	protected void mostrarTablero(Tablero tablero) throws IOException {

		Ficha[][] f = tablero.getTablero();
		for (int i = Math.max(tablero.xMin-1, 0), y = 0; i <= Math.min(tablero.xMax + 1, f.length-1); i++, y++) {
			for (int j = Math.max(tablero.yMin-1, 0), x = 0; j <= Math.min(tablero.yMax + 1, f[i].length - 1); j++, x++) {
				if(!fichasEnTablero.contains(f[i][j])) {
					PanelFicha panelFicha = new PanelFicha(f[i][j]);
					panelFicha.setBounds((x * PanelFicha.LARGO_FICHA),// + offset_x, 
										 (y * PanelFicha.ALTO_FICHA) ,// + offset_y, 
										 PanelFicha.LARGO_FICHA+offset_x, 
										 PanelFicha.ALTO_FICHA+ offset_y);
					cargarPropiedadesFicha(panelFicha);
					add(panelFicha);
					if(f[i][j] != null);
						//fichasEnTablero.add(f[i][j]);
				}
			}
		}
	}
	protected void cargarPropiedadesFicha(PanelFicha panelFicha) {
		// TODO Auto-generated method stub
		
	}

}
