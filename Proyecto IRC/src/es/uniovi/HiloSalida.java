package es.uniovi;

/**
 * Hilo de salida, espera a que lleguen nuevos mensajes y los muestra por pantalla
 * también determina cómo tratar cada comando.
 */
public class HiloSalida extends Thread{
	/**
	 * Constructor, lanza el hilo
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
				/*
				 * Implementamos algo de logica para los diferentes comandos
				 * de cara al siguiente hito.
				 */
					case "MSG":
						String[] params = resp.params.split(";", 2);
						if(params.length > 2){
							System.out.println(params[1]+"|"+params[0]+"> " +params[2]);
						}
						break;
					case "HELLO":
						System.out.println(">> Conectado: "+resp.params);
						break;
						
					default:
						System.out.println("> Unknown type: "+resp.params);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
