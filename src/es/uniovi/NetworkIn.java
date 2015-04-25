package es.uniovi;

import java.io.*;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Receives messages and adds them to an input queue
 */
class NetworkIn extends Thread {
	
	private final ArrayBlockingQueue<Response> inQueue;
	
	private volatile boolean running = true;
	
	public void finish() {
		running = false;
		this.interrupt();
	}
	
	public NetworkIn(){
		inQueue = new ArrayBlockingQueue<>(20);
		this.start();
	}
	
	public void run(){
		int status, code;
		Response res;
		try{
			DataInputStream in = new DataInputStream(ChatClient.s.getInputStream());
			while(running){
				try{
					// Read first 2 bytes
					status = in.readByte();
					code = in.readByte();
					// Get payload size
					in.readShort();
					// Parameter count
					short paramsNum = in.readShort();
					short paramLength;
					String[] params = new String[paramsNum];
					for(int i=0;i<paramsNum;i++){
						paramLength = in.readShort();
						byte[] param = new byte[paramLength];
						in.readFully(param);
						params[i] = new String(param,"UTF-8");
					}
					
					// Store the message in the queue
					res = new Response(code,status,params);
					add(res);
				}catch(EOFException e){
					System.out.println("EOF");
					break;
				}
			}
		}catch(SocketException e){
			System.out.println(e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a response to the input stream
	 * @param ans Received response
	 * @throws InterruptedException if the thread is locked while waiting
	 */
	private void add(Response ans) throws InterruptedException{
		inQueue.put(ans);
	}
	
	/**
	 * Get the last element from th buffer
	 * @return Response Server response
	 * @throws InterruptedException if the thread is locked while waiting
	 */
	public Response remove() throws InterruptedException{
		return inQueue.take();
	}
}
