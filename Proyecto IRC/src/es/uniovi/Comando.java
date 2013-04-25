package es.uniovi;

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
			this.params = new String[]{"/MSG",ClienteChat.nick, ClienteChat.sala, text};
		}
	}
	
	/**
	 * Devuelve el comando como String en el formato adecuado
	 * @return Comando en string
	 */
	public byte[] get(){
		HashMap<String,Integer> tabla = new HashMap<String,Integer>();
		tabla.put("/MSG", 1);
		tabla.put("/JOIN", 2);
		tabla.put("/LEAVE", 3);
		tabla.put("/NICK", 4);
		tabla.put("/QUIT", 5);
		tabla.put("/LIST", 10);
		tabla.put("/WHO", 11);
		
		String content = "";
		for(int i=1; i<params.length; i++){
			content+= params[i];
		}
		Integer size = new Integer(content.getBytes().length);
		
		ByteBuffer command = ByteBuffer.allocate(4+size);
	
		command.putInt(tabla.get(params[0]));
		command.putInt(size);
		command.put(content.getBytes());
		
		return command.array();		
	}
}
