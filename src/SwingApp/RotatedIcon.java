package SwingApp;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import javax.swing.Icon;

/**
 * Código abierto, créditos a Rob Camick
 * https://tips4java.wordpress.com/2009/04/06/rotated-icon/ The RotatedIcon
 * Permite cambiar la orientación de un ícono en una cantidad de grados.
 * Sólo se puede rotar en relación al centro del mismo.
 */
public class RotatedIcon implements Icon {

	private Icon icon;
	private double degrees;

	/**
	 * Rota el icono una cantidad determinada de grados
	 * @param icon    the Icon to rotate
	 * @param degrees the degrees of rotation
	 */
	public RotatedIcon(Icon icon, double degrees) {
		this.icon = icon;
		this.degrees = degrees;
	}

	/**
	 * Gets the width of this icon.
	 *
	 * @return the width of the icon in pixels.
	 */
	@Override
	public int getIconWidth() {
		double radians = Math.toRadians(degrees);
		double sin = Math.abs(Math.sin(radians));
		double cos = Math.abs(Math.cos(radians));
		int width = (int) Math.floor(icon.getIconWidth() * cos + icon.getIconHeight() * sin);
		return width;
	}

	/**
	 * Gets the height of this icon.
	 *
	 * @return the height of the icon in pixels.
	 */
	@Override
	public int getIconHeight() {
		double radians = Math.toRadians(degrees);
		double sin = Math.abs(Math.sin(radians));
		double cos = Math.abs(Math.cos(radians));
		int height = (int) Math.floor(icon.getIconHeight() * cos + icon.getIconWidth() * sin);
		return height;

	}

	/**
	 * Paint the icons of this compound icon at the specified location
	 *
	 * @param c The component on which the icon is painted
	 * @param g the graphics context
	 * @param x the X coordinate of the icon's top-left corner
	 * @param y the Y coordinate of the icon's top-left corner
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();

		int cWidth = icon.getIconWidth() / 2;
		int cHeight = icon.getIconHeight() / 2;

		g2.setClip(x, y, getIconWidth(), getIconHeight());
		g2.translate((getIconWidth() - icon.getIconWidth()) / 2, (getIconHeight() - icon.getIconHeight()) / 2);
		g2.rotate(Math.toRadians(degrees), x + cWidth, y + cHeight);
		icon.paintIcon(c, g2, x, y);

		g2.dispose();
	}
}