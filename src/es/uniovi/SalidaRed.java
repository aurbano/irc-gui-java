package es.uniovi;

import java.io.DataOutputStream;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * SalidaRed tiene un buffer de comandos, donde espera a que aparezcan.
 * Cada vez que tiene un comando disponible 
 */
public class SalidaRed extends Thread{
	
	private ArrayBlockingQueue<Comando> outQueue;
	
	volatile boolean running = true;
	
	public void termina() {
		running = false;
		this.interrupt();
	}
	
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
			while(running){
				try{
					// Espera nuevos comandos
					c = outQueue.take();
					// Cuando llega alguno intenta enviarlo
					out.write(c.get());
				}catch(NullPointerException e){
					System.out.println("Error: Comando invalido");
				}
			}
		}catch(SocketException e){
			return;
		}catch(InterruptedException e){
			return;
		}catch(Exception e){
			ClienteChat.close();
			e.printStackTrace();
		}
	}
}
