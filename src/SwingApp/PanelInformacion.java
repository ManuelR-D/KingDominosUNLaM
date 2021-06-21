package SwingApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import reyes.Ficha;
import reyes.Jugador;
import reyes.Tablero;

public class PanelInformacion extends JPanel {
	int i = 0;

	private static final long serialVersionUID = -6580547458892714155L;
	JTextArea info;
	private List<Jugador> jugadores;

	PanelInformacion(List<Jugador> jugadores) {
		this.jugadores=jugadores;
		setLayout(new BorderLayout());
		info = new JTextArea();
		info.setEditable(false);
		info.setBackground(new Color(0xE3BB86));
		info.setForeground(Color.WHITE);
		this.add(info, BorderLayout.CENTER);
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(jugadores.size(), 1));
		while (i < jugadores.size()) {
			JButton boton = new JButton("Ver puntaje tablero " + (i + 1));
			boton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarPuntaje(e.getSource());
				}
			});
			panelBotones.add(boton);
			i++;
		}

		this.add(panelBotones, BorderLayout.SOUTH);
	}

	protected void mostrarPuntaje(Object object) {
		String texto=((JButton)object).getText();
		int indice=Character.getNumericValue(texto.charAt(texto.length()-1));
		
		Tablero tablero = jugadores.get(indice-1).getTablero();
		String puntaje=tablero.puntajeTotal(this);
		Ficha[][] matriz=tablero.getTablero();
		for (Ficha[] fichas : matriz) {
			for (Ficha ficha : fichas) {
				if(ficha != null) {
					ficha.setPuntajeContado(false);
				}
			}
		}
		JOptionPane.showMessageDialog(this, puntaje, "Puntaje",JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostrarInfo(String string) {
		info.setText(string);
	}

}
