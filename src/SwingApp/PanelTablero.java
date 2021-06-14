package SwingApp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import reyes.Ficha;
import reyes.Tablero;

public class PanelTablero extends JPanel {
	private Tablero tablero;
	private Set<Ficha> fichasEnTablero = new HashSet<Ficha>();
	private static final long serialVersionUID = -3826925585139282813L;
	int offset_x = 0;
	int offset_y = 0;
	int tamTableroVisual;
	int numJugador;
	PanelFicha[][] matrizPaneles;
	private Map<String,Color> mapaColores;
//	private int xMaxAux,xMinAux,yMaxAux,yMinAux;
	JLayeredPane panelConDimension;

	public PanelTablero(Tablero tablero, int tamTableroVisual) {
		setLayout(null);
		this.tablero = tablero;
		this.tamTableroVisual = tamTableroVisual;
		matrizPaneles = new PanelFicha[tablero.getTamanio() * 2 - 1][tablero.getTamanio() * 2 - 1];
		
//		xMaxAux=xMinAux=yMaxAux=yMinAux=tablero.getCentro();
		
		mapaColores=new HashMap<String,Color>();
		mapaColores.put("Campo", new Color(251,196,48,150));
		mapaColores.put("Bosque",new Color(37,81,32,150));
		mapaColores.put("Agua",new Color(0,153,215,150));
		mapaColores.put("Pradera",new Color(235,229,74,150));
		mapaColores.put("Oasis",new Color(165,138,94,150));
		mapaColores.put("Mina",new Color(103,97,87,150));	
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.fillRect(0, 0, tamTableroVisual, tamTableroVisual);
	}

	public void reCrearTablero(String nombreJugador) {
		int xMax = tablero.getxMax();
		int xMin = tablero.getxMin();
		int yMax = tablero.getyMax();
		int yMin = tablero.getyMin();

//		xMaxAux=xMax;
//		xMinAux=xMin;
//		yMaxAux=yMax;
//		yMinAux=yMin;
		
		this.removeAll();
		panelConDimension = new JLayeredPane();
		panelConDimension.setLayout(null);
		panelConDimension.setBounds(0, 0, tamTableroVisual, tamTableroVisual);
		this.add(panelConDimension);
		Ficha[][] fichas = tablero.getTablero();

		// Calculos para realizar la escala
		/*
		 * Por ejemplo si puedo colocar como maximo 5 fichas y coloque 2 a la derecha
		 * del castillo entonces tengo que poder colocar dos a mi derecha y dos a la
		 * izquierda del castillo por lo tanto debo mostrar 7 espacios de ficha en
		 * pantalla
		 */
		int fichasMaximas = tablero.getTamanio() + 2;
		double tamFicha = tamTableroVisual / fichasMaximas;
		double escala = tamFicha / VentanaJueguito.LARGO_FICHA;

		int desplVertical = 0;
		int desplHorizontal = 0;
		
		desplVertical=Math.min(tablero.getTamanio()-( xMax-xMin+1), 2);
		desplHorizontal=Math.min(tablero.getTamanio()-(yMax-yMin+1), 2);

		int inicioFilasAMostrar = Math.max(xMin - desplVertical,0);
		int finFilasAMostrar = Math.min(xMax + desplVertical, fichas.length - 1);
		int inicioColumnasAMostrar = Math.max(yMin - desplHorizontal, 0);
		int finColumnasAMostrar = Math.min(yMax + desplHorizontal, fichas.length - 1);
		int largo = (int) (PanelFicha.LARGO_FICHA * escala);
		int alto = (int) (PanelFicha.ALTO_FICHA * escala);

		int centradoLargo = 0;
		;
		int centradoAlto = 0;
		/*
		 * Estas variables son para acomodar el tablero cuando se haya llegado al limite
		 * de construccion(por ej 5x5)
		 */
		if (xMax - xMin == tablero.getTamanio() - 1) {
			centradoLargo = largo;
		}
		if (yMax - yMin == tablero.getTamanio() - 1) {
			centradoAlto = alto;
		}

		for (int i = inicioFilasAMostrar, y = 0; i <= finFilasAMostrar; i++, y++) {
			for (int j = inicioColumnasAMostrar, x = 0; j <= finColumnasAMostrar; j++, x++) {
				PanelFicha panelFicha = new PanelFicha(fichas[i][j], i, j, escala);
				matrizPaneles[i][j] = panelFicha;
				panelFicha.setBounds((x * alto) + centradoAlto, (y * largo) + centradoLargo, largo, alto);
				panelFicha.setBorder(BorderFactory.createLineBorder(Color.black));
				panelFicha.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						panelFicha.fichaClickeada();
					}
				});
				panelConDimension.add(panelFicha, 0);

			}
		}
		JLabel nombre = new JLabel(nombreJugador);
		nombre.setBounds(0, 0, tamTableroVisual, alto);
		nombre.setBackground(Color.red);
		nombre.setForeground(Color.red);
		panelConDimension.add(nombre, 1);
		this.repaint();
	}

	public void pintarFicha(int i, int j) {
		PanelFicha pFicha = matrizPaneles[i][j];
		/*
		 * Por alguna razon llegan panelFicha nulo, y lo mas raro es que a veces llegan
		 * panelFicha no nulo pero con pFicha.getFicha igual a null
		 */
		if (pFicha != null && pFicha.getFicha() != null) {
			String tipo = pFicha.getFicha().getTipo();


			Color color=mapaColores.get(tipo);
			pFicha.setBorder(BorderFactory.createLineBorder(color, VentanaJueguito.LARGO_FICHA));
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
