package unlam.prograva.taller.client;

import java.io.DataInputStream;
import java.net.Socket;

public class ClienteHilo extends Thread{
	
	private Socket cliente;
	
	public ClienteHilo ( Socket cliente) {
		this.cliente = cliente ;
	}
	
	@SuppressWarnings("resource")
	public void run() {
		String texto = "";
		try {
			while(true) {
				texto = new DataInputStream(cliente.getInputStream()).readUTF();
				System.out.println(texto + "\n");
			}
			
		}catch ( Exception e ) {
			System.err.println("Se ha desconectado el cliente");
		}
	}

}
