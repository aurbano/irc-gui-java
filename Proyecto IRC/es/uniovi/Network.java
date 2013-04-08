package es.uniovi;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
* Representa la red dentro del cliente de chat.
*/
public class Network extends Thread {
	
	private static final int MAX_USERS = 10;
	private static final int MAX_WAITING_TIME_SECS = 20;
	private static final String[] ROOM = new String[]{ "pruebas", "vacaciones" };
	private ArrayBlockingQueue<String> msgQueue;
	private static final int MAX_MESSAGES = 20;
	private boolean closed;
	
	/**
	* Crea un objeto de tipo Network.
	*/
	public Network() {
		msgQueue = new ArrayBlockingQueue<String>(MAX_MESSAGES);
		setName("Hilo de red");
		closed = false;
		start();
	}
	
	/**
	* Cierra la red.
	* @throws IllegalStateException si la red ya está cerrada.
	*/
	public void close() throws IllegalStateException {
		testClosed();
		interrupt();
		closed = true; 
	}
	
	/**
	* Recibe el siguiente mensaje de la red bloqueando el hilo si no hay 
	* mensajes pendientes.
	* @return el mensaje recibido.
	* @throws IllegalStateException si la red está cerrada.
	* @throws InterruptedException si el hilo es bloqueado mientras espera por un mensaje.
	*/
	public String recv() throws IllegalStateException, InterruptedException {
		testClosed();
		return msgQueue.take();
	}
	
	/**
	* Envía un mensaje a la red bloqueando el hilo si no hay espacio para enviar mensajes.
	* @param msg el mensaje a enviar.
	* @throws IllegalStateException si la red está cerrada.
	* @throws InterruptedException si el hilo es bloqueado mientras espera por un mensaje.
	*/
	public void send(String msg) throws IllegalStateException, InterruptedException {
		testClosed();
		msgQueue.put(msg);
	}
}