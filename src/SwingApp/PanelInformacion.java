package SwingApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import reyes.Jugador;
import reyes.Tablero;

public class PanelInformacion extends JPanel {
	int i = 0;

	private static final long serialVersionUID = -6580547458892714155L;
	JTextPane info;
	List<JButton> botones;
	private List<Jugador> jugadores;

	PanelInformacion(List<Jugador> jugadores, int largoPanel, int altoPanel) {
		this.jugadores = jugadores;
		setLayout(new BorderLayout());
		info = new JTextPane();
		info.setEditable(false);
		info.setBackground(new Color(0xE3BB86));
		info.setForeground(Color.BLACK);
		info.setFont(new Font("Arial",Font.CENTER_BASELINE,15));
		StyledDocument doc = info.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		this.add(info, BorderLayout.CENTER);
		
		JPanel panelBotones = new JPanel();
		botones=new ArrayList<JButton>();
		panelBotones.setLayout(new GridLayout(jugadores.size(), 1));
		while (i < jugadores.size()) {
			JButton boton = new JButton("Ver puntaje tablero " + (i + 1));
			botones.add(boton);
			boton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarPuntaje(e.getSource());
				}
			});
			panelBotones.add(boton);
			i++;
		}
		panelBotones.setPreferredSize(new Dimension(largoPanel,altoPanel/4));
		this.add(panelBotones, BorderLayout.SOUTH);
	}

	protected void mostrarPuntaje(Object object) {
		String texto = ((JButton) object).getText();
		int indice = Character.getNumericValue(texto.charAt(texto.length() - 1));

		Tablero tablero = jugadores.get(indice - 1).getTablero();
		String puntaje = tablero.puntajeTotal(this);
		JOptionPane.showMessageDialog(this, puntaje, "Puntaje", JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostrarInfo(String string) {
		info.setText(string);
	}

	public void deshabilitarBotones() {
		for(JButton boton:botones) {
			boton.setEnabled(false);
		}
	}

	public void habilitarBotones() {
		for(JButton boton:botones) {
			boton.setEnabled(true);
		}		
	}

}
