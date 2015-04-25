package es.uniovi;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Intermediary between the UI and the backend that talks IRC
 */
public class InThread extends Thread{
	
	ArrayBlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(20);
	
	/**
	 * Run the thread
	 */
	public InThread(){
		this.start();
	}

	public void run(){
		String line;
		Comand cmd;
		try{
			while(!ChatClient.quit){
				line = messageQueue.take();
				// Generate the command
				cmd = new Comand(line);
				// Send the command
				ChatClient.netOut.send(cmd);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
