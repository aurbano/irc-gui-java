package es.uniovi;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Contiene toda la información de un comando IRC
 */

public class Comando {
	String[] params;
	
	/**
	 * Constructor para la clase Comando
	 * toma el texto introducido por el usuario como parámetro
	 * @param Comando introducido por el usuario
	 */
	public Comando(String text){
		// Genera el string de parametros:
		if (text.startsWith("/")){
			this.params = text.split(" ");
		}else{
			// /MSG requiere algunas cosas extra
			this.params = new String[]{"/MSG", text, ClienteChat.sala};
		}
		params[0] = params[0].toUpperCase();
	}
	
	/**
	 * Devuelve el comando como array de bytes en el formato del protocolo
	 * @return Array de bytes con el comando y el mensaje
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] get() throws UnsupportedEncodingException{
		HashMap<String,Short> tabla = new HashMap<String,Short>();
		tabla.put("/MSG", (short)1);
		tabla.put("/JOIN", (short)2);
		tabla.put("/LEAVE", (short)3);
		tabla.put("/NICK", (short)4);
		tabla.put("/QUIT", (short)5);
		tabla.put("/LIST", (short)16);
		tabla.put("/WHO", (short)17);
		
		// La "carga" para calcular el tamaño
		String content = "";
		if(params.length>1){
			content = params[1];
			if(params[0]=="/MSG") content += params[2];
		}
		
		Short size = new Short((short)(content.getBytes("UTF-8").length + params.length*2));
		
		ByteBuffer command = ByteBuffer.allocate(4+size);
	
		command.putShort(tabla.get(params[0]));
		command.putShort(size);
		if(params.length > 1 && size>0){
			command.putShort((short)(params.length-1));
			if(params[0]=="/MSG"){
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
