package es.uniovi;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Clase de Entrada de red, recibe los mensajes y los va guardando
 * en un buffer de entrada.
 */
public class EntradaRed extends Thread {
	
	private ArrayBlockingQueue<Respuesta> inQueue;
	private boolean running = true;
	
	/**
	 * Constructor de la clase, inicializa el buffer y lanza el hilo.
	 */
	public EntradaRed(){
		inQueue = new ArrayBlockingQueue<Respuesta>(20);
		this.start();
	}
	
	public void termina() {
		running = false;
		this.interrupt();
	}
	
	public void run(){
		int status, code;
		Respuesta res;
		try{
			DataInputStream in = new DataInputStream(ClienteChat.s.getInputStream());
			while(running){
				try{
					// Lee los 2 primeros bytes
					status = in.readByte();
					code = in.readByte();
					// Lee el tamaño de la carga
					in.readShort();
					// Lee el numero de parametros
					short paramsNum = in.readShort();
					short paramLength;
					String[] params = new String[paramsNum];
					for(int i=0;i<paramsNum;i++){
						paramLength = in.readShort();
						byte[] param = new byte[paramLength];
						in.readFully(param);
						params[i] = new String(param,"UTF-8");
						
						/*//Debug info
						System.out.println("Param "+i+": Length= "+paramLength);
						System.out.println("	"+params[i]);//*/
					}
					
					/*// Debug info
					System.out.println("Recibido:");
					System.out.println("	Status= "+status);
					System.out.println("	Code= "+code);
					System.out.println("	Tamaño="+length);
					System.out.println("	Params= "+paramsNum);
					System.out.println("	Mensaje: "+params[0]);
					System.out.println("-----------");//*/
					
					// Guardamos el mensaje recibido en la cola
					res = new Respuesta(code,status,params);
					add(res);
				}catch(EOFException e){
					return;
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
