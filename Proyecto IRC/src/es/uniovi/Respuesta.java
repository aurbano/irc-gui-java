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
		type = parts[0];	
		//Creamos un nuevo vector para los parámetros, excluyendo el tipo
		params = new String[parts.length-1];
		
		for(int i=1;i<params.length;i++){
			params[i-1] = parts[i];
		}
	}
}
