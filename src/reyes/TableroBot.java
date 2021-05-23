package reyes;

import java.util.List;

public class TableroBot extends Tablero{

	public TableroBot(int tamTablero) {
		super(tamTablero);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<String> puntajeTotal() {
		// TODO Auto-generated method stub
		List<String> ret = super.puntajeTotal();
		for (Ficha[] fichas : this.tablero) {
			for (Ficha ficha : fichas) {
				if(ficha != null) {
					ficha.setPuntajeContado(false);
				}
			}
		}
		return ret;
	}
	
	

}
