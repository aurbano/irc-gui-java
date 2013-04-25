package es.uniovi;

import java.util.*;

/**
 * Define los elementos de tipo Respuesta para guardar lo que llega de la red
 */
public class Respuesta {
	String type;
	int status;
	String params;
	
	/**
	 * Separa el texto recibido, diferenciando entre el tipo y los parámetros.
	 * @param Mensaje recibido
	 */
	public Respuesta(int code, int status, String message){
		HashMap<Integer,String> tabla = new HashMap<Integer,String>();
		tabla.put(0,"OTROS");
		tabla.put(1, "MSG");
		tabla.put(2, "JOIN");
		tabla.put(3, "LEAVE");
		tabla.put(4, "NICK");
		tabla.put(5, "QUIT");
		tabla.put(16, "LIST");
		tabla.put(17, "WHO");
		tabla.put(32, "HELLO");
		
		type = tabla.get(code);
		this.status = status;
		this.params = message;
	}
}
