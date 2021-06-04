package SwingApp;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import reyes.Ficha;

public class PanelFicha extends JPanel {

	private Ficha f;
	static final int LARGO_CARTA = 160;
	static final int ALTO_CARTA = 80;
	static final int LARGO_FICHA = LARGO_CARTA / 2;
	static final int ALTO_FICHA = ALTO_CARTA;
	private int x, y;

	public PanelFicha(Ficha f, int y, int x) {
		this.x = x;
		this.y = y;
//		setLayout(null);
		this.f = f;
		if (f == null) {
			JPanel panelVacio = new JPanel();
			panelVacio.setBackground(new Color(200, 200, 200));
			panelVacio.setBorder(BorderFactory.createLineBorder(Color.WHITE));
			panelVacio.setBounds(0, 0, LARGO_FICHA, ALTO_FICHA);
			panelVacio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					fichaClickeada();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					panelVacio.setBackground(new Color(0x63CAA7));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					panelVacio.setBackground(new Color(200, 200, 200));
				}
			});
			this.add(panelVacio);
		} else {
			JLabel pic;
			pic = getTexturaFicha(f);
			pic.setBounds(0, 0, LARGO_FICHA, ALTO_FICHA);
			this.add(pic);
		}
	}

	public void fichaClickeada() {

		VentanaJueguito.coordenadasElegidas[0] = x;
		VentanaJueguito.coordenadasElegidas[1] = y;
		System.out.println(VentanaJueguito.coordenadasElegidas[0] + ";" + VentanaJueguito.coordenadasElegidas[1]);
		VentanaJueguito.getLatchCartaElegida().countDown();
	}

	private JLabel getTexturaFicha(Ficha f) {
		BufferedImage pic = null;
		if (f == null)
			return null;
		else if (f.getId() == -1)
			return new JLabel(new ImageIcon(VentanaJueguito.bufferCastillo.getSubimage(0, 0, LARGO_FICHA, ALTO_FICHA)));
		else {
			pic = getTexturaCarta(f.getId() / 2, f.getId() % 2 == 0);

		}
		ImageIcon ii = new ImageIcon(pic);
		RotatedIcon rt = new RotatedIcon(ii, 90 * (f.getRotacion() - 1));
		return new JLabel(rt);
	}

	private BufferedImage getTexturaCarta(int idCarta, boolean izq) {
		int x = idCarta % 8 == 0 ? 7 : idCarta % 8 - 1;
		int y = (idCarta) / 8 - x / 7;
		// Obtenemos el recorte de la carta elegida
		BufferedImage myPicture = VentanaJueguito.bufferCarta.getSubimage(x * LARGO_CARTA, y * ALTO_CARTA, LARGO_CARTA,
				ALTO_CARTA);
		// Recortamos la carta elegida a la ficha elegida
		return izq ? myPicture.getSubimage(0, 0, LARGO_FICHA, ALTO_FICHA)
				: myPicture.getSubimage(LARGO_FICHA, 0, LARGO_FICHA, ALTO_FICHA);
	}

	public Ficha getFicha() {
		return this.f;
	}

}
