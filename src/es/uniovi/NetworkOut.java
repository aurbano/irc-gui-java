package es.uniovi;

import java.io.DataOutputStream;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Network Out helper class, it implements a command buffer
 * using an Array Blocking Queue for multi threading support.
 */
public class NetworkOut extends Thread{
	
	private ArrayBlockingQueue<Comand> outQueue;
	
	volatile boolean running = true;
	
	public void finish() {
		running = false;
		this.interrupt();
	}
	
	/**
	 * Initialize buffer and run the thread
	 */
	public NetworkOut(){
		outQueue = new ArrayBlockingQueue<Comand>(20);
		this.start();
	}
	
	/**
	 * Adds commands to the output queue
	 * @param cmd Command
	 */
	public void send(Comand cmd) throws InterruptedException{
		outQueue.put(cmd);
	}
	
	/**
	 * Continuously check the output queue for new commands,
	 * sending them when available.
	 */
	public void run(){
		Comand c;
		try{
			DataOutputStream out = new DataOutputStream(ChatClient.s.getOutputStream());
			while(running){
				try{
					// Get a new command
					c = outQueue.take();
					// Send it
					out.write(c.get());
				}catch(NullPointerException e){
					System.out.println("Error: Invalid Command");
				}
			}
		}catch(SocketException e){
			System.out.println(e.getMessage());
		}catch(Exception e){
			ChatClient.close();
			e.printStackTrace();
		}
	}
}
