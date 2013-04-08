package es.uniovi;

/**
 * Hilo de salida, espera a que lleguen nuevos mensajes y los muestra por pantalla
 */
public class HiloSalida extends Thread{
	/**
	 * Constructor
	 */
	public HiloSalida(){
		this.start();
	}
	
	/**
	 * Método run
	 */
	public void run(){
		Respuesta resp;
		while(true){
			try{
				resp = ClienteChat.netIn.remove();
				switch(resp.type){
					case "/MSG":
						// Mostramos segun el formato especificado
						if(resp.params.length > 2){
							System.out.println(resp.params[0]+"|"+resp.params[1]+"> "+resp.params[2]);
						}
						break;
					default:
						System.out.println("> Unknown type: "+resp.toString());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
