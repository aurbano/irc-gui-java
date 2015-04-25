package es.uniovi;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Espera a que el usuario escriba mensajes, y los envia al buffer de salida.
 * Los mensajes que escribe el usuario llegan a través del buffer messageQueue.
 */
public class HiloEntrada extends Thread{
	
	ArrayBlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(20);
	
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
		String line;
		Comando cmd;
		try{
			while(!ClienteChat.quit){
				line = messageQueue.take();
				// Genera el comando
				cmd = new Comando(line);
				// Envia el comando
				ClienteChat.netOut.send(cmd);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
