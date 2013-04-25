package es.uniovi;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Clase de Entrada de red, recibe los mensajes y los va guardando
 * en un buffer de entrada.
 */
public class EntradaRed extends Thread {
	
	private ArrayBlockingQueue<Respuesta> inQueue;
	
	/**
	 * Constructor de la clase, inicializa el buffer y lanza el hilo.
	 */
	public EntradaRed(){
		inQueue = new ArrayBlockingQueue<Respuesta>(20);
		this.start();
	}
	
	public void run(){
		String message;
		Respuesta res;
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(ClienteChat.s.getInputStream()));
			while(true){
				try{
					message = in.readLine();
					System.out.println("Rewcibido");
					System.out.println(message);
					/*if(message.length() > 0){
						res = new Respuesta(message);
						add(res);
					}*/
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Añade un elemento al buffer de entrada, puede lanzar una
	 * excepcion.
	 * @param ans Elemento Respuesta para añadir
	 * @throws InterruptedException si el hilo es bloqueado mientras espera por un mensaje.
	 */
	public void add(Respuesta ans) throws InterruptedException{
		inQueue.put(ans);
	}
	
	/**
	 * Extrae el ultimo elemento del buffer, puede lanzar una excepcion
	 * @return Respuesta Elemento de tipo Respuesta
	 * @throws InterruptedException si el hilo es bloqueado mientras espera por un mensaje.
	 */
	public Respuesta remove() throws InterruptedException{
		return inQueue.take();
	}
}
