package es.uniovi;

import java.util.*;

public class Response {
	final String type;
	final int status;
	final String[] params;
	
	/**
	 * Create a response object
	 * @param code Message code
	 * @param status Message status
	 * @param message Parameters for the message
	 */
	public Response(int code, int status, String[] message){
		HashMap<Integer,String> table = new HashMap<>();
		table.put(0, "OTHERS");
		table.put(1, "MSG");
		table.put(2, "JOIN");
		table.put(3, "LEAVE");
		table.put(4, "NICK");
		table.put(5, "QUIT");
		table.put(16, "LIST");
		table.put(17, "WHO");
		table.put(32, "HELLO");
		
		type = table.get(code);
		this.status = status;
		this.params = message;
	}
}
