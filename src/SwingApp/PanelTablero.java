package SwingApp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
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


	public PanelTablero(Tablero tablero, int tamTableroVisual) {
		setLayout(null);
		this.tablero = tablero;
		this.tamTableroVisual = tamTableroVisual;
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.fillRect(0, 0, tamTableroVisual, tamTableroVisual);
	}

	public void actualizarTablero() {
		this.removeAll();
		
		Ficha[][] fichas = tablero.getTablero();
		
		//Calculos para realizar la escala
		/*  
		 * Por  ejemplo si puedo colocar como maximo 5 fichas y coloque 2 a la derecha del castillo
		 *  entonces tengo que poder colocar dos a mi derecha y dos a la izquierda del castillo
		 *  por lo tanto debo mostrar 7 espacios de ficha en pantalla
		 */
		int fichasMaximas=tablero.getTamanio()+2;
		double tamFicha=tamTableroVisual/fichasMaximas;
		double escala=tamFicha/VentanaJueguito.LARGO_FICHA;

		int xMax = tablero.getxMax();
		int xMin = tablero.getxMin();
		int yMax = tablero.getyMax();
		int yMin = tablero.getyMin();
		
		int desplAbj=(xMax-tablero.getCentro());
		if(desplAbj>2) {
			desplAbj=4-desplAbj;
		}else {
			desplAbj=2;
		}

		int desplArr=(tablero.getCentro()-xMin);
		if(desplArr>2) {
			desplArr=4-desplArr;
		}else {
			desplArr=2;
		}
		
		int desplDer=(yMax-tablero.getCentro());
		if(desplDer>2) {
			desplDer=4-desplDer;
		}else {
			desplDer=2;
		}
		
		int desplIzq=(tablero.getCentro()-yMin);
		if(desplIzq>2) {
			desplIzq=4-desplIzq;
		}else {
			desplIzq=2;
		}
		
		
		int inicioFilasAMostrar = Math.max(xMin-desplAbj, 0);
		int finFilasAMostrar = Math.min(xMax+desplArr, fichas.length - 1);
		int inicioColumnasAMostrar = Math.max(yMin-desplDer, 0);
		int finColumnasAMostrar = Math.min(yMax+desplIzq, fichas.length - 1);
		int largo = (int) (PanelFicha.LARGO_FICHA * escala);
		int alto = (int) (PanelFicha.ALTO_FICHA * escala);
		
		int centradoLargo=0;;
		int centradoAlto=0;
		/*
		 * Estas variables son para acomodar el tablero cuando se haya llegado al limite de construccion(por ej 5x5)
		 */
		if(xMax-xMin==tablero.getTamanio()-1) {
			centradoLargo=largo;
		}
		if(yMax-yMin==tablero.getTamanio()-1) {
			centradoAlto=alto;
		}
		
		
		for (int i = inicioFilasAMostrar, y = 0; i <= finFilasAMostrar; i++, y++) {
			for (int j = inicioColumnasAMostrar, x = 0; j <= finColumnasAMostrar; j++, x++) {
				PanelFicha panelFicha = new PanelFicha(fichas[i][j], i, j, escala);
				panelFicha.setBounds((x * alto)+centradoAlto,(y * largo)+centradoLargo, largo, alto);
				panelFicha.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						panelFicha.fichaClickeada();
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						panelFicha.setBorder(BorderFactory.createLineBorder(Color.MAGENTA,5));
					}
					@Override
					public void mouseExited(MouseEvent e) {
						panelFicha.setBorder(null);
					}
				});
				add(panelFicha);

			}
		}
		this.repaint();
	}

}
