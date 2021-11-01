package unlam.prograva.taller.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServidorHilo extends Thread{
	
	Socket cliente = new Socket();
	private ArrayList<Socket> lista;
	
	public ServidorHilo( Socket cliente, ArrayList<Socket> lista) {
		super();
		this.cliente = cliente;
		this.lista = lista ;
	}
	
	public void run() {
		String texto = "";
		
		try {
			texto = new DataInputStream(cliente.getInputStream()).readUTF();
			while(texto!="Salir") {
				System.out.println(texto);
				for( Socket indice : lista ) {
					if( indice != cliente ) {
						new DataOutputStream(indice.getOutputStream()).writeUTF(texto);
					}
				}
				texto = new DataInputStream(cliente.getInputStream()).readUTF();
			}
			System.out.println("El cliente se ha desconectado");
		}catch( Exception e ) {
			System.out.println("El cliente se ha desconectado");
		}
		
	}

}
