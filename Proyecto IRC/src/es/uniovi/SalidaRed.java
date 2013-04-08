package es.uniovi;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * SalidaRed tiene un buffer de comandos, donde espera a que aparezcan.
 * Cada vez que tiene un comando disponible 
 */
public class SalidaRed extends Thread{
	
	private ArrayBlockingQueue<Comando> outQueue;
	
	public SalidaRed(){
		outQueue = new ArrayBlockingQueue<Comando>(20);
		this.start();
	}
	
	/**
	 * Guarda comandos en la cola de salida de red
	 * @param cmd
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
		Network net = new Network();
		try{
			while(true){
				// Espera nuevos comandos
				c = outQueue.take();
				// Cuando llega alguno intenta enviarlo usando Network
				net.send(c.get());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
