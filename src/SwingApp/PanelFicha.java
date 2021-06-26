package SwingApp;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import reyes.Carta;
import reyes.Ficha;

public class PanelFicha extends JPanel {

	private static final long serialVersionUID = 5537172421677141208L;
	private Ficha ficha;
	static final int LARGO_CARTA = 160;
	static final int ALTO_CARTA = 80;
	static final int LARGO_FICHA = LARGO_CARTA / 2;
	static final int ALTO_FICHA = ALTO_CARTA;
	private static final int LARGO_CORONA = 22;
	private int x, y;
	private BufferedImage bufferFicha;
	double escala;
	private int rotacion;
	private boolean transparente;

	public PanelFicha(Ficha f, int y, int x) {
		this.x = x;
		this.y = y;
		this.ficha = f;
		this.escala = 1;

		bufferFicha = getTexturaFicha(f);
	}

	public PanelFicha(Ficha f, int y, int x, double escala) {
		this.x = x;
		this.y = y;
		this.ficha = f;
		this.escala = escala;
		bufferFicha = getTexturaFicha(f);

	}

	private BufferedImage getTexturaFicha(Ficha f) {
		BufferedImage imagen = null;
		
		if (f == null)
			return VentanaJueguito.bufferVacio;
		else if (f.getId() < 0) {
			int indice = f.getId();
			BufferedImage castillo = null;
			switch (indice) {
			case -1:
				castillo = VentanaJueguito.bufferCastilloAmarillo;
				break;
			case -2:
				castillo = VentanaJueguito.bufferCastilloAzul;
				break;
			case -3:
				castillo = VentanaJueguito.bufferCastilloRojo;
				break;
			case -4:
				castillo = VentanaJueguito.bufferCastilloVerde;
				break;
			}
			return castillo;
		} else {
			rotacion = f.getRotacion()-1;
			int idFicha = f.getId() - 2;
			/*
			 * Nos traemos una copia de bufferCarta, puesto que vamos a dibujar las coronas.
			 * Si trabajaramos sobre la referencia directa de VentanaJueguito.bufferCarta,
			 * perderíamos la textura original. Esto genera un bug para los mazos
			 * personalizados que pueden reutilizar la misma textura con coronas distintas.
			 */
			ColorModel cm = VentanaJueguito.bufferCarta.getColorModel();
			boolean isAlphaPremultiplied = VentanaJueguito.bufferCarta.isAlphaPremultiplied();
			/*
			 * Antes de hacer la copia, primero obtenemos la subimagen que vamos a cortar.
			 * De esta manera nos evitamos copiar las 96 fichas para solo usar una.
			 * Implementar esto bajó el render de 240ms a 40ms
			 */
			imagen = VentanaJueguito.bufferCarta.getSubimage((idFicha % 16) * LARGO_FICHA,
					(idFicha == 96 ? idFicha / 16 - 1 : idFicha / 16) * (ALTO_FICHA), LARGO_FICHA, ALTO_FICHA);
			// Dado que la imagen fue cortada, hay que obtener un raster compatible
			WritableRaster raster = imagen.copyData(imagen.getRaster().createCompatibleWritableRaster());
			// Clonamos la imagen de la ficha para luego dibujarle las coronas
			imagen = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}

		// Dibujamos las coronas. Esto se podria hacer tambien en paintComponent, pero
		// seria mas ineficiente
		// dado que se dibujarian encima de la textura cada vez que se llamara a
		// repaint(); es decir, se
		// redibujaría la textura Y las coronas en cada llamado. Por esto optamos por
		// clonar la textura original,
		// dibujarle las coronas al clon, y guardar el clon en bufferFicha.
		// De esta manera fusionamos ambas texturas en bufferFicha, y solo dibujamos eso
		// en paintComponent.
		// Esto bajo el render de 40ms a 29ms, testeando el peor caso (3 coronas en cada
		// ficha, 4 talberos completos)

		Graphics2D g2d = (Graphics2D) imagen.getGraphics();

		if (ficha != null && ficha.getCantCoronas() > 0) {
			int cantidadCoronas = ficha.getCantCoronas();
			if (ficha.getId() % 2 != 0)
				g2d.translate((LARGO_FICHA - LARGO_CORONA * (cantidadCoronas)) * escala - 7, 5);
			else
				g2d.translate(7, 5);
			AffineTransform scale = new AffineTransform();
			scale.scale(escala, escala);
			for (int i = 0; i < cantidadCoronas; i++) {
				// System.out.println(i);
				g2d.drawImage(VentanaJueguito.bufferCorona, scale, null);
				g2d.translate(LARGO_CORONA * escala, 0);
			}
		}
		return imagen;
	}

	public void fichaClickeada(int xMouse, int yMouse) {
		if (PanelTableroSeleccion.idCartaElegida == Integer.MIN_VALUE)
			return;
		VentanaJueguito.coordenadasElegidas[0] = x;
		VentanaJueguito.coordenadasElegidas[1] = y;
		VentanaJueguito.coordenadasElegidas[2] = xMouse;
		VentanaJueguito.coordenadasElegidas[3] = yMouse;
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
		affineTransform.scale(escala, escala);
		if (ficha != null) {
			rotacion = ficha.getRotacion() - 1;	
		}
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
		g2d.drawImage(bufferFicha, affineTransform, null);
	}

	public void mouseEncima(double escala) {
		Carta c = VentanaJueguito.mainFrame.pSeleccion.getCartaElegida();
		if (c == null || ficha != null )
			return;
		bufferFicha = getTexturaFicha(c.getFichas()[0]);
		int turno = VentanaJueguito.getTurnoJugador();
		//VentanaJueguito.mainFrame.tableros.tableros.get(turno)c.
		//repaint();
	}

	public void mouseAfuera() {
		if(ficha != null)
			return;
		bufferFicha = getTexturaFicha(null);
		repaint();
	}
	public int getFila() {
		return y;
	}
	public int getColumna() {
		return x;
	}

	public void pintarPreview(Ficha ficha) {
		if(bufferFicha != VentanaJueguito.bufferVacio )
			return;
		bufferFicha = getTexturaFicha(ficha);
		repaint();
	}
}
