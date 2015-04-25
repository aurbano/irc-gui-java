package es.uniovi;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Maps text identified commands to their binary representation
 */

public class Comand {
	String[] params;
	
	/**
	 * Generate a new command
	 * @param text Command string
	 */
	public Comand(String text){
		if (text.startsWith("/")){
			this.params = text.split(" ");
		}else{
			// /MSG requires a special treatment.
			this.params = new String[]{"/MSG", text, ChatClient.room};
		}
		params[0] = params[0].toUpperCase();
	}
	
	/**
	 * Returns the command as a byte array
	 * @return Command as byte array
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] get() throws UnsupportedEncodingException{
		HashMap<String,Short> tabla = new HashMap<>();
		tabla.put("/MSG", (short)1);
		tabla.put("/JOIN", (short)2);
		tabla.put("/LEAVE", (short)3);
		tabla.put("/NICK", (short)4);
		tabla.put("/QUIT", (short)5);
		tabla.put("/LIST", (short)16);
		tabla.put("/WHO", (short)17);
		
		// Calculate the command size
		String content = "";
		if(params.length>1){
			content = params[1];
			if(params[0].equals("/MSG")) content += params[2];
		}
		
		Short size = (short) (content.getBytes("UTF-8").length + (params.length * 2));
		
		ByteBuffer command = ByteBuffer.allocate(4+size);
	
		command.putShort(tabla.get(params[0]));
		command.putShort(size);
		if(params.length > 1 && size>0){
			command.putShort((short)(params.length-1));
			if(params[0].equals("/MSG")){
				command.putShort((short)params[2].getBytes("UTF-8").length);
				command.put(params[2].getBytes("UTF-8"));
				command.putShort((short)params[1].getBytes("UTF-8").length);
				command.put(params[1].getBytes("UTF-8"));
			}else{
				command.putShort((short)params[1].getBytes("UTF-8").length);
				command.put(params[1].getBytes("UTF-8"));
			}
		}
		return command.array();		
	}
}
