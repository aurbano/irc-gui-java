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
		String ret = params[0]+ClienteChat.nick;
		for(int i=1;i<params.length;i++){
			ret += ";"+params[i];
		}
		return ret;
	}
}
