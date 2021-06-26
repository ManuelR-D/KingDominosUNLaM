package SwingApp;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import reyes.Carta;
import reyes.Ficha;

public class PanelTableroSeleccion extends JPanel {

	private static final long serialVersionUID = 6840011839081352510L;
	private CountDownLatch startLatch = new CountDownLatch(1);
	Carta cartaElegida;
	public static volatile int idCartaElegida;
	
	public PanelTableroSeleccion(List<Carta> cartasAElegir) {
		int largo = VentanaJueguito.LARGO_FICHA;
		int alto = VentanaJueguito.ALTO_FICHA;
		setLayout(null);
		for (int i = 0, x = 0; i < cartasAElegir.size(); i++, x++) {
			Ficha[] fichasActuales = new Ficha[2];
			if(cartasAElegir.get(i)!=null) {
				cartasAElegir.get(i).setDefault();
				 fichasActuales= cartasAElegir.get(i).getFichas();				
			}else {
				fichasActuales[0]=null;
				fichasActuales[1]=null;
			}
			for (int j = 0, y = 0; j < 2; j++, y++) {
				PanelFicha ficha = new PanelFicha(fichasActuales[j], y, x);
				ficha.setBounds(y * largo, x * alto, largo, alto);
				ficha.setBackground(new Color(0x614828));
				ficha.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(ficha.getFicha() != null)
							onCartaElegida(ficha.getFicha().getCarta());
					}
				});
				this.add(ficha);
			}
		}
	}

	public CountDownLatch getStartLatch() {
		return startLatch;
	}
	public Carta getCartaElegida() {
		return cartaElegida;
	}

	public void setStartLatch(CountDownLatch countDownLatch) {
		startLatch = countDownLatch;
	}
	
	protected void onRotarCartaClick() {
		cartaElegida.rotarCarta();
		onCartaElegida(cartaElegida);
		Sonido.playCartaSeleccionada();
	}

	public synchronized void onCartaElegida(Carta carta) {
		this.cartaElegida = carta;
		idCartaElegida = carta.getId();
		this.removeAll();
		

		int largo = VentanaJueguito.LARGO_FICHA;
		int alto = VentanaJueguito.ALTO_FICHA;
		Ficha[] fichas=carta.getFichas();
		int rotacion=fichas[0].getRotacion();
		
		setLayout(null);
		
		int x1 = 0,y1=0,x2=0,y2=0;
		switch (rotacion) {
		case 1:
			x1=0;
			y1=0;
			x2=largo;
			y2=0;
			break;
		case 2:
			x1=0;
			y1=0;
			x2=0;
			y2=alto;
			break;
		case 3:
			x1=largo;
			y1=0;
			x2=0;
			y2=0;
			break;
		case 4:
			x1=0;
			y1=largo;
			x2=0;
			y2=0;
			break;

		default:
			break;
		}

		PanelFicha ficha1 = new PanelFicha(fichas[0], 0, 0);
		ficha1.setBorder(BorderFactory.createLineBorder(Color.orange,5));
		ficha1.setBounds(x1,y1, largo, alto);
		this.add(ficha1);
		
		PanelFicha ficha2 = new PanelFicha(fichas[1], 0, 0);
		ficha2.setBounds(x2,y2, largo, alto);
		this.add(ficha2);
		ImageIcon rotarIcon = new ImageIcon("./assets/rotar.png");
		JButton boton = new JButton(rotarIcon);
		
		boton.setBounds(0, 3 * largo, largo * 2, alto);
		this.add(boton);
		this.setBackground(new Color(0x614828));

		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onRotarCartaClick();
			}
		});
		this.repaint();
		startLatch.countDown();
	}

}
