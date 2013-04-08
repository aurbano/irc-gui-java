package es.uniovi;

/**
 * Contiene toda la informacion de un comando IRC
 */

public class Comando {
	String[] params;
	
	public Comando(String[] params){
		this.params = params;
	}
	
	/**
	 * Devuelve la cadena completa
	 */
	public String get(){
		// /MSG requiere algunas cosas extra
		if(params[0]=="/MSG"){
			// Añadimos los parametros adicionales
			params[0] += ";"+ClienteChat.nick+";"+ClienteChat.sala;
		}
		for(int i=0;i<params.length;i++){
			ret += ";"+params[i];
		}
		return ret;
	}
}
