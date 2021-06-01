package SwingApp;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

import reyes.Carta;
import reyes.Ficha;
import reyes.Tablero;
import reyes.TableroSeleccion;

public class PanelCartasElegir extends PanelTablero {
	Ficha fElegida;
	TableroSeleccion tS;
	/**
	 * Create the panel.
	 * 
	 * @throws IOException
	 */
	private Tablero t;
	public static volatile Integer idCartaElegida = Integer.MIN_VALUE;
	private CountDownLatch startLatch = new CountDownLatch(1);

	public PanelCartasElegir(Tablero t,int offset_x,int offset_y) throws IOException {
		super(t,offset_x,offset_y);
		this.t = t;
		
	}
	protected void onRotarCartaClick() {
		fElegida.getCarta().rotarCarta();
		onCartaElegida(fElegida);
	}
	@Override
	protected void cargarPropiedadesFicha(PanelFicha panelFicha) {
		panelFicha.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panelFicha.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
				onCartaElegida(panelFicha.getFicha());
			}
		});
	}
	public synchronized void onCartaElegida(Ficha f) {
		idCartaElegida = f.getId()/2;
		this.fElegida = f;
		this.removeAll();
		TableroSeleccion tS = new TableroSeleccion(4,f.getCarta());
		try {
			mostrarTablero(tS);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JButton btn_rot = new JButton("Rotar");
		btn_rot.setBounds(0,4*PanelFicha.ALTO_CARTA,PanelFicha.LARGO_CARTA*2,PanelFicha.ALTO_CARTA*2);
		this.add(btn_rot);
		btn_rot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onRotarCartaClick();
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
	

}
