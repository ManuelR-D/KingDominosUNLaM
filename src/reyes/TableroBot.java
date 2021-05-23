package reyes;

public class TableroBot extends Tablero{

	public TableroBot(int tamTablero) {
		super(tamTablero);
	}

	@Override
	public int puntajeTotal(boolean mostrarRegiones) {
		int ret = super.puntajeTotal(mostrarRegiones);
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
