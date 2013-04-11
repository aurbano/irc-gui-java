package es.uniovi;

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
	public String get(){
		String ret = "";
		for(int i=0;i<params.length;i++){
			ret += params[i];
			if(i<params.length-1) ret += ";";
		}
		return ret;
	}
}
