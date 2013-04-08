package es.uniovi;

/**
 * Contiene toda la informacion de un comando IRC
 */

public class Comando {
	String type;
	String[] params;
	
	public Comando(String type, String[] params){
		if(type.length()==0) type = "/MSG";
		this.type = type;
		this.params = params;
	}
	
	/**
	 * Devuelve la cadena completa
	 */
	public String get(){
		String ret = type+ClienteChat.nick;
		for(int i=0;i<params.length;i++){
			ret += ";"+params[i];
		}
		return ret;
	}
}
