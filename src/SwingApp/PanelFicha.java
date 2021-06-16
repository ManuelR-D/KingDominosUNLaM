package SwingApp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import reyes.Ficha;

public class PanelFicha extends JPanel {

	private static final long serialVersionUID = 5537172421677141208L;
	private Ficha ficha;
	static final int LARGO_CARTA = 160;
	static final int ALTO_CARTA = 80;
	static final int LARGO_FICHA = LARGO_CARTA / 2;
	static final int ALTO_FICHA = ALTO_CARTA;
	private int x, y;
	private BufferedImage bufferFicha;
	double escala;

	public PanelFicha(Ficha f, int y, int x) {
		this.x = x;
		this.y = y;
		this.ficha = f;
		this.escala=1;

		bufferFicha = getTexturaFicha(f);

	}

	public PanelFicha(Ficha f, int y, int x, double escala) {
		this.x = x;
		this.y = y;
		this.ficha = f;
		this.escala=escala;

		bufferFicha = getTexturaFicha(f);

	}

	private BufferedImage getTexturaFicha(Ficha f) {
		BufferedImage imagen = null;
		if (f == null)
			return imagen = null;
		else if (f.getId() == -1)
			return VentanaJueguito.bufferCastillo.getSubimage(0, 0, LARGO_FICHA, ALTO_FICHA);
		else {
			imagen = getTexturaCarta(f.getId() / 2, f.getId() % 2 == 0);
		}
		return imagen;
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
	public void fichaClickeada() {
		if(PanelTableroSeleccion.idCartaElegida == Integer.MIN_VALUE)
			return;
		VentanaJueguito.coordenadasElegidas[0] = x;
		VentanaJueguito.coordenadasElegidas[1] = y;
		VentanaJueguito.getLatchCartaElegida().countDown();
	}

	public Ficha getFicha() {
		return this.ficha;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform affineTransform = new AffineTransform();
//		g2d.scale(escala, escala);
		affineTransform.scale(escala, escala);
		if (ficha != null) {
			int rotacion = ficha.getRotacion() - 1;
//        System.out.println(rotacion);
			if (rotacion != 0) {
				affineTransform.rotate((rotacion) * Math.PI / 2);
				switch (rotacion) {
				case 1:
					affineTransform.translate(0, -bufferFicha.getWidth());
					break;
				case 2:
					affineTransform.translate(-bufferFicha.getHeight(), -bufferFicha.getWidth());
					break;
				case 3:
					affineTransform.translate(-bufferFicha.getHeight(), 0);
					break;

				default:
					break;
				}
			}
		}
		g2d.drawImage(bufferFicha, affineTransform, null);
	}
}
