package unlam.prograva.taller.server;

public class Usuario {
	
	private int id;
	private String nombre;
	private String ip;
	
	public Usuario(int id, String nombre, String ip) {
		this.id = id;
		this.nombre = nombre;
		this.ip = ip;
	}
	
	public String getUsuario() {
		return this.nombre;
	}
	
}
