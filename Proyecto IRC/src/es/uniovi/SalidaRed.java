package es.uniovi;

import java.io.DataOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * SalidaRed tiene un buffer de comandos, donde espera a que aparezcan.
 * Cada vez que tiene un comando disponible 
 */
public class SalidaRed extends Thread{
	
	private ArrayBlockingQueue<Comando> outQueue;
	
	/**
	 * Constructor de la clase, inicia el buffer de salida y lanza el hilo
	 */
	public SalidaRed(){
		outQueue = new ArrayBlockingQueue<Comando>(20);
		this.start();
	}
	
	/**
	 * Guarda comandos en el buffer de salida de red
	 * @param cmd Comando a guardar en el buffer
	 */
	public void send(Comando cmd) throws InterruptedException{
		outQueue.put(cmd);
	}
	
	/**
	 * Metodo run, espera a que aparezcan mensajes en el buffer para
	 * enviarlos a través de Network
	 */
	public void run(){
		Comando c;
		try{
			DataOutputStream out = new DataOutputStream(ClienteChat.s.getOutputStream());
			while(!ClienteChat.quit){
				// Espera nuevos comandos
				c = outQueue.take();
				// Cuando llega alguno intenta enviarlo
				out.write(c.get());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
