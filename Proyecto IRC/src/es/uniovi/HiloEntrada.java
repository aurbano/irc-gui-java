package es.uniovi;
import java.io.*;

/**
 * Espera a que el usuario escriba mensajes, y los envia al buffer de salida
 */
public class HiloEntrada extends Thread{
	
	/**
	 * Constructor de la clase, lanza el hilo
	 */
	public HiloEntrada(){
		this.start();
	}

	/**
	 * Método run
	 */
	public void run(){
		LineNumberReader text = new LineNumberReader (new InputStreamReader(System.in));
		String line;
		Comando cmd;
		while(!ClienteChat.quit){
			try{
				line = text.readLine();
				// Genera el comando
				cmd = new Comando(line);
				// Envia el comando
				ClienteChat.netOut.send(cmd);
				
			}catch(IOException e){
				e.printStackTrace();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	
		
	}
	
}
