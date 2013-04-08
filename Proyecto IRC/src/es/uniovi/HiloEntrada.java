package es.uniovi;
import java.io.*;

/**
 * Espera a que el usuario escriba mensajes, y los envia al buffer de salida
 */
public class HiloEntrada extends Thread{
	
	public HiloEntrada(){
		// Lanza el hilo
		this.start();
	}

	public void run(){
		LineNumberReader text = new LineNumberReader (new InputStreamReader(System.in));
		String line;
		Comando cmd;
		while(true){
			try{
				line = text.readLine();
				// Genera el comando
				cmd = new Comando(line);
				// Envia el comando
				ClienteChat.out.send(cmd);
				
			}catch(IOException e){
				e.printStackTrace();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	
		
	}
	
}
