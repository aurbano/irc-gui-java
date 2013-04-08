package es.uniovi;

/**
 * Contiene toda la informacion de un comando IRC
 */

public class Comando {
	String[] params;
	
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
	 * Devuelve la cadena completa
	 */
	public String get(){
		String ret = "";
		for(int i=0;i<params.length;i++){
			ret += params[i];
			if(i<params.length-1) ret += ";";
		}
		return ret;
	}
}
