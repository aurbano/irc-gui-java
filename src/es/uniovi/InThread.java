package es.uniovi;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Intermediary between the UI and the backend that talks IRC
 */
class InThread extends Thread{
	
	final ArrayBlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(20);
	
	/**
	 * Run the thread
	 */
	public InThread(){
		this.start();
	}

	public void run(){
		String line;
		Command cmd;
		try{
			while(!ChatClient.quit){
				line = messageQueue.take();
				// Generate the command
				cmd = new Command(line);
				// Send the command
				ChatClient.netOut.send(cmd);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
