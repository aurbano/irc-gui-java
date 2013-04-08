package es.uniovi;

public class Respuesta {
	String type;
	String[] params;
	
	/**
	 * Separa el texto recibido, diferenciando entre el tipo y los parámetros.
	 * @param message , mensaje recibido
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
	
	public String toString(){
		String ret = type;
		for(int i=0;i<params.length;i++){
			ret += "; "+params[i];
		}
		return ret;
	}
}
