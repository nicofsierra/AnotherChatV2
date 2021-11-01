package unlam.prograva.taller.server;

import java.util.ArrayList;
import java.util.List;

public class Sala {
	
	private List<String> salas = new ArrayList<String>();

	public Sala() {}
	
	public void CrearSala( String nombre ) {
		salas.add(nombre);
	}

	public List<String> getSalas() {
		return salas;
	}

	@Override
	public String toString() {
		return "Sala [salas=" + salas + "]";
	}
	
	
}
