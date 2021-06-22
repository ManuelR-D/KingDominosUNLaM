package SwingApp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
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
	int fMin = 0;
	int fMax = 0;
	int cMax = 0;
	int cMin = 0;
	int tamTableroVisual;
	int numJugador;
	PanelFicha[][] matrizPaneles;

//	private Map<String, Color> mapaColores;
//	private int xMaxAux,xMinAux,yMaxAux,yMinAux;
	JLayeredPane panelConDimension;
	private double escala;
	private int inicioFilasAMostrar;
	private int inicioColumnasAMostrar;
	private int largo;
	private int alto;
	private HashMap<String, Color> mapaColoresComplementarios;

	public PanelTablero(Tablero tablero, int tamTableroVisual) {
		setLayout(null);
		this.tablero = tablero;
		this.tamTableroVisual = tamTableroVisual;

//		xMaxAux=xMinAux=yMaxAux=yMinAux=tablero.getCentro();

//		mapaColores = new HashMap<String, Color>();
//		mapaColores.put("Campo", new Color(251, 196, 48, 150));
//		mapaColores.put("Bosque", new Color(37, 81, 32, 150));
//		mapaColores.put("Agua", new Color(0, 153, 215, 150));
//		mapaColores.put("Pradera", new Color(235, 229, 74, 150));
//		mapaColores.put("Oasis", new Color(165, 138, 94, 150));
//		mapaColores.put("Mina", new Color(103, 97, 87, 150));

		mapaColoresComplementarios = new HashMap<String, Color>();
		mapaColoresComplementarios.put("Campo", Color.white);
		mapaColoresComplementarios.put("Bosque", Color.orange);
		mapaColoresComplementarios.put("Agua", Color.white);
		mapaColoresComplementarios.put("Pradera", Color.white);
		mapaColoresComplementarios.put("Oasis", Color.magenta);
		mapaColoresComplementarios.put("Mina", Color.green);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color cAnterior=g.getColor();
		g.setColor(new Color(0xAB7632));
		g.fillRect(0, 0, tamTableroVisual, tamTableroVisual);
		g.setColor(cAnterior);
	}

	public void pintarFicha(int fila, int columna, int acumPuntos, int cantCoronas, int indice) {
		PanelFicha pFicha = matrizPaneles[fila][columna];
		/*
		 * Por alguna razon llegan panelFicha nulo, y lo mas raro es que a veces llegan
		 * panelFicha no nulo pero con pFicha.getFicha igual a null
		 */
		if (pFicha != null && pFicha.getFicha() != null) {

			JLabel puntaje = new JLabel(+acumPuntos + "*" + cantCoronas);
			int largo = (int) (PanelFicha.LARGO_FICHA * escala);
			int alto = (int) (PanelFicha.ALTO_FICHA * escala);
			puntaje.setForeground(Color.white);
			puntaje.setBackground(Color.black);
			puntaje.setBounds(0, 0, largo, alto);

			Color colorComplementario = mapaColoresComplementarios.get(pFicha.getFicha().getTipo());
			puntaje.setForeground(Color.black);
			puntaje.setForeground(colorComplementario);
			puntaje.setBorder(BorderFactory.createLineBorder(Color.black));
			pFicha.add(puntaje);
			repaint();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("ERROR tablero:" + indice + " fila:" + fila + " columna:" + columna);
		}
	}

	public void actualizarTablero(String nombre, int fila, int columna) {
		int fMax = tablero.getFMax();
		int fMin = tablero.getFMin();
		int cMax = tablero.getCMax();
		int cMin = tablero.getCMin();

		Ficha[][] fichas = tablero.getTablero();
		boolean necesitaRedibujar = this.fMax != fMax || this.fMin != fMin || this.cMax != cMax || this.cMin != cMin;
		if (necesitaRedibujar) {
			reCrearTablero(nombre);
		} else {
			insertarPFichaEnTablero(fila, columna, fichas);

			int rotacion = fichas[fila][columna].getRotacion();
			switch (rotacion) {

			case 1:
				columna++;
				break;
			case 2:
				fila++;
				break;
			case 3:
				columna--;
				break;
			case 4:
				fila--;
				break;
			}

			insertarPFichaEnTablero(fila, columna, fichas);
		}

	}

	public void reCrearTablero(String nombreJugador) {
		int fMax = tablero.getFMax();
		int fMin = tablero.getFMin();
		int cMax = tablero.getCMax();
		int cMin = tablero.getCMin();

		Ficha[][] fichas = tablero.getTablero();
		this.removeAll();
		matrizPaneles = new PanelFicha[tablero.getTamanio() * 2 - 1][tablero.getTamanio() * 2 - 1];
		panelConDimension = new JLayeredPane();
		panelConDimension.setLayout(null);
		panelConDimension.setBounds(0, 0, tamTableroVisual, tamTableroVisual);
		this.add(panelConDimension);
		fichasEnTablero.clear();
		// Calculos para realizar la escala
		/*
		 * Por ejemplo si puedo colocar como maximo 5 fichas y coloque 2 a la derecha
		 * del castillo entonces tengo que poder colocar dos a mi derecha y dos a la
		 * izquierda del castillo por lo tanto debo mostrar 7 espacios de ficha en
		 * pantalla
		 */
		int fichasMaximas = tablero.getTamanio() + 2;
		double tamFicha = tamTableroVisual / fichasMaximas;
		escala = tamFicha / VentanaJueguito.LARGO_FICHA;

		int desplVertical = Math.min(tablero.getTamanio() - (fMax - fMin + 1), 2);
		int desplHorizontal = Math.min(tablero.getTamanio() - (cMax - cMin + 1), 2);

		inicioFilasAMostrar = Math.max(fMin - desplVertical, 0);
		int finFilasAMostrar = Math.min(fMax + desplVertical, fichas.length - 1);
		inicioColumnasAMostrar = Math.max(cMin - desplHorizontal, 0);
		int finColumnasAMostrar = Math.min(cMax + desplHorizontal, fichas.length - 1);
		largo = (int) (PanelFicha.LARGO_FICHA * escala);
		alto = (int) (PanelFicha.ALTO_FICHA * escala);

		int centradoAlto = (finFilasAMostrar - inicioFilasAMostrar == tablero.getTamanio() - 1) ? alto : 0;

		int centradoLargo = (finColumnasAMostrar - inicioColumnasAMostrar== tablero.getTamanio() - 1) ? largo : 0;
		/*
		 * Estas variables son para acomodar el tablero cuando se haya llegado al limite
		 * de construccion(por ej 5x5)
		 * 
		 */

		for (int i = inicioFilasAMostrar, y = 0; i <= finFilasAMostrar; i++, y++) {
			for (int j = inicioColumnasAMostrar, x = 0; j <= finColumnasAMostrar; j++, x++) {

				PanelFicha panelFicha = new PanelFicha(fichas[i][j], i, j, escala);
				matrizPaneles[i][j] = panelFicha;
				panelFicha.setBounds((x * largo) + centradoLargo, (y * alto) + centradoAlto, largo, alto);
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

		// El nombre tiene que redibujarse al final, siempre, en caso contrario queda
		// "debajo"
		// de las fichas vacias.Estaria bueno cambiar esto

		JLabel nombre = new JLabel(nombreJugador);
		nombre.setBounds(0, 0, tamTableroVisual, alto);
//		nombre.setBackground(Color.red);
		nombre.setForeground(new Color(0x40FFDE));
		panelConDimension.add(nombre, 1);

		this.fMax = fMax;
		this.fMin = fMin;
		this.cMax = cMax;
		this.cMin = cMin;

	}

	private void insertarPFichaEnTablero(int fila, int columna, Ficha[][] fichas) {
		int x = (columna - inicioColumnasAMostrar);
		int y = (fila - inicioFilasAMostrar);
		int centradoAlto = (fMax - fMin == tablero.getTamanio() - 1) ? alto : 0;
		int centradoLargo = (cMax - cMin == tablero.getTamanio() - 1) ? largo : 0;
//		System.out.println("fila:"+fila+" columna:"+columna+" x:"+x+" y:"+y);
//		System.out.println("CentradoLargo:"+centradoLargo+" CentradoAlto:"+centradoAlto);
		PanelFicha panelFicha = new PanelFicha(fichas[fila][columna], fila, columna, escala);
		matrizPaneles[fila][columna] = panelFicha;
		panelFicha.setBounds((x * largo) + centradoLargo, (y * alto) + centradoAlto, largo, alto);
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
