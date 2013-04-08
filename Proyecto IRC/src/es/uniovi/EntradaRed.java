package es.uniovi;

import java.util.concurrent.ArrayBlockingQueue;

public class EntradaRed extends Thread {
	
	private ArrayBlockingQueue<Respuesta> inQueue;
	
	public EntradaRed(){
		inQueue = new ArrayBlockingQueue<Respuesta>(20);
		this.start();
	}
	
	public void run(){
		String message;
		Respuesta res;
		while(true){
			try{
				message = ClienteChat.net.recv();
				if(message.length() > 0){
					res = new Respuesta(message);
					add(res);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
	}
	
	public void add(Respuesta ans) throws InterruptedException{
		inQueue.put(ans);
	}
	
	public Respuesta remove() throws InterruptedException{
		return inQueue.take();
	}
}
