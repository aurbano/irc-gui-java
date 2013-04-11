package es.uniovi;

/**
 * Define los elementos de tipo Respuesta para guardar lo que llega de la red
 */
public class Respuesta {
	String type;
	String[] params;
	
	/**
	 * Separa el texto recibido, diferenciando entre el tipo y los parámetros.
	 * @param Mensaje recibido
	 */
	public Respuesta(String message){
		String[] parts = message.split(";");
		//El tipo viene indicado como primer argumento
		int offset = 0;
		if(parts[0].startsWith("/")){
			type = parts[0];	
			offset = 1;
		}else{
			type = "/MSG";
		}
		//Creamos un nuevo vector para los parámetros, excluyendo el tipo
		params = new String[parts.length-offset];
		
		for(int i=0;i<params.length;i++){
			params[i] = parts[i+offset];
		}
	}
	
	/**
	 * Devuelve la respuesta como string, principalmente
	 * para poder debuggear de manera comoda
	 * @return El comando completo recibido
	 */
	public String toString(){
		String ret = type;
		for(int i=0;i<params.length;i++){
			ret += "; "+params[i];
		}
		return ret;
	}
}
