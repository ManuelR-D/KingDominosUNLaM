package SwingApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import reyes.Carta;
import reyes.Ficha;
import reyes.TableroSeleccion;

public class PanelTableroSeleccion extends JPanel {
	public static int idCartaElegida;
	Carta cartaElegida;
	private TableroSeleccion tableroSeleccion;
	private CountDownLatch startLatch = new CountDownLatch(1);

	public PanelTableroSeleccion(TableroSeleccion tablero, int offset_x, int offset_y){
		this.setLayout(null);
		this.tableroSeleccion = tablero;
		mostrarTablero(tablero);
	}

	protected void onRotarCartaClick() {
		cartaElegida.rotarCarta();
		onCartaElegida(cartaElegida);
	}

	protected void cargarPropiedadesFicha(PanelFicha panelFicha) {
		panelFicha.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panelFicha.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
				onCartaElegida(panelFicha.getFicha().getCarta());
			}
		});
	}

	public synchronized void onCartaElegida(Carta carta) {
		this.cartaElegida = carta;
		idCartaElegida=carta.getId();
		this.removeAll();
		TableroSeleccion tS = new TableroSeleccion(carta);
			mostrarTablero(tS);
		JButton btn_rot = new JButton("Rotar");
		btn_rot.setBounds(0, 5 * PanelFicha.ALTO_FICHA, PanelFicha.LARGO_FICHA * 3, PanelFicha.ALTO_FICHA);
		this.add(btn_rot);
		btn_rot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onRotarCartaClick();
				cartaElegida.moverCarta(-1,-1);
			}
		});
		this.repaint();
		startLatch.countDown();
	}

	public CountDownLatch getStartLatch() {
		return startLatch;
	}

	public void setStartLatch(CountDownLatch countDownLatch) {
		startLatch = countDownLatch;
	}

	protected void mostrarTablero(TableroSeleccion tablero){

		Ficha[][] f = tablero.getTablero();
		for (int i = 0, y = 0; i < 4; i++, y++) {
			for (int j = 0, x = 0; j < 3; j++, x++) {
				if (f[i][j] != null) {
					PanelFicha panelFicha = new PanelFicha(f[i][j], i, j);
					panelFicha.setBounds((x * PanelFicha.LARGO_FICHA), // + offset_x,
							(y * PanelFicha.ALTO_FICHA), // + offset_y,
							PanelFicha.LARGO_FICHA, PanelFicha.ALTO_FICHA);
					cargarPropiedadesFicha(panelFicha);
					add(panelFicha);
				}
			}
		}
	}

	public void actualizarTablero(){
		this.removeAll();
		mostrarTablero(tableroSeleccion);
		this.repaint();
	}

}
